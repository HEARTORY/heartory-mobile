package com.heartsteel.heartory.ui.auth

import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
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

}