package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noxnews.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        if (FirebaseService.auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
            return
        }// check if already logged in
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "u forgot to type in YOUR DATA!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.tvForgotPass.setOnClickListener {
                val intent = Intent(this , ForgotPasswordActivity::class.java)
                startActivity(intent)
                finish()
            }

            //disables the  button till i get your stuff
            binding.btnLogin.isEnabled = false

            AuthRepository.login(email, password).addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }// DON'T DELETE THIS, it exits the app if pressed on back instead of login screen
                startActivity(intent)
                finish()
            }.addOnFailureListener { e ->
                Toast.makeText(this, "u missed up: ${e.message}", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true //return button functionality
            }
        }
    }
}