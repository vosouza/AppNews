package com.evosouza.news.data.database.repository

import androidx.lifecycle.LiveData
import com.evosouza.news.data.model.User

interface UserRepository {

    suspend fun insert(user: User)

    fun getUser(email: String, password: String) : LiveData<User>

    fun getUserById(id: Long) : LiveData<User>

    suspend fun deleteUser(user: User)

    suspend fun updateUser(id: Long, userName: String, email: String, password: String, photo: String)

}