package com.heartsteel.heartory.ui.profile_onboarding

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.ui.profile.ProfileViewModel

class CheckProfileActivity : BaseActivity() {
    private val profileViewModel: ProfileViewModel by viewModels()
    private val userState = profileViewModel.userState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        profileViewModel.fetchUser()

        Log.d("CheckProfileActivity", "onCreate: $userState")
    }

    private fun checkProfile(){

    }
}