package com.heartsteel.heartory.service.model.response

import com.heartsteel.heartory.service.model.domain.User

data class LoginRes(
    val accessToken: String,
    val token: String,
    val user: User
) {

}