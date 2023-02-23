package com.example.aiagenda.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.aiagenda.util.UserDataStatus

class UiStateViewModel : ViewModel() {

    private val _uiState = MutableLiveData<UserDataStatus>()
    val uiState: LiveData<UserDataStatus> = _uiState

    fun setLoading() {
        _uiState.postValue(UserDataStatus.LOADING)
    }

    fun setSuccess() {
        _uiState.postValue(UserDataStatus.SUCCESS)
    }

    fun setError() {
        _uiState.postValue(UserDataStatus.ERROR)
    }


}