package org.freedu.fcmapp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import org.freedu.fcmapp.databinding.ActivityMainBinding
import org.freedu.fcmapp.viewmodel.NotificationViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel:NotificationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d("FCM", "Subscribed to all topic successfully")
                }else{
                    Log.d("FCM", "Failed to subscribe to all topic")
                }
            }

        binding.sendBtn.setOnClickListener {
            val title = binding.messageTitle.text.toString().trim()
            val body = binding.messageBody.text.toString().trim()

            if(title.isNotEmpty() && body.isNotEmpty()){
                viewModel.sendNotification(title, body)
            }else{
                Toast.makeText(this, "Please, Enter message title and message body...", Toast.LENGTH_SHORT).show()
            }

            viewModel.notificationSent.observe(this, Observer {
                if(it){
                    Toast.makeText(this, "Notification Sent Successfully!", Toast.LENGTH_SHORT).show()
                }
            })

            viewModel.errorMessage.observe(this, Observer {
                it.let {
                    Toast.makeText(this,"Error $it", Toast.LENGTH_SHORT).show()
                }
            })
        }

    }
}