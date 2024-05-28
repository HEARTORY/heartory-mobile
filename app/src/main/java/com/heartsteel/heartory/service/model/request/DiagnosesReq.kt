package com.heartsteel.heartory.service.model.request

data class DiagnosesReq(
    var hr: Int,
    var hrv: Int,
    var numCycles: Int,
    var botId: String,
    var query: String
) {
}