package com.evosouza.news.ui.home.homefragment.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.evosouza.news.core.State
import com.evosouza.news.data.model.NewsResponse
import com.evosouza.news.data.repository.NewsRepository
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: NewsRepository,
    private val cache: SharedPreference,
) : ViewModel() {

    private val _response = MutableLiveData<State<NewsResponse>>()
    val response: LiveData<State<NewsResponse>>
        get() = _response

    fun getBreakNews(country: String, page: Int, apiKey: String) = viewModelScope.launch {
        try {
            _response.value = State.loading(true)
            val response = withContext(ioDispatcher) {
                repository.getBreakNews(country, page, apiKey)
            }
            _response.value = State.success(response)
        } catch (throwable: Throwable) {
            _response.value = State.error(throwable)
        }
    }

    fun getSubjects() {
        val subjects = cache.getData(SharedPreference.INTERESTS)
    }

    class HomeViewModelProviderFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val repository: NewsRepository,
        private val cache: SharedPreference,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                return HomeViewModel(ioDispatcher, repository, cache) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}