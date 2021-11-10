package com.evosouza.news.ui.login.loginfragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.*
import com.evosouza.news.R
import com.evosouza.news.data.database.repository.DBRepository
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User
import com.evosouza.news.ui.home.article.viewmodel.ArticleViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class LoginViewModel(private val db: UserRepository): ViewModel() {

    private val _userNameFieldErrorResId = MutableLiveData<Int?>()
    val loginFieldErrorResId : LiveData<Int?> = _userNameFieldErrorResId

    private val _passwordFieldErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId : LiveData<Int?> = _passwordFieldErrorResId

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean> = _loading

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


    fun insertUser(user: User) = viewModelScope.launch { db.insert(user) }


    class LoginViewModelProvider(
        private val repository: UserRepository
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
                return LoginViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}