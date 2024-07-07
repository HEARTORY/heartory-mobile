package com.heartsteel.heartory.ui.profile_onboarding

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.SetProfileForOnBoardReq
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.JwtRepository
import com.heartsteel.heartory.service.repository.UserRepository
import com.heartsteel.heartory.ui.heart_rate_onboarding.SharedPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel  @Inject constructor(
    override val userRepository: UserRepository,
) : BaseViewModel(userRepository) {
    val setProfileState = MutableLiveData<Resource<ResponseObject<Int>?>>()
    val userState = MutableLiveData<Resource<User>>()

    suspend fun setProfile(gender:String, dateOfBirth: String, height: Double, weight: Double) {
        setProfileState.postValue(Resource.Loading())
        Log.d("ProfileViewModel", "setProfile: $gender, $dateOfBirth, $height, $weight")
        try {
            var user = userRepository.getUserFromSharePref()
            user?.weight = weight
            user?.height = height
            user?.gender = gender
            user?.dateOfBirth = dateOfBirth
            userState.postValue(Resource.Loading())
            userRepository.setProfileOnBoarding(user!!).let {user ->
                if (user != null) {
                    userState.postValue(Resource.Success(user))
                } else {
                    userState.postValue(Resource.Error("User not found"))
                }
            }
        } catch (e: Exception) {
            setProfileState.postValue(Resource.Error("Error setting profile"))
        }
    }
}