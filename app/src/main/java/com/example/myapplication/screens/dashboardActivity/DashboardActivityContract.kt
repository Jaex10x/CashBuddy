package com.example.myapplication.screens.dashboardActivity

class DashboardActivityContract {

    interface View {
        fun displayWelcomeMessage(message: String)
    }
    interface  Presenter {
        fun getUsername()
    }
}