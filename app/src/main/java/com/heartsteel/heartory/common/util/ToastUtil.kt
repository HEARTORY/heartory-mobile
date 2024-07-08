package com.heartsteel.heartory.common.util

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.heartsteel.heartory.R

object ToastUtil {
    fun showToast(context: Context, message: String?, colorResId: Int) {
        if (message.isNullOrEmpty()) return

        val inflater = LayoutInflater.from(context)
        val layout: View = inflater.inflate(R.layout.exercise_toast_custom, null)

        val textColor = when (colorResId) {
            R.color.red -> ContextCompat.getColor(context, R.color.white)
            R.color.yellow -> ContextCompat.getColor(context, R.color.black)
            R.color.green -> ContextCompat.getColor(context, R.color.white)
            else -> ContextCompat.getColor(context, R.color.white)
        }

        layout.findViewById<TextView>(R.id.toast_text).apply {
            text = message
            setBackgroundColor(ContextCompat.getColor(context, colorResId))
            setTextColor(textColor)
        }

        Toast(context).apply {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }
}