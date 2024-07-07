package com.heartsteel.heartory.ui.heart_rate.result

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.healthcarecomp.base.BaseViewModel
import com.google.gson.Gson
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.HBRecord
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import com.heartsteel.heartory.service.model.response.ResponseObject
import com.heartsteel.heartory.service.model.response.StreamingRes
import com.heartsteel.heartory.service.repository.HBRecordRepository
import com.heartsteel.heartory.service.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    override val userRepository: UserRepository,
    val hBRecordRepository: HBRecordRepository,
    val hasInternetConnection: () -> Boolean,
) : BaseViewModel(
    userRepository
) {
    val createState = MutableLiveData<Resource<ResponseObject<HBRecord>>>()
    val diagnosisResult = MutableLiveData<Resource<StreamingRes>>()

     fun getAge(dateOfBirth: String): Int {
         try {
             val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
             val birthDate = LocalDate.parse(dateOfBirth, formatter)
             val currentDate = LocalDate.now()
             Log.d("ResultViewModel", "getAge: $birthDate, $currentDate")
             return Period.between(birthDate, currentDate).years
         } catch (e: Exception) {
             e.printStackTrace()
             return 0
         }

    }
    fun getUserFromSharePref() = userRepository.getUserFromSharePref()
    fun createHBRecord(hbRecord: HBRecord) {
        if (hasInternetConnection()) {
            viewModelScope.launch {
                createState.postValue(Resource.Loading())
                try {
                    val response = hBRecordRepository.createRecord(hbRecord)
                    createState.postValue(Resource.Success(response.body()!!))
                } catch (e: Exception) {
                    createState.postValue(Resource.Error(e.message ?: "An error occurred"))
                }
            }
        } else {
            createState.postValue(Resource.Error("No internet connection"))
        }
    }

//    fun getDiagnoses(diagnosesReq: DiagnosesReq) {
//        viewModelScope.launch(Dispatchers.IO) {
//            diagnosisResult.postValue(Resource.Loading())
//            val call = hBRecordRepository.getDiagnoses(diagnosesReq)
//            call.enqueue(object : Callback<ResponseBody> {
//                override fun onResponse(
//                    call: Call<ResponseBody>,
//                    response: Response<ResponseBody>
//                ) {
//                    Log.d("ResultViewModel", "response.body: ${response.body()}")
//                    if (response.isSuccessful) {
//                        val input = response.body()?.byteStream()?.bufferedReader()
//                        try {
//                            var line = input?.readLine()
//                            while (line != null) {
//                                if (line.startsWith("data:")) {
//                                    try {
//                                        val streamingRes = Gson().fromJson(
//                                            line.substring(5).trim(),
//                                            StreamingRes::class.java
//                                        )
//                                        Log.d("ResultViewModel", "streamingRes: $streamingRes")
//                                        diagnosisResult.postValue(Resource.Success(streamingRes))
//                                    } catch (e: Exception) {
//                                        e.printStackTrace()
//                                    }
//                                }
//                                line = input?.readLine()
//                            }
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        } finally {
//                            input?.close()
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                    // Handle failure
//                }
//            })
//        }
//    }

    fun getDiagnoses(diagnosesReq: DiagnosesReq) = flow<StreamingRes> {
        val response = hBRecordRepository.getDiagnoses(diagnosesReq).execute()

        if (response.isSuccessful) {

            val input = response.body()?.byteStream()?.bufferedReader() ?: throw Exception()
            try {
                while (currentCoroutineContext().isActive) {
                    val line = input.readLine()
                    if (line != null && line.startsWith("data:")) {
                        try {
                            val streamingRes = Gson().fromJson(
                                line.substring(5).trim(),
                                StreamingRes::class.java
                            )
                            emit(streamingRes)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    delay(100)
                }
            } catch (e: IOException) {
                throw Exception(e)
            } finally {
                input.close()
            }
        } else {
            throw HttpException(response)
        }
    }

//    fun getDiagnoses(diagnosesReq: DiagnosesReq) {
//        if (hasInternetConnection()) {
//            viewModelScope.launch(Dispatchers.IO) {
//                diagnosisResult.postValue(Resource.Loading())
//                Log.d("ResultViewModel", "getDiagnoses: $diagnosesReq")
//                val response = hBRecordRepository.getDiagnoses(diagnosesReq).execute()
//                if (response.isSuccessful) {
//                    Log.d("ResultViewModel", "getDiagnoses: ${response.body()}")
//                    val input = response.body()?.byteStream()?.bufferedReader()
//                    try {
//                        var line = input?.readLine()
//                        while (line != null && line.startsWith("data:")) {
//                            val streamingRes =
//                                Gson().fromJson(line.substring(5).trim(), StreamingRes::class.java)
//                            Log.d("ResultViewModel", "getDiagnoses: $streamingRes")
//                            diagnosisResult.postValue(Resource.Success(diagnosisResult.value?.data + streamingRes.message?.content))
//                            delay(100)
//                        }
//                    } catch (e: IOException) {
//                        Log.e("ResultViewModel", "getDiagnoses: ${e.message}")
//                        diagnosisResult.postValue(Resource.Error(e.message ?: "An error occurred"))
//                    } finally {
//                        input?.close()
//                    }
//                } else {
//                    Log.d("ResultViewModel", "getDiagnoses: ${response.errorBody()}")
//                    diagnosisResult.postValue(
//                        Resource.Error(
//                            response.errorBody()?.string() ?: "An error occurred"
//                        )
//                    )
//                }
//            }
//        } else {
//            diagnosisResult.postValue(Resource.Error("No internet connection"))
//        }
//    }

}

