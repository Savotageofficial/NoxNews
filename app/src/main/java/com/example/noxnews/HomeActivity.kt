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
import com.example.noxnews.databinding.ActivityHomeBinding
import javax.annotation.Nonnull

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityHomeBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.btn1.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "general")
            startActivity(intent)

        }
        binding.btn2.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "sports")
            startActivity(intent)

        }
        binding.btn3.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "entertainment")
            startActivity(intent)

        }
        binding.btn4.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "health")
            startActivity(intent)

        }
        binding.btn5.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "science")
            startActivity(intent)
        }
        binding.btn6.setOnClickListener {

            val intent = Intent(this , MainActivity::class.java)
            intent.putExtra("catID" , "technology")
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
                Toast.makeText(this, "favourites icon", Toast.LENGTH_SHORT).show()
            }
            R.id.settings_btn -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.logout_btn -> {
                Toast.makeText(this, "logout icon", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}