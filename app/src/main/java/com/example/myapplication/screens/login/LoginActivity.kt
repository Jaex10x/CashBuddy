package com.example.myapplication.screens.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.R
import com.example.myapplication.app.CustomApp
import com.example.myapplication.screens.register.RegisterActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.getEditTextValue
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.requireText
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.toast

class LoginActivity : Activity(), LoginContract.View {
        private lateinit var loginPresenter: LoginPresenter
        override fun onCreate(savedInstanceState: Bundle?) {
            applyCurrentTheme()
            installSplashScreen()
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)
            loginPresenter = LoginPresenter(this, LoginModel(application as CustomApp))

            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            if (sharedPref.getBoolean("isLoggedIn", false)) {
                navigateTo(DashboardActivity::class.java)
            }

            setOnClick(R.id.btnLogin, View.OnClickListener {
                loginPresenter.login(
                    getEditTextValue(R.id.etEmail),
                    getEditTextValue(R.id.etPassword),
                    sharedPref.getString("username", "") ?: "",
                    sharedPref.getString("password", "") ?: ""
                )
            })
            setOnClick(R.id.tvRegister, View.OnClickListener {
                navigateTo(RegisterActivity::class.java)
            })
        }

        override fun showInvalidCredentialsMessage() {
            toast("Invalid credentials")
        }

        override fun showEmptyMessage() {
            toast("Fields cannot be empty")
        }

        override fun onLoginSuccess() {
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            sharedPref.edit().putBoolean("isLoggedIn", true).apply()
            toast("Login Successful")
            navigateTo(DashboardActivity::class.java)
        }

        override fun onEmptyFields() {
            requireText(R.id.etEmail, "Enter Email")
            requireText(R.id.etPassword, "Enter Password")
        }
    }