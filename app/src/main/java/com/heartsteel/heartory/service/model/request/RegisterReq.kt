package com.heartsteel.heartory.service.model.request

data class RegisterReq(
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var role_id: Int,
    val avatar: String? = null,
    val phone: String? = null,
) {

}