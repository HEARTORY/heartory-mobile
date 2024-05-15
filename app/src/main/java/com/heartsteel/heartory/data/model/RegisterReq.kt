package com.heartsteel.heartory.data.model

data class RegisterReq(
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var role_id: Int
) {

}