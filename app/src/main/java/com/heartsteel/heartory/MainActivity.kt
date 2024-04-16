package com.heartsteel.heartory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.heartsteel.heartory.activity.LoginActivity
import com.heartsteel.heartory.common.helper.heartbeat.HeartbeatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setEvent();
    }
    private fun setEvent() {
        val btnHeartbeat = findViewById<Button>(R.id.btn_heartbeat)
        btnHeartbeat.setOnClickListener {
            val intent = Intent(this, HeartbeatActivity::class.java)
            startActivity(intent)
        }
    }
}