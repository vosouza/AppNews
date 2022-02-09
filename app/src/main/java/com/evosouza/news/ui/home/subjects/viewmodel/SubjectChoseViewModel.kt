package com.evosouza.news.ui.home.subjects.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evosouza.news.core.State
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import timber.log.Timber

class SubjectChoseViewModel(private val dataBase: DatabaseReference): ViewModel() {

    private var _subjectList =  MutableLiveData<State<List<String>>>()
    val subjectList: MutableLiveData<State<List<String>>> = _subjectList

    fun getSubjects(){
        _subjectList.value = State.loading(true)
        dataBase.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<String>()
                val hash = snapshot.value as HashMap<*, *>

                try{
                    (hash["subjects"] as ArrayList<String>).forEach {
                        list.add(it)
                    }
                }catch (e: Exception){
                    Timber.e(e)
                }
                _subjectList.value = State.success(list)
            }

            override fun onCancelled(error: DatabaseError) {
                _subjectList.value = State.error(IllegalStateException(error.message))
            }

        })
    }

    class SubjectChooseViewModelProviderFactory(val dataBase: DatabaseReference) : ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SubjectChoseViewModel::class.java)){
                return SubjectChoseViewModel(dataBase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

}