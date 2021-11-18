package com.evosouza.news.ui.login.register.passwordfragment.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.evosouza.news.R

class PasswordViewModel: ViewModel() {

    private var _passwordErrorResId = MutableLiveData<Int?>()
    val passwordErrorResId: LiveData<Int?>
        get() = _passwordErrorResId

    private var _confirmPasswordResId = MutableLiveData<Int?>()
    val confirmPasswordResId: LiveData<Int?>
        get() = _confirmPasswordResId

    private var _passwordValid = MutableLiveData<String>()
    val passwordValid: LiveData<String?>
        get() = _passwordValid


    private var isValid = false

    fun validatePassword( password: String, confirmPassword: String){
        isValid = true
        _passwordErrorResId.value = passwordEmptyResId(password)
        _confirmPasswordResId.value = confirmPasswordResId(password, confirmPassword)

        if(isValid){
            _passwordValid.value = password
        }
    }

    private fun confirmPasswordResId(password: String, confirmPassword: String): Int? =
        if(confirmPassword.isEmpty() || confirmPassword.isBlank()){
            isValid = false
            R.string.empty_password
        }else if(password != confirmPassword){
            isValid = false
            R.string.password_confirm_error
        }else null

    private fun passwordEmptyResId(password: String): Int? =
        if(password.isEmpty()||password.isBlank()){
            isValid = false
            R.string.empty_password
        }else null


    class PasswordViewModelProviderFactory(
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PasswordViewModel::class.java)) {
                return PasswordViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}
