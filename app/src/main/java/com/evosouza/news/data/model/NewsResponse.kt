package com.evosouza.news.data.model

import java.io.Serializable

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<Article>
): Serializable