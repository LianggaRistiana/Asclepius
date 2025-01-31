package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.data.remote.response.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String,
        @Query("apiKey") apiKey: String
    ): Response
//
//    @GET("events/{id}")
//    suspend fun getEventById(@Path("id") id: Int): Response

}