package com.heartsteel.heartory.service.model.domain

data class HBRecord(
    val user: User? = null,
    val hr: Int? = null,
    val hrv: Int? = null,
    val deviceId: String? = null,
    val numCycles: Int? = null,
    val emotion_id: Int? = null,
    val activity_id: Int? = null
) {

}