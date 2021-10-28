package com.evosouza.news.data.database.repository

import androidx.lifecycle.LiveData
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.model.Article

class DBRepositoryImpl(private val newsDB: NewsDB): DBRepository {
    override suspend fun insert(article: Article) = newsDB.newsDao().upsert(article)

    override suspend fun delete(article: Article) = newsDB.newsDao().deleteArticle(article)

    override fun getAllArticles(): LiveData<List<Article>> = newsDB.newsDao().getAllArticles()
}