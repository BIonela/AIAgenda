package com.example.aiagenda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aiagenda.repository.ClassRepository
import com.example.aiagenda.model.Class
import com.example.aiagenda.model.User
import com.example.aiagenda.util.UiStatus

class ClassViewModel(private val repository: ClassRepository) : ViewModel() {

    private val _classes: MutableLiveData<List<Class>> =
        MutableLiveData<List<Class>>()
    val classes: LiveData<List<Class>>
        get() = _classes

    private val _uiState: MutableLiveData<UiStatus> =
        MutableLiveData<UiStatus>()
    val uiState: LiveData<UiStatus>
        get() = _uiState

    fun getClasses(user: User) {
        _uiState.postValue(UiStatus.LOADING)
        repository.getClasses(user, { classes ->
            _classes.postValue(classes)
        }, { uiStatus ->
            _uiState.postValue(uiStatus)
        })
    }
}