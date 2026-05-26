package com.example.myapplication.screens.dashboardActivity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.screens.piggy.PiggyActivity
import com.example.myapplication.screens.settings.SettingsActivity
import com.example.myapplication.R
import com.example.myapplication.app.CustomApp
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.loadSpendItems
import com.example.myapplication.utils.navigateTo
import java.text.NumberFormat
import java.util.Locale

class DashboardActivity: Activity(), DashboardActivityContract.View{
    @SuppressLint("MissingInflatedId", "SetTextI18n")
    private lateinit var dashboardActivityPresenter : DashboardActivityPresenter
    private lateinit var tvuserDisplay: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardhome_activity)
        tvuserDisplay = findViewById<TextView>(R.id.tvuserDisplay)
        dashboardActivityPresenter = DashboardActivityPresenter(this, DashboardActivityModel(application as CustomApp))
        dashboardActivityPresenter.getUsername()
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Sync piggy balance to home screen
        val tvBalanceValue = findViewById<TextView>(R.id.tvBalanceValue)
        val piggyBalance = sharedPref.getFloat("piggyBalance", 0.0f).toDouble()
        val format = NumberFormat.getNumberInstance(Locale.US)
        format.minimumFractionDigits = 2
        format.maximumFractionDigits = 2
        tvBalanceValue.text = "P ${format.format(piggyBalance)}"

        // Load spend items dynamically
        loadSpendItems(R.id.spendContainerHome)

        setupBottomNav()

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
                   4 -> navigateTo(PiggyActivity::class.java)
                   5 -> navigateTo(SettingsActivity::class.java)
                   6 -> {
                       showLogoutConfirm(sharedPref)
                   }
               }
                true
            }

            popup.show()
        }
    }

    private fun showLogoutConfirm(sharedPref: android.content.SharedPreferences) {
        AlertDialog.Builder(this)
            .setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setPositiveButton("Yes") { _, _ ->
                sharedPref.edit().putBoolean("isLoggedIn", false).apply()
                navigateTo(LoginActivity::class.java, clearStack = true)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setupBottomNav() {
        findViewById<ImageButton>(R.id.btnhome).setColorFilter(Color.GREEN)
        findViewById<ImageButton>(R.id.btnlist).setColorFilter(Color.GRAY)
        findViewById<View>(R.id.btnProfileBottom).setOnClickListener {
            navigateTo(DashboardProfileActivity::class.java)
        }
        findViewById<View>(R.id.btnPiggyBottom).setOnClickListener {
            navigateTo(PiggyActivity::class.java)
        }
        findViewById<View>(R.id.btnSettingsBottom).setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        findViewById<ImageButton>(R.id.btnhome).setOnClickListener {
            // already here
        }
        findViewById<ImageButton>(R.id.btnlist).setOnClickListener {
            navigateTo(DashboardListActivity::class.java)
        }
    }

    override fun displayWelcomeMessage(message: String) {
        tvuserDisplay.setText(message)
    }
}