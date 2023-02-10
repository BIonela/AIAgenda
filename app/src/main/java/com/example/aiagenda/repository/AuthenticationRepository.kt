package com.example.aiagenda.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.aiagenda.model.User
import com.example.aiagenda.util.AuthenticationStatus
import com.example.aiagenda.util.FireStoreCollection
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore

class AuthenticationRepository(
    private val auth: FirebaseAuth,
    private val database: FirebaseFirestore,
    private val appPreferencesRepository: SharedPreferencesRepository
) {

    private val _registerStatus: MutableLiveData<AuthenticationStatus> =
        MutableLiveData<AuthenticationStatus>()
    val registerStatus: LiveData<AuthenticationStatus>
        get() = _registerStatus

    private val _loginStatus: MutableLiveData<AuthenticationStatus> =
        MutableLiveData<AuthenticationStatus>()
    val loginStatus: LiveData<AuthenticationStatus>
        get() = _loginStatus

    private val _forgotPasswordStatus: MutableLiveData<AuthenticationStatus> =
        MutableLiveData<AuthenticationStatus>()
    val forgotPasswordStatus: LiveData<AuthenticationStatus>
        get() = _forgotPasswordStatus

    fun signUp(email: String, password: String, user: User) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.id = task.result.user?.uid ?: ""
                    updateUserInfo(user)
                    _registerStatus.postValue(AuthenticationStatus.SUCCESS)
                } else {
                    _registerStatus.postValue(AuthenticationStatus.ERROR)
                    try {
                        throw task.exception!!
                    } catch (e: FirebaseAuthUserCollisionException) {
                        _registerStatus.postValue(AuthenticationStatus.USER_EXISTS)
                    } catch (e: FirebaseNetworkException) {
                        _registerStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        _registerStatus.postValue(AuthenticationStatus.EMAIL_INVALID)
                    } catch (e: Exception) {
                        _registerStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                    }
                }
            }
            .addOnFailureListener {
                Log.e("Error", "Sign up not completed")
            }
    }

    fun login(email: String, password: String, isChecked: Boolean) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                if (isChecked) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if (it == null) {
                            Log.e("SESSION", "FAILED")
                        } else {
                            Log.e("SESSION", "SUCCESS")
                        }
                    }
                }
                _loginStatus.postValue(AuthenticationStatus.SUCCESS)
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD_OR_EMAIL_INVALID)
                } catch (e: FirebaseAuthInvalidUserException) {
                    _loginStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _loginStatus.postValue(AuthenticationStatus.WRONG_PASSWORD_OR_EMAIL_INVALID)
                } catch (e: FirebaseNetworkException) {
                    _loginStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: FirebaseTooManyRequestsException) {
                    _loginStatus.postValue(AuthenticationStatus.TOO_MANY_REQUESTS)
                } catch (e: Exception) {
                    _loginStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                }
            }
        }
    }

    fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _forgotPasswordStatus.postValue(AuthenticationStatus.SUCCESS)
            } else {
                try {
                    throw task.exception!!
                } catch (e: FirebaseNetworkException) {
                    _forgotPasswordStatus.postValue(AuthenticationStatus.NO_INTERNET_CONNECTION)
                } catch (e: FirebaseAuthInvalidUserException) {
                    _forgotPasswordStatus.postValue(AuthenticationStatus.EMAIL_NOT_FOUND)
                } catch (e: FirebaseAuthInvalidCredentialsException) {
                    _forgotPasswordStatus.postValue(AuthenticationStatus.EMAIL_INVALID)
                } catch (e: Exception) {
                    _forgotPasswordStatus.postValue(AuthenticationStatus.ANOTHER_EXCEPTION)
                }
            }
        }.addOnFailureListener {
            Log.e("Error", "Email reset failed")
        }
    }

    private fun updateUserInfo(user: User) {
        val document = database.collection(FireStoreCollection.USER).document(user.id)
        document
            .set(user)
            .addOnSuccessListener {
                Log.e("TAG", "SUCCESS")
            }
            .addOnFailureListener {
                Log.e("TAG", "ERROR")
            }
    }

    fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferencesRepository.clear()
        result.invoke()
    }

    private fun storeSession(id: String, result: (User?) -> Unit) {
        database.collection(FireStoreCollection.USER).document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val user = it.result.toObject(User::class.java)
                    if (user != null) {
                        appPreferencesRepository.putString(user)
                    }
                    result.invoke(user)
                }
            }
            .addOnFailureListener {
                Log.e("TAG", "SESSION ERROR")
                result.invoke(null)
            }
    }

    fun getSession(result: (User?) -> Unit) {
        val userStr = appPreferencesRepository.getSession()
        if (userStr == null) {
            result.invoke(null)
        } else {
            val user = appPreferencesRepository.getUser(userStr)
            result.invoke(user)
        }
    }
}