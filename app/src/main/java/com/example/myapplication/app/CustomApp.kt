package com.example.myapplication.app

import android.app.Application
import android.util.Log
import com.example.myapplication.data.User

class CustomApp : Application() {

    var username = ""
    var password = ""

   var loginUser = User()

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        username = sharedPref.getString("username", "") ?: ""
        password = sharedPref.getString("password", "") ?: ""
        Log.e("Custom App", "onCreate is called")
    }
}