package com.heartsteel.heartory.service.model.domain

data class HBRecord(
    val user: User? = null,
    val hr: Int? = null,
    val hrv: Int? = null,
    val deviceId: String? = null,
    val numCycles: Int? = null,
    val emotion: Emotion? = null,
    val activity: Activity? = null
): BaseEntity() {

}