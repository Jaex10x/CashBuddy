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

        val backbutton = findViewById<ImageView>(R.id.backbutton)
        val btnEnterName = findViewById<EditText>(R.id.btnEnterName)
        val btnRegCreateNewProfile = findViewById<Button>(R.id.btnRegCreateNewProfile)

        backbutton.setOnClickListener {
            finish()
        }
        btnRegCreateNewProfile.setOnClickListener {
            val etEmail = btnEnterName.text.toString()
            val intent = Intent(this,DashboardActivity::class.java )
            intent.putExtra("USERNAME", etEmail)
            startActivity(intent)
        }
    }
}


