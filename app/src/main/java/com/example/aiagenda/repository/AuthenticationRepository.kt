package com.example.aiagenda.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.util.AuthenticationStatus
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class AuthenticationRepository(private val application: Application) {
    val firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLogged: MutableLiveData<Boolean> = MutableLiveData()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val status: MutableLiveData<AuthenticationStatus> = MutableLiveData()

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    status.postValue(AuthenticationStatus.SUCCESS)
                } else {
                    // If sign in fails, display a message to the user.
                    //verificare erori
//                    Toast.makeText(
//                        application.applicationContext, "Inregistrare esuata. ${task.exception}",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    status.postValue(AuthenticationStatus.ERROR)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        status.postValue(AuthenticationStatus.USER_EXISTS)
                    } catch (e: FirebaseNetworkException) {
                        status.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                    }
                }
            }
    }
}