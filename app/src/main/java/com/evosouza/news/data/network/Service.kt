package com.evosouza.news.data.network

import com.evosouza.news.data.model.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface Service{

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String
    ): NewsResponse

}