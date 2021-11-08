package com.evosouza.news.ui.login.loginfragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.evosouza.news.R
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User

class LoginViewModel(val db: UserRepository): ViewModel() {

    private val _userNameFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId : LiveData<Int?> = _userNameFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId : LiveData<Int?> = _passwordFieldErrorResId

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

    private var isValid: Boolean = false


//    fun login(email: String, password: String): LiveData<User>{
//        isValid = true
//
//        _userNameFieldErrorResId.value = getErrorStringResIdEmptyUserName(email)
//        _passwordFieldErrorResId.value = getErrrorStringResIdEmpytPassword(password)
//
//
//    }

    private fun getErrorStringResIdEmptyUserName(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            isValid = false
            R.string.invalid_user_name
        } else null

    private fun getErrorStringResIdEmptyPassword(value: String): Int? =
        if (value.isNotEmpty()){
            isValid = false
            R.string.empty_password
        } else null
}