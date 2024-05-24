package com.heartsteel.heartory.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.LoginRes
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
import com.heartsteel.heartory.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class AuthViewModel @Inject constructor(
    override val authRepository: AuthRepository,
    val hasInternetConnection: () -> Boolean
) : BaseViewModel(authRepository) {

    val loginState = MutableLiveData<Resource<ResponseObject<LoginRes>?>>()

    val registerState = MutableLiveData<Resource<ResponseObject<Int>?>>()

    val registerWithGoogleState = MutableLiveData<Resource<ResponseObject<Int>?>>()
    suspend fun login(loginReq: LoginReq) {
        loginState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                authRepository.login(loginReq).let {
                    if (it.isSuccessful) {
                        Log.d("TAG", "login: ${it.body()?.data?.user}")

                        if(it.body()?.data?.user != null) {
                            authRepository.saveUser(it.body()?.data?.user!!)
                            loginState.postValue(Resource.Success(it.body()))
                        }
                        else {
                            loginState.postValue(Resource.Error("User not found"))
                        }
                    } else {
                        loginState.postValue(Resource.Error(it.message()))
                    }
                }
            } else {
                loginState.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            loginState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }

    suspend fun register(registerReq: RegisterReq) {
        registerState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                authRepository.register(registerReq).let {
                    if (it.isSuccessful) {
                        registerState.postValue(Resource.Success(it.body()))
                    } else {
                        registerState.postValue(Resource.Error(it.message()))
                    }
                }
            } else {
                registerState.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            registerState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }

    suspend fun registerWithGoogle(registerReq: RegisterReq) {
        registerWithGoogleState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                authRepository.register(registerReq).let {
                    if (it.isSuccessful) {
                        registerWithGoogleState.postValue(Resource.Success(it.body()))
                        login(LoginReq(registerReq.email, registerReq.password))
                    } else {
                        registerWithGoogleState.postValue(Resource.Error(it.message()))
                    }
                }
            } else {
                registerWithGoogleState.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            registerWithGoogleState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }

}