package com.evosouza.news.data.sharedpreference

interface DataStorage {

    fun getData(key: String): String?
    fun saveData(key: String, email: String)
    fun deleteData(key: String)

}