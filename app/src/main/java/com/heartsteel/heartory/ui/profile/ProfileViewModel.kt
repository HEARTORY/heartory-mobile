package com.heartsteel.heartory.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    override val userRepository: UserRepository
) : BaseViewModel(userRepository) {

    val userState = MutableLiveData<Resource<User>>()

    fun fetchUser() {
        userState.postValue(Resource.Loading())
        viewModelScope.launch {
            try {
                userRepository.fetchUser().let { user ->
                    if (user != null) {
                        userState.postValue(Resource.Success(user))
                    } else {
                        userState.postValue(Resource.Error("User not found"))
                    }
                }
            } catch (t: Throwable) {
                userState.postValue(Resource.Error(t.message.toString()))
            }
        }
    }

}