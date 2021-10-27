package com.evosouza.news.data.database.repository

import androidx.lifecycle.LiveData
import com.evosouza.news.data.database.NewsDAO
import com.evosouza.news.data.model.Article

class DBRepositoryImpl(private val newsDAO: NewsDAO): DBRepository {
    override suspend fun insert(article: Article) = newsDAO.upsert(article)

    override suspend fun delete(article: Article) = newsDAO.deleteArticle(article)

    override fun getAllArticles(): LiveData<List<Article>> = newsDAO.getAllArticles()
}