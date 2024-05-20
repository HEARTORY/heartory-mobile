package com.heartsteel.heartory.data.model

data class FirebaseRegisterReq(
    var firstName: String,
    var secondName: String? = null,
    var lastName: String,
    var email: String,
    var phone: String?,
    var role_id: Int,
    var avatar: String? = null
) {

}