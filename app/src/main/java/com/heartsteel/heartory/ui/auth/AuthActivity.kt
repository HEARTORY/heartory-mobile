package com.heartsteel.heartory.ui.auth

import android.app.Fragment
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heartsteel.heartory.common.constant.RoleEnum
import com.heartsteel.heartory.data.model.FirebaseRegisterReq
import com.heartsteel.heartory.data.model.User
import com.heartsteel.heartory.databinding.ActivityAuthBinding
import com.heartsteel.heartory.ui.MainActivity
import com.heartsteel.heartory.ui.auth.login.LoginFragment
import com.heartsteel.heartory.ui.auth.register.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private lateinit var _binding: ActivityAuthBinding
    private val _viewModel: AuthViewModel by viewModels()

    private lateinit var loginFragment: LoginFragment
    private lateinit var resigterFragment: RegisterFragment

    lateinit var oneTapClient: SignInClient
    lateinit var signInRequest: BeginSignInRequest
    lateinit var auth: FirebaseAuth
    lateinit var googleLoginListener: ((User?) -> Unit)

    companion object {
        const val REQ_ONE_TAP = 1
        const val TAG = "Auth activity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupView()
    }

    private fun setupView() {
        loginFragment = LoginFragment.newInstance()
        resigterFragment = RegisterFragment.newInstance()
        navigateToLogin()
        setupLoginWithGoogle()
    }

    fun navigateToRegister() {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.m3_side_sheet_enter_from_right,
            com.google.android.material.R.anim.m3_side_sheet_exit_to_right,
        ).replace(_binding.fcvAuth.id, resigterFragment).commit()
    }

    fun navigateToLogin() {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.m3_side_sheet_enter_from_left,
            com.google.android.material.R.anim.m3_side_sheet_exit_to_left,
        ).replace(_binding.fcvAuth.id, loginFragment).commit()
    }


    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
    }

    private fun setupLoginWithGoogle() {
        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("686710245051-sa9488g9v4as8mbodigsh4cvjjt2guc3.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        auth = Firebase.auth
    }

    fun loginWithGoogle() {
//        googleLoginListener = listener
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this) { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender, REQ_ONE_TAP,
                        null, 0, 0, 0
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            }.addOnFailureListener(this) { e ->
                e.printStackTrace()
                Log.e(TAG, e.localizedMessage)
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    when {
                        idToken != null -> {
                            val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                            auth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful)
                                        handleGoogleAuthSuccess()
                                    else {
                                        Log.e(TAG, "Could not sign in with Google.")
                                    }
                                    Log.d(TAG, "Got ID token.")
                                }
                        }

                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token!")
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleGoogleAuthSuccess() {
        val loginUser = auth.currentUser
        var user: FirebaseRegisterReq? = null

        loginUser?.let {
            loginUser.email?.let {
                if(true) {
                    handleGoogleLogin(loginUser)
                } else {
                    handleGoogleRegister(loginUser)
                }
            }

            Intent(this, MainActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun handleGoogleLogin(loginUser: FirebaseUser){

    }

    private fun handleGoogleRegister(loginUser: FirebaseUser){
        var firstName = ""
        var secondName = ""
        var lastName = ""
        var email = ""
        var photoUrl = ""
        var phoneNumber: String? = null
        var uid = ""
        loginUser.displayName?.let {
            var nameList = it.split(" ")
            firstName = nameList[0]
            if (nameList.size > 1) {
                lastName = nameList.last()
                for (i in 1 until nameList.size - 1) {
                    secondName += nameList[i] + " "
                }
                secondName = secondName.trim()
            }
        }
        loginUser.email?.let {
            email = it
        }
        loginUser.photoUrl?.let {
            photoUrl = it.toString()
        }
        loginUser.phoneNumber?.let {
            phoneNumber = it
        }
        loginUser.uid.let {
            uid = it
        }
        var user = FirebaseRegisterReq(
            firstName = firstName,
            secondName = secondName,
            lastName = lastName,
            email = email,
            avatar = photoUrl,
            phone = phoneNumber,
            role_id = RoleEnum.USER.value,
        )
        Log.d("AuthActivity", "user: $user")
    }
}
