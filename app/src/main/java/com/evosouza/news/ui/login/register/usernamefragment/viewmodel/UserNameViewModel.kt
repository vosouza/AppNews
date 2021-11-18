package com.evosouza.news.ui.login.register.usernamefragment.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evosouza.news.R
import com.evosouza.news.data.model.User

class UserNameViewModel : ViewModel() {

    private val _userNameFieldError = MutableLiveData<Int>()
    val userNameFieldError: LiveData<Int>
        get() = _userNameFieldError

    private val _emailFieldError = MutableLiveData<Int>()
    val emailError: LiveData<Int>
        get() = _emailFieldError

    private val _confirmEmailFieldError = MutableLiveData<Int>()
    val confirmEmailError: LiveData<Int>
        get() = _confirmEmailFieldError

    private val _user = MutableLiveData<User>()
    val userData: LiveData<User>
        get() = _user

    private var isValid = false

    fun validateUserName(userName: String, email: String, confirmEmail: String) {
        isValid = true
        _userNameFieldError.value = getErrorStringUserName(userName)
        _emailFieldError.value = getErrorStringEmail(email)
        _confirmEmailFieldError.value = getErrorStringConfirmEmail(email, confirmEmail)

        if (isValid) {
            _user.value = User(email, userName, "", "")
        }
    }

    private fun getErrorStringUserName(userName: String): Int? =
        if (userName.isEmpty() || userName.isBlank()) {
            isValid = false
            R.string.user_name_invalid
        } else null

    private fun getErrorStringEmail(password: String): Int? =
        if (!Patterns.EMAIL_ADDRESS.matcher(password).matches()) {
            isValid = false
            R.string.email_invalid
        } else null

    private fun getErrorStringConfirmEmail(password: String, confirmPassword: String): Int? =
        when {
            confirmPassword.isEmpty() -> {
                isValid = false
                R.string.mandatory_field
            }
            password != confirmPassword -> {
                isValid = false
                R.string.email_mach
            }
            else -> null
        }

    class UserNameViewModelProviderFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserNameViewModel::class.java)) {
                return UserNameViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}