package com.example.myapplication.screens.login

class LoginPresenter(
    private val view: LoginContract.View,
    private val model: LoginModel
) : LoginContract.Presenter {
    override fun login(username: String, password: String, savedUsername: String, savedPassword: String) {
        if (username.isNotEmpty() && password.isNotEmpty()) {
            model.saveData(username, password)
            if(model.validateCredentials(username, password)) {
                view.onLoginSuccess()
            } else {
                view.showInvalidCredentialsMessage()
            }
        } else {
            view.onEmptyFields()
        }
    }
}