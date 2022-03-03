package com.evosouza.news.data.repository

import com.evosouza.news.data.model.NewsResponse
import com.evosouza.news.data.network.Service

class NewsRepositoryImpl(private val service: Service): NewsRepository {
    override suspend fun getBreakNews(country: String, page: Int, apiKey: String): NewsResponse =
        service.getBreakingNews(country, page, apiKey)

    override suspend fun searchNews(query: String, page: Int, apiKey: String): NewsResponse =
        service.searchNews(query, page, apiKey)

    override suspend fun getNewsBySubject(subject: String, apiKey: String): NewsResponse =
        service.searchNewsBySubject(subject, apiKey)
}