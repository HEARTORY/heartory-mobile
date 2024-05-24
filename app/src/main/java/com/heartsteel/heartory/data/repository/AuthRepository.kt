package com.heartsteel.heartory.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.heartsteel.heartory.data.api.RetrofitInstance
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.domain.User
import com.heartsteel.heartory.data.sharePreference.AppSharePreference
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    val appSharePreference: AppSharePreference
) {

    suspend fun login(loginReq: LoginReq) = RetrofitInstance.authApi.login(loginReq)

    suspend fun register(registerReq: RegisterReq) = RetrofitInstance.authApi.register(registerReq)

    suspend fun isEmailIsExist(email: String) = RetrofitInstance.authApi.isEmailIsExist(email)

    fun saveUser(user: User) {
        Log.d("AuthRepository", "User saved: ${user}")
        appSharePreference.putObject("user", user)
    }
    fun getUser(): User?{
        val user = appSharePreference.getObject("user", User::class.java)
        Log.d("AuthRepository", "User get: ${user}")
        return user
    }

    fun isLoggedIn(): Boolean {
        return appSharePreference.getObject("user", User::class.java) != null
    }

     fun logout() {
         Log.d("AuthRepository", "User removed")
        appSharePreference.putObject("user", null)
    }

    suspend fun loginWithGoogle(token: String) {
        firebaseAuth.signInWithCredential(GoogleAuthProvider.getCredential(token, null))
    }

    suspend fun loginWithFirebase(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    suspend fun registerWithFirebase(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }


}