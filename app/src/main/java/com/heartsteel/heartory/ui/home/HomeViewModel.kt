package com.heartsteel.heartory.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.google.gson.Gson
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.response.HBRecordPageRes
import com.heartsteel.heartory.service.model.response.HBRecordRes
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.repository.HBRecordRepository
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    override val userRepository: UserRepository,
    private val HBRecordRepository: HBRecordRepository
) : BaseViewModel(userRepository) {

    val hBRecordsLiveData = MutableLiveData<Resource<List<HBRecordRes>>>()

    fun getHBRecords() {
        try {
            viewModelScope.launch {
                hBRecordsLiveData.postValue(Resource.Loading())
                val response: Response<ResponseObject<HBRecordPageRes>> =
                    HBRecordRepository.getRecords()
                if (response.isSuccessful) {
                    val hBRecords: List<HBRecordRes> = response.body()?.data?.content ?: emptyList()
                    hBRecordsLiveData.postValue(Resource.Success(hBRecords))
                } else {
                    val errorResponseObject =
                        Gson().fromJson(response.errorBody()?.string(), ResponseObject::class.java)
                    Log.e("HBRecordsLiveData:37 Error", errorResponseObject.message)
                }
            }
        } catch (e: Exception) {
            hBRecordsLiveData.postValue(Resource.Error(e.message.toString()))
        }
    }
}