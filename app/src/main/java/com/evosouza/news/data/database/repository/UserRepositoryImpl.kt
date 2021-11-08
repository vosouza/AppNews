package com.evosouza.news.data.database.repository

import androidx.lifecycle.LiveData
import com.evosouza.news.data.database.dao.NewsDB
import com.evosouza.news.data.model.User

class UserRepositoryImpl(private val newsDB: NewsDB) : UserRepository {
    override suspend fun insert(user: User) = newsDB.userDAO().insertUser(user)

    override fun getUser(email: String, password: String): LiveData<User> = newsDB.userDAO().getUser(email, password)

    override fun getUserById(id: Long): LiveData<User> = newsDB.userDAO().getUserById(id)

    override suspend fun deleteUser(user: User) = newsDB.userDAO().deleteUser(user)

    override suspend fun updateUser(
        id: Long,
        userName: String,
        email: String,
        password: String,
        photo: String
    ) = newsDB.userDAO().updateUser(id, userName, email, password, photo)

}