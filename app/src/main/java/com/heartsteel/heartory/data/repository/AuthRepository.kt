package com.heartsteel.heartory.data.repository

import com.heartsteel.heartory.data.api.RetrofitInstance
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.RegisterReq

class AuthRepository {

    suspend fun login(loginReq: LoginReq) = RetrofitInstance.authApi.login(loginReq)

    suspend fun register(registerReq: RegisterReq) = RetrofitInstance.authApi.register(registerReq)

}