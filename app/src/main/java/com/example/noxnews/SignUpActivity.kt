package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.noxnews.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //checkIfLoggedIn()//if logged in, moves to home activity right away, wingardium liviosa type stuff

        binding.tvLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }//starts login activity when clicked "already have an account?"

        binding.btnSignup.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            when {
                name.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                    Toast.makeText(this, "there are no such thing as void emails, this isn't galactic express", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    Toast.makeText(this, "Password too short (min 6 chars)", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            binding.btnSignup.isEnabled = false
            binding.loadingProgress.isVisible = true


            //verify email while signing up, so we can reuse auth repo as much as we like.
            AuthRepository.signUp(name, email, password)
                .addOnSuccessListener { result ->
                    val user = FirebaseService.auth.currentUser

                    user?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Verification email sent. Check your inbox.",
                                Toast.LENGTH_LONG
                            ).show()
                            FirebaseService.auth.signOut()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()//moves to login so u login with only authenticated email
                        }
                        ?.addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Failed to send verification email: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.btnSignup.isEnabled =
                                true //unlocks the btn for you to try again
                            binding.loadingProgress.isVisible = false
                        }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Signup failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.btnSignup.isEnabled = true
                    binding.loadingProgress.isVisible = false //to make it extra invisible
                }


        }

    }
    private fun checkIfLoggedIn() {
        val user = FirebaseService.auth.currentUser

        if (user == null) {
            Toast.makeText(this, "No user logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        // Reload user data from Firebase server
        user.reload()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val refreshedUser = FirebaseService.auth.currentUser
                    if (refreshedUser != null && refreshedUser.isEmailVerified) {
                        Toast.makeText(this, "Welcome back, verified user!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, HomeActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Your email is not verified.", Toast.LENGTH_SHORT).show()
                        FirebaseService.auth.signOut()
                    }
                } else {
                    Toast.makeText(this, "Failed to reload user data.", Toast.LENGTH_SHORT).show()
                    FirebaseService.auth.signOut()
                }
            }
    }//toke it outside for readability


}