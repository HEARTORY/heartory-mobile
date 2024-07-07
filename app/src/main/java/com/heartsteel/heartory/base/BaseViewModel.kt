package com.example.healthcarecomp.base

import androidx.lifecycle.ViewModel
import com.example.healthcarecomp.di.NetworkModule.hasInternetConnection
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.repository.UserRepository
import javax.inject.Inject
open class BaseViewModel @Inject constructor(
    open val userRepository: UserRepository
) : ViewModel() {
    val user: User?
        get() = userRepository.getUserFromSharePref()

    fun logout() = userRepository.logout()
}