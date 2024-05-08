package com.heartsteel.heartory.ui.auth

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

class AuthActivity : BaseActivity() {

    private lateinit var _binding: ActivityAuthBinding
    private val _viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(_binding.root)
        setupView()
    }

    private fun setupView() {
        val LoginFragment = LoginFragment.newInstance()
        val ResigterFragment = RegisterFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(_binding.fcvAuth.id, LoginFragment).commit()
    }


}