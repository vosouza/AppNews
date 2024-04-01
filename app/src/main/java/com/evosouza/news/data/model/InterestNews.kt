package com.evosouza.news.data.model

data class InterestNews(
    val news: NewsResponse,
    val title: String,
    val isInterestNews: Boolean = false
)