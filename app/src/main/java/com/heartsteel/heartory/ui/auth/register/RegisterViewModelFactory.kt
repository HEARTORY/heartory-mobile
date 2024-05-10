package com.heartsteel.heartory.ui.auth.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.heartsteel.heartory.data.repository.AuthRepository
import com.heartsteel.heartory.ui.auth.AuthViewModel

class RegisterViewModelFactory(
    val app: Application,
    val authRepository: AuthRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterViewModel(app, authRepository) as T
    }
}