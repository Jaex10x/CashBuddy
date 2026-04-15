package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class DashboardListActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardlist_activity)
        val btnMenu1 = findViewById<ImageButton>(R.id.btnMenu1)

        btnMenu1.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, "Home")
            popup.menu.add(0, 2, 1, "List")
            popup.menu.add(0, 3, 2, "Personal Details")
            popup.menu.add(0, 4, 3, "Piggy Bank")
            popup.menu.add(0, 5, 4, "Settings")

            val onMenuItemClickListener = popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    1 -> {
                        val intent = Intent(this, DashboardActivity:: class.java)
                        startActivity(intent)
                        finish()
                    }
                    2 -> {
                        val intent = Intent(this, DashboardListActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    3 -> {
                        val intent = Intent(this, DashboardProfileActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    4 -> {}
                    5 -> {}

                }
                true
            }

            popup.show()

        }
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