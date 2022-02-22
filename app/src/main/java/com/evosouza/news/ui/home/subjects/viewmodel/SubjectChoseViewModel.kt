package com.evosouza.news.ui.home.subjects.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.evosouza.news.core.State
import com.evosouza.news.data.firebase.FirebaseDataSource
import com.evosouza.news.data.model.SubjectsModel
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.*
import timber.log.Timber
import java.lang.Exception

class SubjectChoseViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val dataBase: FirebaseDataSource,
    private val cache: SharedPreference
): ViewModel() {

    private var _subjectList =  MutableLiveData<State<SubjectsModel>>()
    val subjectList: MutableLiveData<State<SubjectsModel>> = _subjectList

    fun getSubjects() = viewModelScope.launch{
        _subjectList.value = State.loading(true)
        try {
            val list = withContext(ioDispatcher){
                dataBase.getValues()
            }
            _subjectList.value = State.success(list)
        }catch (e : Exception){
            _subjectList.value = State.error(e)
            Timber.e(e)
        }
    }

    fun saveInterestsList(list: SubjectsModel){
        cache.deleteData(SharedPreference.INTERESTS)
        cache.saveStringSet(SharedPreference.INTERESTS, list.subjects.toSet())
    }

    class SubjectChooseViewModelProviderFactory(
        private val ioDispatcher: CoroutineDispatcher,
        val dataBase: FirebaseDataSource,
        private val cache: SharedPreference
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SubjectChoseViewModel::class.java)){
                return SubjectChoseViewModel(ioDispatcher, dataBase, cache) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

}