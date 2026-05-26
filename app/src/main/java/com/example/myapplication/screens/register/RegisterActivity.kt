package com.example.myapplication.screens.register

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.app.CustomApp
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.getEditTextValue
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.requireText
import com.example.myapplication.utils.setOnClick

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registration_activity)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        setOnClick(R.id.backbutton, View.OnClickListener {
            navigateTo(LoginActivity::class.java)
        })
        setOnClick(R.id.btnRegCreateNewProfile, View.OnClickListener {
            val username = getEditTextValue(R.id.btnEnterName)
            val password = getEditTextValue(R.id.btnEnterPassword)

            if (username.isNotEmpty()) {
                sharedPref.edit()
                    .putString("username", username)
                    .putString("password", password)
                    .putBoolean("isRegister", true)
                    .putBoolean("isNewUser", true)
                    .apply()

                val app = application as CustomApp
                app.username = username
                app.password = password

                navigateTo(LoginActivity::class.java)
            } else {
                requireText(R.id.btnEnterName, "Enter Username")
            }
        })

    }
}