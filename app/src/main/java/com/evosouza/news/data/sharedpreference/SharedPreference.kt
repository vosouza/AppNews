package com.evosouza.news.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context): DataStorage {

    companion object{
        const val EMAIL = "EMAIL"
    }

    private val sharedPref : SharedPreferences = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE)

    override fun getData(key: String): String? =
        sharedPref.getString(key, "")

    override fun saveData(key: String, email: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(key, email)
        editor.apply()
    }

    override fun deleteData(key: String) {
        sharedPref.edit().remove(key).apply()
    }
}