package com.heartsteel.heartory.ui.auth.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.ResponseObject
import com.heartsteel.heartory.data.repository.AuthRepository
import com.heartsteel.heartory.ui.auth.register.RegisterViewModel

class LoginViewModelFactory(
    val app: Application,
    val authRepository: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(app, authRepository) as T
    }
}