package com.heartsteel.heartory.ui.profile

import androidx.lifecycle.ViewModel
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    override val authRepository: AuthRepository
) : BaseViewModel(authRepository) {

}