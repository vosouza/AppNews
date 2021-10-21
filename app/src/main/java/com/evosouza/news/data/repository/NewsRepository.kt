package com.evosouza.news.data.repository

import com.evosouza.news.data.model.NewsResponse

interface NewsRepository {
    suspend fun getBreakNews(country: String, page: Int, apiKey: String): NewsResponse
}