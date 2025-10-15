package com.example.noxnews

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noxnews.databinding.ActivityFavoritesBinding
import com.example.noxnews.databinding.ActivityMainBinding

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        adapter = FavoritesAdapter(emptyList()) { news ->
            FavoritesRepository.removeFavorite(news) { success ->
                if (success) {
                    // Refresh list after removing
                    loadFavorites()
                }
            }
        }

        binding.favouritesList.layoutManager = LinearLayoutManager(this)
        binding.favouritesList.adapter = adapter

        // Load data
        loadFavorites()

    }

    private fun loadFavorites() {
        FavoritesRepository.getFavorites { list ->
            binding.progress.visibility = View.GONE
            if (list.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.favouritesList.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.favouritesList.visibility = View.VISIBLE
                adapter.updateList(list)
            }
        }
    }


}
