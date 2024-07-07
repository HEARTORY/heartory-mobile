package com.heartsteel.heartory.service.api

import com.heartsteel.heartory.service.model.domain.HBRecord
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

interface HBRecordAPI {

    @GET("record")
    suspend fun getRecords(): List<HBRecord>

    @Streaming
    @POST("record/diagnoses/streaming")
    fun getDiagnoses(@Body diagnosesReq: DiagnosesReq): Call<ResponseBody>

    @GET("record/{id}")
    suspend fun getRecordById(@Path("id") id: Int): HBRecord

    @POST("record")
    suspend fun createRecord(@Body record: HBRecord): HBRecord

    @PATCH("record/{id}")
    suspend fun updateRecord(@Path("id") id: Int, @Body record: HBRecord): HBRecord

    @DELETE("record/{id}")
    suspend fun deleteRecord(@Path("id") id: Int): HBRecord
}