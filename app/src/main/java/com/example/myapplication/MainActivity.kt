package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnlogin = findViewById<Button>(R.id.btnLogin)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvRegister = findViewById<TextView>(R.id.tvRegister)

        btnlogin.setOnClickListener {
            val etEmail = etEmail.text.toString()
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("USERNAME", etEmail)
            startActivity(intent)
        }

        tvRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}