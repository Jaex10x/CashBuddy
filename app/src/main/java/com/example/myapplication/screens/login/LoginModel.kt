package com.example.myapplication.screens.login

import com.example.myapplication.app.CustomApp
import com.example.myapplication.data.User

class LoginModel(private val app: CustomApp) {
    fun validateCredentials(username: String, password: String): Boolean {
       return username.equals(app.username, false) && password.equals(app.password, false)
    }

    fun saveData(username: String, password: String) {
        app.loginUser = User(username, password)
    }

}