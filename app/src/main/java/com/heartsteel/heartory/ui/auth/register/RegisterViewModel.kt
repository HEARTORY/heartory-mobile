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

class RegisterViewModel(
    app: Application,
    val authRepository: AuthRepository
) : BaseViewModel(app) {

    val registerState = MutableLiveData<Resource<ResponseObject<Int>?>>()
    suspend fun register(RegisterRes: RegisterReq) {
        registerState.postValue(Resource.Loading())
        try {
            if (InternetUtil(this).hasInternetConnection()) {
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

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<HeartoryApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}