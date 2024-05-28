package com.heartsteel.heartory.ui.heart_rate.result

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.HBRecord
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import com.heartsteel.heartory.service.repository.HBRecordRepository
import com.heartsteel.heartory.service.repository.UserRepository
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class ResultViewModel @Inject constructor(
    override val userRepository: UserRepository,
    val hBRecordRepository: HBRecordRepository,
    val hasInternetConnection: () -> Boolean,
) : BaseViewModel(
    userRepository
) {
    val createState = MutableLiveData<Resource<HBRecord>>()
    val diagnosisResult = MutableLiveData<Resource<String>>()

    fun createHBRecord(hbRecord: HBRecord) {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                createState.postValue(Resource.Loading())
                try {
                    val response = hBRecordRepository.createRecord(hbRecord)
                    createState.postValue(Resource.Success(response))
                } catch (e: Exception) {
                    createState.postValue(Resource.Error(e.message ?: "An error occurred"))
                }
            }
        } else {
            createState.postValue(Resource.Error("No internet connection"))
        }
    }

    fun getDiagnosis(diagnosesReq: DiagnosesReq) {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                val response = hBRecordRepository.getDiagnoses(diagnosesReq).execute()
                if (response.isSuccessful) {
                    val input = response.body()?.byteStream()?.bufferedReader() ?: throw Exception()
                    try {
                        while (isActive) {
                            val line = input.readLine() ?: continue
                            if (line.startsWith("data:")) {
                                try {
                                    val resultLine = line.substring(5)
                                    diagnosisResult.postValue(Resource.Success(resultLine))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("ResultViewModel", "getDiagnosis: ", e)
                        diagnosisResult.postValue(Resource.Error(e.message ?: "An error occurred"))
                    } finally {
                        input.close()
                    }
                } else {
                    diagnosisResult.postValue(Resource.Error("An error occurred"))
                }
            }
        } else {
            diagnosisResult.postValue(Resource.Error("No internet connection"))
        }
    }

}