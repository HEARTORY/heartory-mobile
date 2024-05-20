package com.heartsteel.heartory.ui.auth

import android.app.Fragment
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.data.model.FirebaseRegisterReq
import com.heartsteel.heartory.data.model.LoginReq
import com.heartsteel.heartory.data.model.RegisterReq
import com.heartsteel.heartory.data.model.domain.User
import com.heartsteel.heartory.databinding.ActivityAuthBinding
import com.heartsteel.heartory.ui.MainActivity
import com.heartsteel.heartory.ui.auth.login.LoginFragment
import com.heartsteel.heartory.ui.auth.register.RegisterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private lateinit var _binding: ActivityAuthBinding
    val viewModel: AuthViewModel by viewModels()

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
        setupObserver()
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
                lifecycleScope.launchWhenStarted {
                    viewModel.authRepository.isEmailIsExist(it).let {
                        if (it.isSuccessful) {
                            it.body()?.data?.let {
                                if (it) {
                                    handleGoogleLogin(loginUser)
                                } else {
                                    handleGoogleRegister(loginUser)
                                }
                            }
                        } else {
                            Log.e(TAG, "Could not check if email exists.")
                        }
                    }
                }
            }
        }
    }

    private fun handleGoogleLogin(loginUser: FirebaseUser) {
        var email = ""
        var uid = ""
        loginUser.email?.let {
            email = it
        }
        loginUser.uid.let {
            uid = it
        }
        var user = LoginReq(
            email = email,
            password = uid,
        )
        lifecycleScope.launchWhenStarted {
            viewModel.login(user)
        }
        Log.d("handleGoogleLogin", "user: $user")
    }

    private fun handleGoogleRegister(loginUser: FirebaseUser) {
        var firstName = ""
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
        var user = RegisterReq(
            firstName = firstName,
            lastName = lastName,
            email = email,
            password = uid,
            avatar = photoUrl,
            phone = phoneNumber,
            role_id = RoleEnum.USER.value,
        )
        lifecycleScope.launchWhenStarted {
            viewModel.registerWithGoogle(user)
        }
        Log.d("handleGoogleRegister", "user: $user")
    }

    private fun setupObserver() {
        viewModel.loginState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    Toast.makeText(this, "Log in success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toast.makeText(
                        this,
                        "Login failed" + it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        viewModel.registerState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show()
                    this.navigateToLogin()
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.registerWithGoogleState.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    Toast.makeText(this, "Register with Google success", Toast.LENGTH_SHORT).show()
                }

                is Resource.Error -> {
                    hideLoading2()
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
