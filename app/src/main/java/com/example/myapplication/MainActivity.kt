package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        if (sharedPref.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        val btnlogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)
        val etPassword = findViewById<EditText>(R.id.etPassword)

        btnlogin.setOnClickListener {

            val username = etEmail.text.toString()
            val password = etPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                sharedPref.edit()
                    .putString("username", username)
                    .putString("password", password) // ⚠️ basic demo only
                    .putBoolean("isLoggedIn", true)
                    .apply()

                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                if (username.isEmpty()) etEmail.error = "Enter username"
                if (password.isEmpty()) etPassword.error = "Enter password"
            }
        }

        tvRegister.setOnClickListener {

            val intent = Intent(this, RegisterActivity:: class.java)
            startActivity(intent)
            finish()
        }
    }
}