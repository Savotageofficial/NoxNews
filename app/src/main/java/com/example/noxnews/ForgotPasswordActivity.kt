package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noxnews.databinding.ActivityForgotPasswordBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        lateinit var auth: FirebaseAuth
        auth = Firebase.auth


        binding.submitBtn.setOnClickListener {

            var emailAddress = binding.emailEt.text.toString()

            auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password reset email sent successfully
                        Toast.makeText(this, "Password reset email sent to $emailAddress", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java)) // Go back to the login screen
                    } else {
                        // Handle the error (e.g., email not found, network issues)
                        Toast.makeText(this, "Error sending password reset email: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                    }
                }
        }


    }
}