package com.evosouza.news.ui.home.favorites.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.sharedpreference.DataStorage
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.launch
import timber.log.Timber

class FavoritesViewModel(
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {

    fun getAllArticles(id: Long) = repository.getAllArticles(id)

    fun deleteArticle(article: Article) = viewModelScope.launch {
        repository.delete(article)
    }

    fun getUserId() = cacheStorage.getIntegerData(SharedPreference.USERID)

    class FavoritesViewModelProviderFactory(
        private val cacheStorage: DataStorage,
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)){
                return FavoritesViewModel(cacheStorage, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }

}