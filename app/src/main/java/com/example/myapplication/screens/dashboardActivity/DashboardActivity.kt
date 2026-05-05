package com.example.myapplication.screens.dashboardActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.contentcapture.DataShareWriteAdapter
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.R
import com.example.myapplication.app.CustomApp
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.navigateTo

class DashboardActivity: Activity(), DashboardActivityContract.View{
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private lateinit var dashboardActivityPresenter : DashboardActivityPresenter
    private lateinit var tvuserDisplay: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardhome_activity)
        Log.e("DashboardActivityHome", "onCreate is called")
        tvuserDisplay = findViewById<TextView>(R.id.tvuserDisplay)
        dashboardActivityPresenter = DashboardActivityPresenter(this, DashboardActivityModel(application as CustomApp))
        dashboardActivityPresenter.getUsername()
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)



        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener { view ->
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
        }
    }
    private fun setupBottomNav() {
        val home = findViewById<ImageButton>(R.id.btnhome)
        val list = findViewById<ImageButton>(R.id.btnlist)
//            val piggy = findViewById<ImageButton>(R.id.btnPiggy)
//            val settings = findViewById<ImageButton>(R.id.btnSettings)


        home.setColorFilter(Color.GREEN)
        list.setColorFilter(Color.GRAY)
        //piggy.setColorFilter(Color.GRAY)
        //settings.setColorFilter(Color.GRAY)

        home.setOnClickListener {
            // already here
        }

        list.setOnClickListener {
            startActivity(Intent(this, DashboardListActivity::class.java))
            finish()
        }
    }

    override fun displayWelcomeMessage(message: String) {
        tvuserDisplay.setText(message)
    }
}