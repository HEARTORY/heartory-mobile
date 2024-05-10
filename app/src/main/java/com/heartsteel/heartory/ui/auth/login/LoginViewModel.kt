package com.heartsteel.heartory.ui.auth.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.InternetUtil
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.LoginRes
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
import com.heartsteel.heartory.data.repository.AuthRepository

class LoginViewModel(
    val app: Application,
    val authRepository: AuthRepository
) : BaseViewModel(app) {

    val loginState = MutableLiveData<Resource<ResponseObject<LoginRes>?>>()
    suspend fun login(loginReq: LoginReq) {
        loginState.postValue(Resource.Loading())
        try {
            if (InternetUtil(this).hasInternetConnection()) {
                authRepository.login(loginReq).let {
                    if (it.isSuccessful) {
                        loginState.postValue(Resource.Success(it.body()))
                    } else {
                        loginState.postValue(Resource.Error(it.message()))
                    }
                }
            } else {
                loginState.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            loginState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }
}