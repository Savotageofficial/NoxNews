package com.example.noxnews

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.noxnews.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.activity.addCallback


class MainActivity : AppCompatActivity() {


    //https://newsapi.org/v2/top-headlines?country=us&category=general&apiKey=e145d0ed4d974df59b294a451d4d4e96&pageSize=30
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // youssef added country shared pref
        val sharedPref = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val receivedCategory = intent.getStringExtra("catID")
        val receivedCountry = sharedPref.getString("region", "eg") ?: "eg"

        binding.genreTv.text = receivedCategory

        Log.d("trace" , receivedCategory!!)
        Log.d("trace" , receivedCountry)

        loadNews(conID = receivedCountry , catID = receivedCategory)

        binding.swipeRefresh.setOnRefreshListener { loadNews(conID = receivedCountry , catID = receivedCategory) }



        onBackPressedDispatcher.addCallback(this ) {
            // Back is pressed... Finishing the activity
            finish()
        }
        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun loadNews(conID : String , catID : String){
        val retro = Retrofit
            .Builder()
            .baseUrl("https://newsapi.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val c = retro.create(NewsCallable::class.java)



        c.getNews(countryID = conID  , categoryID = catID).enqueue(object : Callback<News>{
            override fun onResponse(
                call: Call<News?>,
                response: Response<News?>
            ) {
                val news = response.body()
                val articles = news?.articles ?: arrayListOf()

                articles.removeAll{
                    it.title == "[Removed]"
                }
//                Log.d("trace" , "Articles: $articles")
                showNews(articles)
                binding.progress.isVisible = false
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onFailure(
                call: Call<News?>,
                t: Throwable
            ) {
                Log.d("trace" , "Error: ${t.message}")
                binding.progress.isVisible = false
            }
        })
    }
    private fun showNews(articles: ArrayList<Article>){
        val adapter = NewsAdapter(this , articles)
        binding.newsList.adapter = adapter

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