package com.evosouza.news.data.sharedpreference

interface DataStorage {

    fun getData(key: String): String?
    fun saveData(key: String, email: String)
    fun saveStringSet(key: String, data:  Set<String>)
    fun getStringSet(key: String): Set<String>
    fun deleteData(key: String)
    fun saveIntegerData(key: String, value: Long)
    fun getIntegerData(key: String): Long
}