package com.evosouza.news.ui.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.model.Article
import com.evosouza.news.ui.article.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: DBRepository
): ViewModel() {

    fun getAllArticles() = repository.getAllArticles()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }


    class FavoritesViewModelProviderFactory(
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
                return FavoritesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}