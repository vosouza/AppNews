package com.evosouza.news.ui.home.article.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.model.Article
import com.evosouza.news.data.sharedpreference.DataStorage
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class ArticleViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val cacheStorage: DataStorage,
    private val repository: DBRepository
): ViewModel() {

    fun saveArticle(article: Article) = viewModelScope.launch {
      try {
          val responseCache = withContext(ioDispatcher){
              article.url?.let {
                  repository.getArticleByURL(it)
              }
          }

          if (responseCache == null || responseCache == 0){
              repository.insert(article)
          }
      }catch (e: Exception){
          Timber.e(e)
      }
    }

    fun getUserId() = cacheStorage.getIntegerData(SharedPreference.USERID)

    class ArticleViewModelProviderFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val cacheStorage: DataStorage,
        private val repository: DBRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)){
                return ArticleViewModel(ioDispatcher, cacheStorage, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}