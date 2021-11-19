package com.evosouza.news.ui.login.register.photofragment.photoviewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.evosouza.news.core.State
import com.evosouza.news.data.database.repository.UserRepository
import com.evosouza.news.data.model.User
import kotlinx.coroutines.launch


class PhotoViewModel(
    private val userDB: UserRepository
): ViewModel() {

    private var _photo = MutableLiveData<State<Uri?>>()
    val photo : LiveData<State<Uri?>>
        get() = _photo

    fun setImageUri(uri: Uri?){
        if(uri == null){
            _photo.value = State.error(NoSuchFieldError())
        }else{
            _photo.value = State.success(uri)
        }

    }

    fun saveUser(user: User) = viewModelScope.launch{ userDB.insert(user)}

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