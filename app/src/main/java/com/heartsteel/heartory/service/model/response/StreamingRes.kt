package com.heartsteel.heartory.service.model.response

data class StreamingRes(
    val event: String? = "",
    val message: Message? = null,
    val is_finish: Boolean? = false,
    val index: Int? = 0,
    val conversation_id: String? = "",
    val seq_id: Int? = 0
) {

}

data class Message(
    val role: String? = "",
    val type: String? = "",
    val content: String? = "",
    val content_type: String? = "",
) {

}