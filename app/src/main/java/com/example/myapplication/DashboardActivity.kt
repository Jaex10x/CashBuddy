package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity: AppCompatActivity() {
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardhome_activity)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val username = sharedPref.getString("username", "Guest")

        val userDisplay = findViewById<TextView>(R.id.tvuserDisplay)
        userDisplay.text = "Welcome, $username"
        println("Welcome $username")


        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, "Home")
            popup.menu.add(0, 2, 1, "List")
            popup.menu.add(0, 3, 2, "Personal Details")
            popup.menu.add(0, 4, 3, "Piggy Bank")
            popup.menu.add(0, 5, 4, "Settings")
            popup.menu.add(0, 6, 5, "Log out")

            val onMenuItemClickListener = popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {}
                    2 -> {
                        val intent = Intent(this, DashboardListActivity::class.java)
                        startActivity(intent)
                    }

                    3 -> {
                        val intent = Intent(this, DashboardProfileActivity::class.java)
                        startActivity(intent)
                    }
                    4 -> {}
                    5 -> {}
                    6 -> {
                        sharedPref.edit().putBoolean("isLoggedIn", false).apply()

                        val intent = Intent(this@DashboardActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
                true
            }

            popup.show()
        }

    }
}