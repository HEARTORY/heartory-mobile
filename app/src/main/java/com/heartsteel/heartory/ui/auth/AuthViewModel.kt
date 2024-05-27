package com.heartsteel.heartory.ui.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.google.gson.Gson
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.model.request.ForgotPasswordReq
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.service.model.response.LoginRes
import com.heartsteel.heartory.service.model.request.RegisterReq
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.IsEmailExistReq
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    override val userRepository: UserRepository,
    private val hasInternetConnection: () -> Boolean,
    private val jwtRepository: JwtRepository
) : BaseViewModel(userRepository) {

    val loginState = MutableLiveData<Resource<ResponseObject<LoginRes>?>>()

    val registerState = MutableLiveData<Resource<ResponseObject<Int>?>>()

    val registerWithGoogleState = MutableLiveData<Resource<ResponseObject<Int>?>>()

    val forgotPasswordState = MutableLiveData<Resource<ResponseObject<Int>?>>()
    suspend fun login(loginReq: LoginReq) {
        loginState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                userRepository.login(loginReq).let {
                    Log.d("AuthViewModel", "login: ${it.body()}")
                    if (it.isSuccessful) {
                        val user: User? = it.body()?.data?.user
                        val accessToken = it.body()?.data?.accessToken
                        val refreshToken = it.body()?.data?.token

                        if (user != null && accessToken != null && refreshToken != null) {
                            jwtRepository.saveAccessJwt(accessToken)
                            jwtRepository.saveRefreshJwt(refreshToken)
                            userRepository.saveUserToSharePref(it.body()?.data?.user!!)
                            loginState.postValue(Resource.Success(it.body()))
                        } else {
                            loginState.postValue(Resource.Error("User or credentials not found"))
                        }
                    } else {
                        val errorResponseObject =
                            Gson().fromJson(it.errorBody()?.string(), ResponseObject::class.java)
                        loginState.postValue(Resource.Error(errorResponseObject.message))
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
                userRepository.register(registerReq).let {
                    if (it.isSuccessful) {
                        registerState.postValue(Resource.Success(it.body()))
                    } else {
                        val errorResponseObject =
                            Gson().fromJson(it.errorBody()?.string(), ResponseObject::class.java)
                        registerState.postValue(Resource.Error(errorResponseObject.message))
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
                userRepository.register(registerReq).let {
                    if (it.isSuccessful) {
                        registerWithGoogleState.postValue(Resource.Success(it.body()))
                        login(LoginReq(registerReq.email, registerReq.password))
                    } else {
                        val errorResponseObject =
                            Gson().fromJson(it.errorBody()?.string(), ResponseObject::class.java)
                        registerWithGoogleState.postValue(Resource.Error(errorResponseObject.message))
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

    fun forgotPassword(email: String) {
        forgotPasswordState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                viewModelScope.launch {
                    userRepository.isEmailIsExist(IsEmailExistReq(email)).let {
                        if (it.isSuccessful) {
                            if (it.body()?.data == true) {
                                userRepository.forgotPassword(ForgotPasswordReq(email)).let {
                                    if (it.isSuccessful) {
                                        forgotPasswordState.postValue(
                                            Resource.Success(
                                                it.body(),
                                                it.body()?.message
                                            )
                                        )
                                    } else {
                                        val errorResponseObject: ResponseObject<*>? = Gson().fromJson(
                                            it.errorBody()?.string(),
                                            ResponseObject::class.java
                                        )
                                        forgotPasswordState.postValue(
                                            Resource.Error(
                                                errorResponseObject?.message ?: "An error occurred"
                                            )
                                        )
                                    }
                                }
                            } else {
                                forgotPasswordState.postValue(Resource.Error("Email not found"))
                            }
                        } else {
                            val errorResponseObject = Gson().fromJson(
                                it.errorBody()?.string(),
                                ResponseObject::class.java
                            )
                            forgotPasswordState.postValue(Resource.Error(errorResponseObject.message))
                        }
                    }

                }
            } else {
                forgotPasswordState.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            forgotPasswordState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }

}