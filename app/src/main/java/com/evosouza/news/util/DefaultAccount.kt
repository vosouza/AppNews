package com.evosouza.news.util

import android.content.Context
import com.evosouza.news.data.database.NewsDB
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.database.repository.UserRepositoryImpl
import com.evosouza.news.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class DefaultAccount {

    fun createAccount(context: Context) {
        val userDB: UserRepository = UserRepositoryImpl(NewsDB(context))
        User(
            "asad@asd.com",
            "asd",
            "asad",
            null
        ).also { user ->
            CoroutineScope(Dispatchers.Unconfined).launch{
                userDB.insert(user)
            }
        }

    }


}