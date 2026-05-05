package com.example.myapplication.screens.login


class LoginContract {
   interface View {
       fun showInvalidCredentialsMessage()
       fun showEmptyMessage()
       fun showDashboard()
       fun onLoginSuccess()
       fun onEmptyFields()
   }
   interface Presenter {
       fun login(username: String, password: String, savedUsername: String, savedPassword: String)
    }
}

