package com.heartsteel.heartory.ui.heart_rate.heartbeat

import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.repository.UserRepository
import javax.inject.Inject

class HeartRateViewModel @Inject constructor(
    override val userRepository: UserRepository,
    private val hasInternetConnection: () -> Boolean,
    private val jwtRepository: JwtRepository
) : BaseViewModel(userRepository) {

}