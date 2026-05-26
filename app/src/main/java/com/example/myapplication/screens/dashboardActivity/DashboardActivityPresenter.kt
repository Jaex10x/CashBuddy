package com.example.myapplication.screens.dashboardActivity

class DashboardActivityPresenter(
   private val view: DashboardActivityContract.View,
    private val model: DashboardActivityModel
): DashboardActivityContract.Presenter {
    override fun getUsername() {
        val username = model.getUsername()
        if(username.isNotEmpty()) {
            view.displayWelcomeMessage("Welcome back, $username!")
        }else {
            view.displayWelcomeMessage("Welcome back, User!")
        }
    }
}