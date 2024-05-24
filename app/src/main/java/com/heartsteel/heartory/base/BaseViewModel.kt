package com.example.healthcarecomp.base

import androidx.lifecycle.ViewModel
import com.heartsteel.heartory.data.model.domain.User
import com.heartsteel.heartory.data.repository.AuthRepository
import javax.inject.Inject
open class BaseViewModel @Inject constructor(
    open val authRepository: AuthRepository
) : ViewModel() {
    val user: User?
        get() = authRepository.getUser()

    fun logout() = authRepository.logout()
}