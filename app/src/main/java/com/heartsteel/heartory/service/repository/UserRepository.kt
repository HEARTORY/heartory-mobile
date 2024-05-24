package com.heartsteel.heartory.service.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.service.api.retrofit.PrivateRetrofit
import com.heartsteel.heartory.service.api.retrofit.PublicRetrofit
import com.heartsteel.heartory.service.model.request.ForgotPasswordReq
import com.heartsteel.heartory.service.model.request.LoginReq
import com.heartsteel.heartory.service.model.request.RegisterReq
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.service.sharePreference.AppSharePreference
import javax.inject.Inject

class UserRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val appSharePreference: AppSharePreference,
    val privateRetrofit: PrivateRetrofit,
    val publicRetrofit: PublicRetrofit
) {

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

    suspend fun isEmailIsExist(email: String) = publicRetrofit.authApi.isEmailIsExist(email)

    fun saveUserToSharePref(user: User) = appSharePreference.saveUser(user)
    fun getUserFromSharePref(): User? = appSharePreference.getUser()
    fun isLoggedIn(): Boolean = appSharePreference.isLoggedIn()

    fun logout() {
        Log.d("UserRepository", "User removed")
        appSharePreference.putObject("user", null)
    }

    suspend fun forgotPassword(forgotPasswordReq: ForgotPasswordReq) =
        publicRetrofit.authApi.forgotPassword(forgotPasswordReq)



}