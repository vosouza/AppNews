package com.evosouza.news.ui.home.subjects.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class SubjectChoseViewModel: ViewModel() {

    fun getSubjects() : ArrayList<String>{
        return arrayListOf("Business", "Health", "Entertainment", "General", "Sports", "Technology", "Science")
    }

    class SubjectChooseViewModelProviderFactory(): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SubjectChoseViewModel::class.java)){
                return SubjectChoseViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }

}