package com.heartsteel.heartory.service.model.domain

data class HBRecord(
    val id: Int? = null,
    val user: User? = null,
    val hr: Int? = null,
    val hrv: Int? = null,
    val deviceId: String? = null,
    val numCycles: Int? = null,
    val emotion: Int? = null,
    val activity: Int? = null
) {

}