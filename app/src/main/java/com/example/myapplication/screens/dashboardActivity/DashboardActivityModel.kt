package com.example.myapplication.screens.dashboardActivity

import android.content.Context.MODE_PRIVATE
import com.example.myapplication.app.CustomApp

class DashboardActivityModel(private val app: CustomApp) {
    fun getUsername(): String {
        val sharedPref = app.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPref.getString("username", "") ?: ""
    }
    fun isNewUser(): Boolean {
        val sharedPref = app.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        return sharedPref.getBoolean("isNewUser", false)
    }

    fun clearNewUserFlag() {
        val sharedPref = app.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        sharedPref.edit().putBoolean("isNewUser", false).apply()
    }
}