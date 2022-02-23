package com.evosouza.news.data.model

data class InterestNews(
    val news: NewsResponse,
    val subject: HeaderTitle
)

data class HeaderTitle(
    val title: String
): SubjectAdapterModel

interface SubjectAdapterModel