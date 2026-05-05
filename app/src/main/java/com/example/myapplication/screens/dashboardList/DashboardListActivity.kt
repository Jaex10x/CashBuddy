package com.example.myapplication.screens.dashboardList

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.setOnClick

class DashboardListActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardlist_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        setOnClick(R.id.btnMenu1, View.OnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, "Home")
            popup.menu.add(0, 2, 1, "List")
            popup.menu.add(0, 3, 2, "Personal Details")
            popup.menu.add(0, 4, 3, "Piggy Bank")
            popup.menu.add(0, 5, 4, "Settings")
            popup.menu.add(0, 6, 5, "Log out")

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> navigateTo(DashboardActivity::class.java)
                    2 -> navigateTo(DashboardListActivity::class.java)
                    3 -> navigateTo(DashboardProfileActivity::class.java)
                    4 -> {}
                    5 -> {}
                    6 -> {
                        sharedPref.edit().putBoolean("isLoggedIn", false).apply()
                        navigateTo(LoginActivity::class.java, clearStack = true)
                    }
                }
                true
            }

            popup.show()

        })
    }
    private fun setupBottomNav() {
        val home = findViewById<ImageButton>(R.id.btnhome)
        val list = findViewById<ImageButton>(R.id.btnlist)
//                val piggy = findViewById<ImageButton>(R.id.btnPiggy)
//                val settings = findViewById<ImageButton>(R.id.btnSettings)

        home.setColorFilter(Color.GRAY)
        list.setColorFilter(Color.GREEN)
//                piggy.setColorFilter(Color.GRAY)
//                settings.setColorFilter(Color.GRAY)

        home.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }

        list.setOnClickListener {

        }
    }
}