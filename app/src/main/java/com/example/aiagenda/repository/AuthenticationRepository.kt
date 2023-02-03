package com.example.aiagenda.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.model.User
import com.example.aiagenda.util.AuthenticationStatus
import com.example.aiagenda.util.FireStoreCollection
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AuthenticationRepository(private val application: Application) {
    val firebaseUser: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLogged: MutableLiveData<Boolean> = MutableLiveData()

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseFirestore = FirebaseFirestore.getInstance()

    val registerStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    val loginStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()
    val forgotPasswordStatus: MutableLiveData<AuthenticationStatus> = MutableLiveData()

    fun signUp(email: String, password: String, user: User) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserInfo(user)
                    registerStatus.postValue(AuthenticationStatus.SUCCESS)
                } else {
                    registerStatus.postValue(AuthenticationStatus.ERROR)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        registerStatus.postValue(AuthenticationStatus.USER_EXISTS)
                    } catch (e: FirebaseNetworkException) {
                        registerStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        registerStatus.postValue(AuthenticationStatus.EMAIL_INVALID)
                    } catch (e: Exception) {
                        registerStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Error", "Sign up not completed")
            }
    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loginStatus.postValue(AuthenticationStatus.SUCCESS)
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD_OR_EMAIL_INVALID)
                } catch (e: FirebaseAuthInvalidUserException) {
                    loginStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD_OR_EMAIL_INVALID)
                } catch (e: FirebaseNetworkException) {
                    loginStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: FirebaseTooManyRequestsException) {
                    loginStatus.postValue(AuthenticationStatus.TOO_MANY_REQUESTS)
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
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseNetworkException) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: FirebaseAuthInvalidUserException) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.EMAIL_INVALID)
                } catch (e: Exception) {
                    forgotPasswordStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                }
            }
        }.addOnFailureListener {
            Log.e("Error", "Email reset failed")
        }
    }

    fun updateUserInfo(user: User) {
        val document = database.collection(FireStoreCollection.USER).document()
        user.id = document.id
        document
            .set(user)
            .addOnSuccessListener {
                Log.e("TAG", "SUCCES")
            }
            .addOnFailureListener {
                Log.e("TAG", "ERROR")
            }
    }
}