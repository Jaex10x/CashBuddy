package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity

class DashboardProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardprofile_activity)

        val btnMenu2 = findViewById<ImageButton>(R.id.btnMenu2)
        btnMenu2.setOnClickListener { view ->
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
}