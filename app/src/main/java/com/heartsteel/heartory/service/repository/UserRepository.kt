package com.heartsteel.heartory.service.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.exception.JwtException
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.model.request.ForgotPasswordReq
import com.heartsteel.heartory.service.model.request.IsEmailExistReq
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.service.model.request.RefreshTokenReq
import com.heartsteel.heartory.service.model.request.RegisterReq
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class UserRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val appSharePreference: AppSharePreference,
    val privateRetrofit: PrivateRetrofit,
    val publicRetrofit: PublicRetrofit,
    val jwtRepository: JwtRepository
) {

    private final val USER_SHARE_PREF_KEY = "user"

    suspend fun fetchUser(): User? {
        getUserFromSharePref()?.id?.let {
            privateRetrofit.userApi.getUserById(it).let { response ->
                if (response.isSuccessful) {
                    val user: User? = response.body()?.data
                    user?.let {
                        Log.d("UserRepository", "User fetched and saved to sharePref")
                        saveUserToSharePref(it)
                        return it
                    }
                }
            }
        }
        return null
    }

    suspend fun login(loginReq: LoginReq) = publicRetrofit.authApi.login(loginReq)

    suspend fun register(registerReq: RegisterReq) = publicRetrofit.authApi.register(registerReq)

    suspend fun isEmailIsExist(isEmailExistReq: IsEmailExistReq) =
        publicRetrofit.authApi.isEmailIsExist(isEmailExistReq)

    fun saveUserToSharePref(user: User) {
        appSharePreference.putObject(USER_SHARE_PREF_KEY, user)
    }

    fun getUserFromSharePref(): User? {
        return appSharePreference.getObject(USER_SHARE_PREF_KEY, User::class.java)
    }

    fun isLoggedIn(): Boolean {
        try {
            runBlocking {
                refreshToken()
            }
            Log.d("UserRepository", "User is logged in")
            return true
        } catch (e: JwtException) {
            Log.d("UserRepository", "User is not logged in ${e.message}")
            return false
        }
    }

    suspend fun refreshToken() {
        if (jwtRepository.getRefreshJwt().isNullOrEmpty())
            throw JwtException("Refresh token is null or empty")

        val response =
            publicRetrofit.authApi.refreshToken(RefreshTokenReq(jwtRepository.getRefreshJwt()!!))
        if (response.isSuccessful) {
            val token = response.body()?.data?.token
            val accessToken = response.body()?.data?.accessToken
            Log.d("UserRepository", "Body ${response.body()?.data}")
            if (token.isNullOrEmpty() || accessToken.isNullOrEmpty())
                throw JwtException("Token is null or empty")

            jwtRepository.saveAccessJwt(accessToken)
            jwtRepository.saveRefreshJwt(token)
        } else {
            throw JwtException("Refresh token failed")
        }
    }

    fun logout() {
        jwtRepository.clearAllTokens()
        appSharePreference.remove(USER_SHARE_PREF_KEY)
    }

    suspend fun forgotPassword(forgotPasswordReq: ForgotPasswordReq) =
        publicRetrofit.authApi.forgotPassword(forgotPasswordReq)


}