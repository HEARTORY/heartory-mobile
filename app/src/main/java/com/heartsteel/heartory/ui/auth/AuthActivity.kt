package com.heartsteel.heartory.ui.auth

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthcarecomp.base.BaseActivity
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.heartsteel.heartory.R
import com.heartsteel.heartory.data.model.User
import com.heartsteel.heartory.databinding.ActivityAuthBinding
import com.heartsteel.heartory.databinding.ActivityMainBinding
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

    @SuppressLint("PrivateResource")
    fun navigateToRegister(){
        supportFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.m3_side_sheet_enter_from_right,
            com.google.android.material.R.anim.m3_side_sheet_exit_to_right,
        ).replace(_binding.fcvAuth.id, resigterFragment).commit()
    }

    fun navigateToLogin(){
        supportFragmentManager.beginTransaction().setCustomAnimations(
            com.google.android.material.R.anim.m3_side_sheet_enter_from_left,
            com.google.android.material.R.anim.m3_side_sheet_exit_to_left,
        ).replace(_binding.fcvAuth.id, loginFragment).commit()
    }


    override fun onAttachFragment(fragment: Fragment?) {
        super.onAttachFragment(fragment)
    }

    private fun setupLoginWithGoogle(){
        oneTapClient = Identity.getSignInClient(this)

        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId("686710245051-sa9488g9v4as8mbodigsh4cvjjt2guc3.apps.googleusercontent.com")
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(true)
                    .build())
            .build()
        auth = Firebase.auth

    }

    fun loginWithGoogle(listener: (User?) -> Unit) {
        googleLoginListener = listener
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
                            // Got an ID token from Google. Use it to authenticate
                            // with Firebase.
                            Log.d(TAG, "Got ID token.")
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
}