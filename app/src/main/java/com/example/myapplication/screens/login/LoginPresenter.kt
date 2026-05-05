package com.example.myapplication.screens.login

import com.example.myapplication.R
import com.example.myapplication.app.CustomApp
import com.example.myapplication.utils.requireText
import com.example.myapplication.utils.toast

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel
) : LoginContract.Presenter {
    override fun login(username: String, password: String, savedUsername: String, savedPassword: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            model.saveData(username, password)
            if(model.validateCredentials(username, password)) {
                view.showDashboard()
                view.onLoginSuccess()
            } else {
                view.showInvalidCredentialsMessage()
            }


        } else {
            view.onEmptyFields()
        }
    }
}