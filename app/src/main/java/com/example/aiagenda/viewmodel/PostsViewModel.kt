package com.example.aiagenda.viewmodel

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aiagenda.model.Post
import com.example.aiagenda.model.PostBody
import com.example.aiagenda.model.User
import com.example.aiagenda.repository.PostsRepository
import com.example.aiagenda.util.UiStatus

class PostsViewModel(private val repository: PostsRepository) : ViewModel() {

    private val _posts: MutableLiveData<PostBody> =
        MutableLiveData<PostBody>()
    val posts: LiveData<PostBody>
        get() = _posts

    private val _uiState: MutableLiveData<UiStatus> =
        MutableLiveData<UiStatus>()
    val uiState: LiveData<UiStatus>
        get() = _uiState

    private var _title = MutableLiveData<String>()

    private var _type = MutableLiveData<String>()

    private var _date = MutableLiveData<String>()

    private var _hour = MutableLiveData<String>()

    private var _isEnabled: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>().apply {
        addSource(_title) { value = setIsEnabled() }
        addSource(_type) { value = setIsEnabled() }
        addSource(_date) { value = setIsEnabled() }
        addSource(_hour) { value = setIsEnabled() }
    }
    val isEnabled: LiveData<Boolean>
        get() = _isEnabled


    fun setTitle(title: Editable) {
        _title.postValue(title.toString())
    }

    fun setType(type: Editable) {
        _type.postValue(type.toString())
    }

    fun setDate(date: Editable) {
        _date.postValue(date.toString())
    }

    fun setHour(hour: Editable) {
        _hour.postValue(hour.toString())
    }

    private fun isValid(field: String?): Boolean {
        return !field.isNullOrEmpty()
    }

    private fun setIsEnabled(): Boolean {
        return (isValid(_title.value) && isValid(_type.value) && isValid(_date.value)
                && isValid(_hour.value))
    }

    fun getPosts(user: User) {
        _uiState.postValue(UiStatus.LOADING)
        repository.getPosts(user, { posts ->
            _posts.postValue(posts)
        }, { uiStatus ->
            _uiState.postValue(uiStatus)
        })
    }

    fun addPost(user: User, post: Post) {
        _uiState.postValue(UiStatus.LOADING)
        repository.addPost(user, post) { uiStatus ->
            _uiState.postValue(uiStatus)
        }
    }

}