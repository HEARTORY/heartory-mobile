package com.heartsteel.heartory.ui.auth.register

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.HeartoryApp
import com.heartsteel.heartory.common.util.InternetUtil
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
import com.heartsteel.heartory.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val authRepository: AuthRepository,
    val hasInternetConnection: ()-> Boolean
) : BaseViewModel() {

    val registerState = MutableLiveData<Resource<ResponseObject<Int>?>>()
    suspend fun register(RegisterRes: RegisterReq) {
        registerState.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                authRepository.register(RegisterRes).let {
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
            registerState.postValue(Resource.Error(t.message ?: "An error occurred"))
        }
    }

}