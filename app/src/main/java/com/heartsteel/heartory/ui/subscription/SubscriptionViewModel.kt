package com.heartsteel.heartory.ui.subscription

import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.service.repository.UserRepository
import javax.inject.Inject

class SubscriptionViewModel @Inject constructor(
    override val userRepository: UserRepository,
) : BaseViewModel(userRepository) {

}