package org.freedu.fcmapp.repository

import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FCMRepository {
    private val serverKey =
        "BBJzt76pG1edKYpLuI8IBEnpWj5xniVW5Q8eO3nl36DAm4KG4Lv_eq9148gqmT_AGBhu9kBSe1Cveu82IrI9mUQ"
    private val fcmUrl = "https://fcm.googleapis.com/fcm/send"
    private val client = OkHttpClient()

    fun sendNotification(
        title: String,
        body: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val json = JSONObject()
        val notificationJson = JSONObject()

        notificationJson.put("title", title)
        notificationJson.put("body", body)
        json.put("to", "/topics/all")
        json.put("notification", notificationJson)

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaType(),
            json.toString()
        )

        val request = Request.Builder()
            .url(fcmUrl)
            .post(requestBody)
            .addHeader("Authorization", "key=$serverKey")
            .addHeader("Content-Type", "application/json")
            .build()

        client.newCall(request).enqueue(object :Callback{
            override fun onFailure(call: Call, e: IOException) {
                onFailure(e)
            }

            override fun onResponse(call: Call, response: Response) {
                if(response.isSuccessful){
                    onSuccess()
                }else{
                    onFailure(Exception("Notification failed with status code ${response.code}"))
                }
            }

        })
    }

}