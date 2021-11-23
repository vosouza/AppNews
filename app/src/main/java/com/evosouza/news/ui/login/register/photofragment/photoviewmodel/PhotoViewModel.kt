package com.evosouza.news.ui.login.register.photofragment.photoviewmodel

import androidx.lifecycle.*
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User
import kotlinx.coroutines.launch


class PhotoViewModel(
    private val userDB: UserRepository
) : ViewModel() {

    private val _saveResult = MutableLiveData<Boolean>()
    val saveResult: LiveData<Boolean>
        get() = _saveResult

    fun saveUser(user: User) = viewModelScope.launch {
        try {
            userDB.insert(user)
            _saveResult.value = true
        } catch (e: Exception) {
            _saveResult.value = false
        }
    }

    class PhotoViewModelProviderFactory(
        private val userDB: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PhotoViewModel::class.java)) {
                return PhotoViewModel(userDB) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}