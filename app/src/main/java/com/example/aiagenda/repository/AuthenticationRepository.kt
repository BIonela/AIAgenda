package com.example.aiagenda.repository

import android.R
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.util.AuthenticationStatus
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*


class AuthenticationRepository(private val application: Application) {
    val firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLogged: MutableLiveData<Boolean> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val registerStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    val loginStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerStatus.postValue(AuthenticationStatus.SUCCESS)
                } else {
                    // If sign in fails, display a message to the user.
                    //verificare erori
//                    Toast.makeText(
//                        application.applicationContext, "Inregistrare esuata. ${task.exception}",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    registerStatus.postValue(AuthenticationStatus.ERROR)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        registerStatus.postValue(AuthenticationStatus.USER_EXISTS)
                    } catch (e: FirebaseNetworkException) {
                        registerStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                    }
                }
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginStatus.postValue(AuthenticationStatus.SUCCESS)
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    loginStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD)
                    Log.e("LOGIN", "PAROLA INCORECTA")
                } catch (e: FirebaseNetworkException) {
                    loginStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                }
            }
        }

    }
}