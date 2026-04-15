package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        if (sharedPref.getBoolean("isRegister", false)) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        val backbutton = findViewById<ImageView>(R.id.backbutton)
        val btnEnterName = findViewById<EditText>(R.id.btnEnterName)
        val btnRegCreateNewProfile = findViewById<Button>(R.id.btnRegCreateNewProfile)

        backbutton.setOnClickListener {
            finish()
        }
        btnRegCreateNewProfile.setOnClickListener {
            val username = btnEnterName.text.toString()

            if (username.isNotEmpty()) {

                sharedPref.edit()
                    .putString("username", username)
                    .putBoolean("isRegister", true)
                    .apply()

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                if (username.isEmpty()) btnEnterName.error = "Enter username"
            }

        }
    }
}


