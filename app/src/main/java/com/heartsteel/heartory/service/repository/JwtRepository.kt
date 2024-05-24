package com.heartsteel.heartory.service.repository

import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import javax.inject.Inject

class JwtRepository @Inject constructor(
    private val appSharePreference: AppSharePreference
) {
    companion object {
        val ACCESS_JWT_KEY = "access_jwt"
        val REFRESH_JWT_KEY = "refresh_jwt"
    }

    fun saveAccessJwt(token: String) {
        appSharePreference.putString(ACCESS_JWT_KEY, token)
    }

    fun saveRefreshJwt(token: String) {
        appSharePreference.putString(REFRESH_JWT_KEY, token)
    }

    fun getAccessJwt(): String? =
        appSharePreference.getString(ACCESS_JWT_KEY, "").ifBlank { null }

    fun getRefreshJwt(): String? {
        return appSharePreference.getString(REFRESH_JWT_KEY, "").ifBlank { null }
    }

    fun clearAllTokens() {
        appSharePreference.remove(ACCESS_JWT_KEY)
        appSharePreference.remove(REFRESH_JWT_KEY)
    }

}