package com.evosouza.news.ui.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.model.Article
import kotlinx.coroutines.launch

class ArticleViewModel(
    private val repository: DBRepository
): ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch {
        repository.insert(article)
    }

    class ArticleViewModelProviderFactory(
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
                return ArticleViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}