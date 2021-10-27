package com.evosouza.news.ui.search.viewmodel

import androidx.lifecycle.*
import com.evosouza.news.core.State
import com.evosouza.news.data.model.NewsResponse
import com.evosouza.news.data.repository.NewsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.lang.IllegalArgumentException

class SearchViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: NewsRepository
): ViewModel() {

    private val _response = MutableLiveData<State<NewsResponse>>()
    val response: LiveData<State<NewsResponse>>
        get() = _response

    fun searchNews(query: String, page: Int, apiKey: String) = viewModelScope.launch {
        try {
            _response.value = State.loading(true)
            val response = withContext(ioDispatcher){
                repository.searchNews(query,page,apiKey)
            }
            _response.value = State.success(response)
        }catch (e: Throwable){
            _response.value = State.error(e)
        }
    }

    class SearchViewModelProviderFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val repository: NewsRepository
    ): ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SearchViewModel::class.java)){
                return SearchViewModel(ioDispatcher, repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

}