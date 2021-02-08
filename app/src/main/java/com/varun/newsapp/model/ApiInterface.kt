package com.varun.newsapp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

public interface ApiInterface {

    @GET("top-headlines")
    fun getnews(
        @Query("country") country:String,
        @Query("apiKey") apiKey:String


    ):Call<news>
}