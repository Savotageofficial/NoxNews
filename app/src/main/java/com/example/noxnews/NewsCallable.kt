package com.example.noxnews

import android.R
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsCallable {

    @GET("/v2/top-headlines&apiKey=e145d0ed4d974df59b294a451d4d4e96&pageSize=60")
    fun getNews(@Query("country") countryID : String?, @Query("category") categoryID : String?): Call<News>


}