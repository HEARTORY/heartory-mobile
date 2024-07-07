package com.heartsteel.heartory.service.repository

import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.model.domain.HBRecord
import com.heartsteel.heartory.service.model.request.DiagnosesReq
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import javax.inject.Inject

class HBRecordRepository @Inject constructor(
    val privateRetrofit: PrivateRetrofit,
) {
    suspend fun getRecords() = privateRetrofit.hBRecordAPI.getRecords()
    suspend fun getDiagnoses(diagnosesReq: DiagnosesReq) = privateRetrofit.hBRecordAPI.getDiagnoses(diagnosesReq)
    suspend fun getRecordById(id: Int) = privateRetrofit.hBRecordAPI.getRecordById(id)
    suspend fun createRecord(record: HBRecord) = privateRetrofit.hBRecordAPI.createRecord(record)
    suspend fun updateRecord(id: Int, record: HBRecord) = privateRetrofit.hBRecordAPI.updateRecord(id, record)
    suspend fun deleteRecord(id: Int) = privateRetrofit.hBRecordAPI.deleteRecord(id)
}