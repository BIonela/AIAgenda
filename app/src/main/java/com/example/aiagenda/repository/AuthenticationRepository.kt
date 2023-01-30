package com.example.aiagenda.repository

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
    val forgotPasswordStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()

    fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    registerStatus.postValue(AuthenticationStatus.SUCCESS)
                } else {
                    registerStatus.postValue(AuthenticationStatus.ERROR)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        registerStatus.postValue(AuthenticationStatus.USER_EXISTS)
                    } catch (e: FirebaseNetworkException) {
                        registerStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                    } catch (e: Exception) {
                        registerStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("EROARE", "SIGN UP NOT COMPLETED")
            }
    }

    fun login(email: String, password: String) {
        Log.e("LOGIN","INLOGIN")
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            Log.e("LOGIN","TASK")

            if (task.isSuccessful) {
                loginStatus.postValue(AuthenticationStatus.SUCCESS)
                Log.e("LOGIN","INLOGINSUCCES")

            } else {
                Log.e("LOGIN","INLOGINELSE")

                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidUserException) {
                    loginStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD)
                    Log.e("LOGIN", "PAROLA INCORECTA")
                } catch (e: FirebaseNetworkException) {
                    loginStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: Exception) {
                    loginStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                forgotPasswordStatus.postValue(AuthenticationStatus.SUCCESS)
                Log.e("SUCCESS", "Email sent")

            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseNetworkException) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: FirebaseAuthInvalidUserException) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: Exception) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                }
            }
        }.addOnFailureListener {
            Log.e("Error", "Email reset failed")
        }
    }
}