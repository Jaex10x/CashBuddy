package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardhome_activity)



        val userEnteredEmail = intent.getStringExtra("USERNAME") ?:"Guest"
        val userDisplay = findViewById<TextView>(R.id.tvuserDisplay)

        println("Welcome $userEnteredEmail")

        userDisplay.text = "Welcome, $userEnteredEmail"

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, "Home")
            popup.menu.add(0, 2, 1, "List")
            popup.menu.add(0, 3, 2, "Personal Details")
            popup.menu.add(0, 4, 3, "Piggy Bank")
            popup.menu.add(0, 5, 4, "Settings")

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
                }
                true
            }

            popup.show()
        }

    }
}