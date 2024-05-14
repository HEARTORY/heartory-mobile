package com.heartsteel.heartory.data.repository

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.heartsteel.heartory.data.api.RetrofitInstance
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.User
import retrofit2.Retrofit
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val firebaseAuth: FirebaseAuth
) {
    val currentUser: LiveData<User?>
        get() = getLoggedInUser()

    private fun getLoggedInUser(): LiveData<User?> {
        return TODO()
    }

    fun hasUser(): Boolean {
        return getLoggedInUser().value != null
    }

    suspend fun login(loginReq: LoginReq) = RetrofitInstance.authApi.login(loginReq)

    suspend fun register(registerReq: RegisterReq) = RetrofitInstance.authApi.register(registerReq)

    suspend fun loginWithFirebase(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
    }

    suspend fun registerWithFirebase(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
    }

    suspend fun logout() {
        firebaseAuth.signOut()
    }

}