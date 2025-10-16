package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noxnews.AuthRepository.getUserData
import com.example.noxnews.AuthRepository.logout
import com.example.noxnews.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import javax.annotation.Nonnull

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ThemeHelper.applySavedTheme(this)
        //night mode helper

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userid = Firebase.auth.currentUser?.uid

        if (userid != null) {
            getUserData(userid).addOnSuccessListener { user ->
                if (user != null) {
                    val username = user.name
                    binding.welcomeText.text = "Welcome ${username}"
                }
            }
        }



        val intent = Intent(this , MainActivity::class.java)

        binding.btn1.setOnClickListener {

            intent.putExtra("catID" , "General")
            startActivity(intent)

        }
        binding.btn2.setOnClickListener {

            intent.putExtra("catID" , "Sports")
            startActivity(intent)

        }
        binding.btn3.setOnClickListener {

            intent.putExtra("catID" , "Entertainment")
            startActivity(intent)

        }
        binding.btn4.setOnClickListener {

            intent.putExtra("catID" , "Health")
            startActivity(intent)

        }
        binding.btn5.setOnClickListener {

            intent.putExtra("catID" , "Science")
            startActivity(intent)
        }
        binding.btn6.setOnClickListener {
            intent.putExtra("catID" , "Technology")
            startActivity(intent)

        }

//        binding.settings.setOnClickListener {
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu , menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected( item: MenuItem): Boolean {

        when(item.itemId){
            R.id.fav_btn -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
            }
            R.id.settings_btn -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout_btn -> {
                logout()
                finishAffinity()
                startActivity(Intent(this, LoginActivity::class.java))}
        }
        return super.onOptionsItemSelected(item)
    }
}