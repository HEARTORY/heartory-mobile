package com.heartsteel.heartory.slot2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.heartsteel.heartory.R

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneOrEmail: String = findViewById<EditText>(R.id.etPhoneOrEmail).text.toString()
        val password: String = findViewById<EditText>(R.id.etPass).text.toString()
        val loginButton = findViewById<Button>(R.id.btnLogin2).setOnClickListener{
            login(phoneOrEmail, password)
            Toast.makeText(this,"Login successful", Toast.LENGTH_SHORT).show()
        }
    }
    fun login(phoneOrEmail: String, password: String) {

    }

}