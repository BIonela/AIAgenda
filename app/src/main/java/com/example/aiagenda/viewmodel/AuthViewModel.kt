package com.example.aiagenda.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.model.User
import com.example.aiagenda.repository.AuthenticationRepository
import com.example.aiagenda.util.ValidationError

class AuthViewModel(val app: Application, val repository: AuthenticationRepository) :
    AndroidViewModel(app) {

    //mutablelivedata password error

    private val _registerError = MutableLiveData<ValidationError>()
    val registerError: LiveData<ValidationError> = _registerError

    private val _loginError = MutableLiveData<ValidationError>()
    val loginError: LiveData<ValidationError> = _loginError

    private val _forgotPasswordError = MutableLiveData<ValidationError>()
    val forgotPasswordError: LiveData<ValidationError> = _forgotPasswordError

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        year: String,
        user: User
    ) {
        _registerError.postValue(ValidationError.LOADING)
        if (validateRegister(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                year = year
            )
        ) {
            repository.signUp(email, password, user)
        }
    }

    fun login(email: String, password: String, isChecked: Boolean) {
        _loginError.postValue(ValidationError.LOADING)
        if (validateLogin(email, password)) {
            repository.login(email, password, isChecked)
        }
    }

    private fun validateRegister(
        email: String,
        password: String,
        lastName: String,
        firstName: String,
        year: String
    ): Boolean {
        if (lastName.isEmpty()) {
            _registerError.postValue(ValidationError.LAST_NAME_IS_EMPTY)
            return false
        }
        if (firstName.isEmpty()) {
            _registerError.postValue(ValidationError.FIRST_NAME_IS_EMPTY)
            return false
        }
        if (email.isEmpty()) {
            _registerError.postValue(ValidationError.EMAIL_IS_EMPTY)
            return false
        }
        if (email.split("@").last() != "student.utcb.ro") {
            _registerError.postValue(ValidationError.EMAIL_NOT_VALID)
            return false
        }
        if (password.isEmpty()) {
            _registerError.postValue(ValidationError.PASSWORD_IS_EMPTY)
            return false
        }
        if (password.length < 6) {
            _registerError.postValue(ValidationError.PASSWORD_SHORT)
            return false
        }
        if (year == "Nicio selectie") {
            _registerError.postValue(ValidationError.YEAR_NOT_SELECTED)
            return false
        }
        return true
    }

    //check if empty, if user exists, if password wrong, if email is not student.utcb.ro
    private fun validateLogin(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            _loginError.postValue(ValidationError.EMAIL_IS_EMPTY)
            return false
        }
        if (email.split("@").last() != "student.utcb.ro") {
            _loginError.postValue(ValidationError.EMAIL_NOT_VALID)
            return false
        }
        if (password.isEmpty()) {
            _loginError.postValue(ValidationError.PASSWORD_IS_EMPTY)
            return false
        }
        if (password.length < 6) {
            _loginError.postValue(ValidationError.PASSWORD_SHORT)
            return false
        }
        return true
    }

    fun forgotPassword(email: String) {
        _forgotPasswordError.postValue(ValidationError.LOADING)
        if (validateForgotPassword(email)) {
            repository.forgotPassword(email)
        }
    }

    private fun validateForgotPassword(email: String): Boolean {
        if (email.isEmpty()) {
            _forgotPasswordError.postValue(ValidationError.EMAIL_IS_EMPTY)
            return false
        }
        if (email.split("@").last() != "student.utcb.ro") {
            _forgotPasswordError.postValue(ValidationError.EMAIL_NOT_VALID)
            return false
        }
        return true
    }

    fun logout(result: () -> Unit) {
        repository.logout(result)
    }

    fun getSession(result: (User?) -> Unit) {
        repository.getSession(result)
    }

}