package com.evosouza.news.data.firebase

import com.evosouza.news.data.model.SubjectsModel

interface FirebaseDataSource {
    suspend fun getValues(): SubjectsModel
}