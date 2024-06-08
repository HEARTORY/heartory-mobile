package com.heartsteel.heartory.ui.profile_onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.util.Resource
import com.heartsteel.heartory.service.model.domain.User
import com.heartsteel.heartory.ui.MainActivity
import com.heartsteel.heartory.ui.heart_rate.heartbeat.HeartRateActivity
import com.heartsteel.heartory.ui.profile.ProfileViewModel
import com.heartsteel.heartory.ui.profile.edit.ProfileEditActivity
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty

@AndroidEntryPoint
class CheckProfileActivity : BaseActivity() {
    private val checkProfileViewModel: CheckProfileViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkProfile()
    }

    //    override fun onStart() {
//        super.onStart()
//        checkProfile()
//    }
    private fun checkProfile() {
        checkProfileViewModel.fetchUser()

        // Observe userState here or in a method called later in the lifecycle
        checkProfileViewModel.userState.observe(this) { userState ->
            Log.d("CheckProfileActivity", "onCreate: ${userState.data}")
            // Perform actions based on userState here
            when (userState) {
                is Resource.Loading -> {
                    showLoading2()
                }

                is Resource.Success -> {
                    hideLoading2()
                    if(userState.data != null) {
                        checkEntities(userState.data)
                    }
                }

                is Resource.Error -> {
                    hideLoading2()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    startActivity(mainIntent)
                }
            }
        }
    }

    private fun checkEntities(user: User) {
        if (user.height == null || user.weight == null || user.dateOfBirth == null || user.gender == null) {
            val profileOnBoardingIntent = Intent(this, ProfileOnBoardingActivity::class.java)
            startActivity(profileOnBoardingIntent)
        }
    }
}