package com.heartsteel.heartory.service.jwt


import android.util.Log
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.exception.JwtException
import com.heartsteel.heartory.service.model.request.RefreshTokenReq
import com.heartsteel.heartory.service.repository.JwtRepository
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import okio.EOFException
import javax.inject.Inject


class JwtTokenInterceptor @Inject constructor(
    private val tokenManager: JwtRepository,
    private val publicRetrofit: PublicRetrofit
) : Interceptor {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val TOKEN_TYPE = "Bearer"
    }

    private fun refreshToken(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            if (tokenManager.getRefreshJwt().isNullOrEmpty())
                throw JwtException("Refresh token in local storage not found!")
            val response = runBlocking {
                publicRetrofit.authApi.refreshToken(RefreshTokenReq(tokenManager.getRefreshJwt()!!))
            }
            return if (response.isSuccessful) {
                val token = response.body()?.data?.token
                val accessToken = response.body()?.data?.accessToken

                if (token.isNullOrEmpty() || accessToken.isNullOrEmpty())
                    throw JwtException("Token is null or empty")

                tokenManager.saveAccessJwt(accessToken)
                tokenManager.saveRefreshJwt(token)

                val newRequest = request
                    .newBuilder()
                    .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
                    .build()
                chain.proceed(newRequest)
            } else {
                chain.proceed(request)
            }
        } catch (e: Exception) {
            Log.e("JwtInterceptor", "refreshToken error: ${e.message.toString()}")
            throw JwtException(e.message.toString())
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val request = chain.request()
            val token = tokenManager.getAccessJwt()
            if (!token.isNullOrEmpty()) {
                val newRequest = request
                    .newBuilder()
                    .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
                    .build()
                val response = chain.proceed(newRequest)
                return if (response.code == 401 || response.code == 403) {
                    refreshToken(chain)
                } else {
                    response
                }
            } else {
                refreshToken(chain)
            }
            return chain.proceed(request)
        } catch (e: Exception) {
            Log.e("JwtInterceptor", "access token error: ${e.message.toString()}")
            if (e is JwtException) {
                throw JwtException(e.message)
            } else if (e is EOFException) {

            } else
                throw e
        }
        return chain.proceed(chain.request())
    }


}