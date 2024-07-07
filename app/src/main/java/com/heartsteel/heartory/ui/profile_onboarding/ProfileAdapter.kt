package com.heartsteel.heartory.ui.profile_onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ProfileAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroFragment()
            1 -> SetDataFragment()
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}