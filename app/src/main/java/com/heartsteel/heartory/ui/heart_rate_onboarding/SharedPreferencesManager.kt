package com.heartsteel.heartory.ui.heart_rate_onboarding

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.heartsteel.heartory.R


class SharedPreferencesManager(context:Context) {
    private val preferences = context.getSharedPreferences(
        context.getString(R.string.app_name), AppCompatActivity.MODE_PRIVATE
    )

    private val editor = preferences.edit()

    private val keyIsFirstTime = "isFirstTime"

    var isFirstTime
        get() = preferences.getBoolean(keyIsFirstTime, true)
        set(value) {
            editor.putBoolean(keyIsFirstTime, value)
            editor.commit()
        }

}