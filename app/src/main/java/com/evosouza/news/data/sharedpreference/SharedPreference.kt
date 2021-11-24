package com.evosouza.news.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(context: Context): DataStorage {

    private val EMAIL = "EMAIL"

    private val sharedPref : SharedPreferences = context.getSharedPreferences(EMAIL, Context.MODE_PRIVATE)

    override fun getData(key: String): String? =
        sharedPref.getString(key, "")

    override fun saveData(userName: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(EMAIL, userName)
        editor.apply()
    }
}