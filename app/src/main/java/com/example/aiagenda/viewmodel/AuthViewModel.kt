package com.example.aiagenda.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.util.ValidationError
import com.google.android.material.textfield.TextInputLayout

class AuthViewModel(val app: Application, val repository: AuthenticationRepository) :
    AndroidViewModel(app) {

    //mutablelivedata password error

    private val _error = MutableLiveData<ValidationError>()
    val error: LiveData<ValidationError> = _error

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        year: String
    ) {

        if (validateIfNotEmpty(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                year = year
            )
        ) {
            repository.signUp(email, password)
        }
    }

    private fun validateIfNotEmpty(
        email: String,
        password: String,
        lastName: String,
        firstName: String,
        year: String
    ): Boolean {
        if (lastName.isEmpty()) {
            _error.postValue(ValidationError.LAST_NAME_IS_EMPTY)
            return false
        }
        if (firstName.isEmpty()) {
            _error.postValue(ValidationError.FIRST_NAME_IS_EMPTY)
            return false
        }
        if (email.isEmpty()) {
            _error.postValue(ValidationError.EMAIL_IS_EMPTY)
            return false
        }
        if (email.split("@").last() != "student.utcb.ro") {
            _error.postValue(ValidationError.EMAIL_NOT_VALID)
            return false
        }
        if (password.isEmpty()) {
            _error.postValue(ValidationError.PASSWORD_IS_EMPTY)
            return false
        }
        if (password.length < 6) {
            _error.postValue(ValidationError.PASSWORD_SHORT)
            return false
        }
        if (year == "Nicio selectie") {
            _error.postValue(ValidationError.YEAR_NOT_SELECTED)
            return false
        }
        return true
    }


}