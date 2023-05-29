package com.example.aiagenda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun getPosts(user: User) {
        _uiState.postValue(UiStatus.LOADING)
        repository.getPosts(user, { posts ->
            _posts.postValue(posts)
        }, { uiStatus ->
            _uiState.postValue(uiStatus)
        })
    }

}