package com.evosouza.news.ui.login.loginfragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evosouza.news.R
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User
import com.evosouza.news.data.sharedpreference.DataStorage
import com.evosouza.news.data.sharedpreference.SharedPreference

class LoginViewModel(
    private val db: UserRepository,
    private val cacheStorage: DataStorage
): ViewModel() {

    private val _userNameFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId : LiveData<Int?> = _userNameFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId : LiveData<Int?> = _passwordFieldErrorResId

    private val _userEmailSavedLogin = MutableLiveData<String>()
    val userEmailSavedLogin: LiveData<String> = _userEmailSavedLogin

    private var isValid: Boolean = false


    fun login(email: String, password: String): LiveData<User>?{
        isValid = true

        _userNameFieldErrorResId.value = getErrorStringResIdEmptyUserName(email)
        _passwordFieldErrorResId.value = getErrorStringResIdEmptyPassword(password)

        return if(isValid) db.getUser(email, password) else null

    }

    private fun getErrorStringResIdEmptyUserName(value: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            isValid = false
            R.string.invalid_user_name
        } else null

    private fun getErrorStringResIdEmptyPassword(value: String): Int? =
        if (value.isEmpty()){
            isValid = false
            R.string.empty_password
        } else null

//    fun insertUser(user: User) = viewModelScope.launch { db.insert(user) }

    fun getUserSavedEmail() {
        cacheStorage.getData(SharedPreference.EMAIL)?.let {
            _userEmailSavedLogin.value = it
        }
    }

    fun deleteUserEmailLogin() {
        if (!_userEmailSavedLogin.value.isNullOrEmpty()) cacheStorage.deleteData(SharedPreference.EMAIL)
    }

    fun saveUserEmailLogin(email: String) {
        if (email != _userEmailSavedLogin.value) cacheStorage.saveData(SharedPreference.EMAIL, email)
    }

    fun saveUserID(id: Long) {
        cacheStorage.saveIntegerData(SharedPreference.USERID, id)
    }

    class LoginViewModelProvider(
        private val repository: UserRepository,
        private val cacheStorage: DataStorage
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
                return LoginViewModel(repository, cacheStorage) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}