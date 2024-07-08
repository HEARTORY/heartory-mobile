package com.heartsteel.heartory.ui.profile.edit

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
class ProfileEditViewModel @Inject constructor(
    override val userRepository: UserRepository
) : BaseViewModel(userRepository) {

    val userLiveData = MutableLiveData<Resource<User?>>()

    fun updateUser(user: User) {
        viewModelScope.launch {
            try {
                userLiveData.postValue(Resource.Loading())
                val response = userRepository.update(user)
                if (response.isSuccessful) {
                    userLiveData.postValue(Resource.Success(response.body()?.data))
                    response.body()?.data?.let { userRepository.saveUserToSharePref(it) }
                } else {
                    userLiveData.postValue(Resource.Error(response.message()))
                }
            } catch (e: Exception) {
                userLiveData.postValue(Resource.Error(e.message?: "An error occurred"))
            }
        }

    }

}