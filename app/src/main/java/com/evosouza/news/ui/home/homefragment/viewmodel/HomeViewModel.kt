package com.evosouza.news.ui.home.homefragment.viewmodel

import androidx.lifecycle.*
import com.evosouza.news.core.State
import com.evosouza.news.data.model.HeaderTitle
import com.evosouza.news.data.model.InterestNews
import com.evosouza.news.data.model.NewsResponse
import com.evosouza.news.data.repository.NewsRepository
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: NewsRepository,
    private val cache: SharedPreference,
) : ViewModel() {

    private val _newsListOfInterests = MutableLiveData<State<List<InterestNews>>>()
    val newsListOfInterests: LiveData<State<List<InterestNews>>>
        get() = _newsListOfInterests

    private val _interests = MutableLiveData<State<List<String>>>()
    val interests: LiveData<State<List<String>>>
        get() = _interests

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

    fun getSubjects() = viewModelScope.launch {
        try {
            _interests.value = State.loading(true)
            val response = withContext(ioDispatcher){
                cache.getStringSet(SharedPreference.INTERESTS)
            }

            val list = mutableListOf<String>()
            response.forEach {
                list.add(it)
            }

            _interests.value = State.success(list)
        } catch (throwable: Throwable){
            _interests.value = State.error(throwable)
        }
    }

    fun getListOfInterest(apiKey: String) {
        val deferredList = ArrayList<Deferred<*>>()
        _newsListOfInterests.value = State.loading(true)
        try{
            viewModelScope.launch {
                _interests.value?.data?.forEach{ subject ->
                    deferredList.add(
                        async{
                            repository.getNewsBySubject(subject, apiKey).run {
                                InterestNews(this, HeaderTitle(subject))
                            }
                        }
                    )
                }

                val response = deferredList.awaitAll() as List<InterestNews>
                _newsListOfInterests.value = State.success(response)
            }

        }catch (e : Exception){
            Timber.e(e)
            _newsListOfInterests.value = State.error(e)
        }
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