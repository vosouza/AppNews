package com.evosouza.news.ui.home.favorites.viewmodel

import androidx.lifecycle.*
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.sharedpreference.DataStorage
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {

    private val _delete = MutableLiveData<Int>()
    val delete: LiveData<Int>
        get() = _delete

    fun getAllArticles(id: Long) = repository.getAllArticles(id)

    fun deleteArticle(article: Article, position: Int) = viewModelScope.launch {
        try {
            repository.delete(article)
            _delete.value = position
        }catch (e : Exception){
            _delete.value = DELETE_ERROR
        }
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

    companion object{
        const val DELETE_ERROR = -1
    }
}