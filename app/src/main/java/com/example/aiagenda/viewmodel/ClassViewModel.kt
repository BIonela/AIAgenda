package com.example.aiagenda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aiagenda.model.Grade
import com.example.aiagenda.repository.ClassRepository
import com.example.aiagenda.model.SchoolClass
import com.example.aiagenda.model.User
import com.example.aiagenda.util.UiStatus

class ClassViewModel(private val repository: ClassRepository) : ViewModel() {

    private val _classes: MutableLiveData<List<SchoolClass>> =
        MutableLiveData<List<SchoolClass>>()
    val classes: LiveData<List<SchoolClass>>
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

    fun setGrade(user: User, grade: List<Grade>) {
        _uiState.postValue(UiStatus.LOADING)
        repository.setGrade(user, grade) {
            _uiState.postValue(it)
        }
    }
}