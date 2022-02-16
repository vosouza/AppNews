package com.evosouza.news.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context): DataStorage {

    companion object{
        private const val CACHENAME = "NEWSAPPCACHE"
        const val EMAIL = "EMAIL"
        const val INTERESTS = "INTERESTS"
    }

    private val sharedPref : SharedPreferences = context.getSharedPreferences(CACHENAME, Context.MODE_PRIVATE)

    override fun getData(key: String): String? =
        sharedPref.getString(key, "")

    override fun saveData(key: String, values: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, values)
        editor.apply()
    }

    override fun saveStringSet(key: String, data: Set<String>) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putStringSet(key, data)
        editor.apply()
    }

    override fun getStringSet(key: String): Set<String>
    = sharedPref.getStringSet(key, setOf()) as Set<String>


    override fun deleteData(key: String) {
        sharedPref.edit().remove(key).apply()
    }
}