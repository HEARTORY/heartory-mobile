package com.heartsteel.heartory.data.model

import com.heartsteel.heartory.data.model.domain.User

data class LoginRes(
    val accessToken: String,
    val token: String,
    val user: User
) {

}