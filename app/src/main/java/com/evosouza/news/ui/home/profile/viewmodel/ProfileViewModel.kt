package com.evosouza.news.ui.home.profile.viewmodel

import androidx.lifecycle.*
import com.evosouza.news.R
import com.evosouza.news.core.State
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User
import com.evosouza.news.data.sharedpreference.SharedPreference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Exception

class ProfileViewModel(
    private val ioDispatcher: CoroutineDispatcher,
    private val repository: UserRepository,
    private val cache: SharedPreference,
) : ViewModel() {

    private var _updateUserData = MutableLiveData<State<Boolean>>()
    val updateUserData: LiveData<State<Boolean>>
        get() = _updateUserData

    private var _userName = MutableLiveData<Int>()
    val userName: LiveData<Int>
        get() = _userName

    private var _userEmail = MutableLiveData<Int>()
    val userEmail: LiveData<Int>
        get() = _userEmail

    private var _lastPassword = MutableLiveData<Int>()
    val lastPassword: LiveData<Int>
        get() = _lastPassword

    private var _newPassword = MutableLiveData<Int>()
    val newPassword: LiveData<Int>
        get() = _newPassword

    private lateinit var user : LiveData<User>

    private var validForm = false

    fun getUserData(): LiveData<User> {
        val userId = getUserCachedId()
        user = repository.getUserById(userId)
        return user
    }

    private fun updateUserData(user: User) {
        viewModelScope.launch {
            _updateUserData.value = State.loading(true)
            delay(2000)
            try {
                withContext(ioDispatcher) {
                    user.apply {
                        repository.updateUser(
                            id,
                            userName,
                            email,
                            password,
                            photo ?: ""
                        )
                    }
                }
                _updateUserData.value = State.success(true)
            } catch (e: Exception) {
                Timber.e(e)
                _updateUserData.value = State.error(e)
            }
        }
    }

    private fun getUserCachedId() = cache.getIntegerData(SharedPreference.USERID)

    fun validateUserInput(
        userName: String,
        email: String,
        lastPassword: String,
        newPassword: String,
        imageUri: String?
    ) {
        validForm = true
        user.value?.let {
            _userName.value = validateUserName(userName)
            _userEmail.value = validateUserEmail(email, it)
            _lastPassword.value = validateUserLastPassword(lastPassword, it)
            _newPassword.value = validateUserNewPassword(newPassword, it)
            if (validForm){
                it.email = email
                it.userName = userName
                it.password = newPassword
                it.photo = imageUri
                updateUserData(it)
            }
        }
    }

    private fun validateUserNewPassword(newPassword: String, userData: User): Int {
        return if (newPassword.isEmpty() || newPassword.isBlank() && newPassword != userData.password) {
            validForm = false
             R.string.password_error
        } else 0
    }

    private fun validateUserLastPassword(lastPassword: String, userData: User ): Int {
        return if (lastPassword.isEmpty() || lastPassword.isBlank() && lastPassword != userData.password) {
            validForm = false
            R.string.password_error
        } else 0
    }

    private fun validateUserEmail(email: String, userData: User ): Int {
        return if (email.isEmpty() || email.isBlank() && email != userData.email) {
            validForm = false
            R.string.email_invalid
        } else 0
    }

    private fun validateUserName(userName: String): Int {
        return if (userName.isEmpty() || userName.isBlank()) {
            validForm = false
            R.string.user_name_invalid
        } else 0
    }

    class ProfileViewModelProviderFactory(
        private val ioDispatcher: CoroutineDispatcher,
        private val repository: UserRepository,
        private val cache: SharedPreference,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                return ProfileViewModel(ioDispatcher, repository, cache) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }

    }
}