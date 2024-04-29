package com.heartsteel.heartory.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.healthcarecomp.base.BaseActivity
import com.heartsteel.heartory.R
import com.heartsteel.heartory.common.helper.heartbeat.HeartbeatActivity

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvent();
    }
    private fun setupEvent() {
        val btnHeartbeat = findViewById<Button>(R.id.btn_heartbeat)
        btnHeartbeat.setOnClickListener {
            val intent = Intent(this, HeartbeatActivity::class.java)
            startActivity(intent)
        }
    }
}