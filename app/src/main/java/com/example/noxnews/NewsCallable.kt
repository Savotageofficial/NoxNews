package com.example.noxnews

import android.R
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsCallable {

    @GET("/v2/top-headlines?country={countryID}&category={categoryID}&apiKey=e145d0ed4d974df59b294a451d4d4e96&pageSize=60")
    fun getNews(@Path("countryID") countryID : String , @Path("categoryID") categoryID : String): Call<News>


}