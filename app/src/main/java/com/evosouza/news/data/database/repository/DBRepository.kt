package com.evosouza.news.data.database.repository

import androidx.lifecycle.LiveData
import com.evosouza.news.data.model.Article

interface DBRepository {

    suspend fun insert(article: Article)

    suspend fun delete(article: Article)

    fun getAllArticles(): LiveData<List<Article>>

}