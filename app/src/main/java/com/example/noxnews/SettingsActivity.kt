package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.noxnews.AuthRepository.logout
import com.example.noxnews.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val countryMap = mapOf(
            "eg" to "مصر",
            "us" to "United States",
            "gb" to "United Kingdom",
            "fr" to "France",
            "de" to "Deutschland",
            "sa" to "السعودية",
            "it" to "Italia",
            "es" to "España",
            "jp" to "日本"
        )

        val countryNames = countryMap.values.toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerRegion.adapter = adapter

        val sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val savedRegion = sharedPref.getString("region", "eg")

        // Restore saved selection
        val savedCountryName = countryMap[savedRegion]
        val position = countryNames.indexOf(savedCountryName)
        if (position >= 0) {
            binding.spinnerRegion.setSelection(position)
        }

        var isFirstSelection = true
        binding.spinnerRegion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long,
            ) {
                if (isFirstSelection) {
                    isFirstSelection = false
                    return
                }
                val selectedCountryName = countryNames[position]
                val region = countryMap.entries.firstOrNull { it.value == selectedCountryName }?.key
                sharedPref.edit { putString("region", region) }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        binding.logout.setOnClickListener {
            logout()
            finishAffinity()
            startActivity(Intent(this, LoginActivity::class.java))
        }


        binding.favourites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))

        }
    }
}
