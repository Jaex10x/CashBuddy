package com.example.myapplication.screens.dashboardProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.PopupMenu
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.screens.piggy.PiggyActivity
import com.example.myapplication.screens.settings.SettingsActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.getEditTextValue
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.setEditTextValue
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.setTextValue
import com.example.myapplication.utils.toast

class DashboardProfileActivity : Activity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardprofile_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        setupBottomNav()

        setEditTextValue(R.id.etFirstName, sharedPref.getString("firstName", "")!!)
        setEditTextValue(R.id.etLastName, sharedPref.getString("lastName", "")!!)
        setEditTextValue(R.id.etContact, sharedPref.getString("contact", "")!!)
        setTextValue(R.id.tvProfileTitle, "${sharedPref.getString("firstName", "")} ${sharedPref.getString("lastName", "")}")

        setOnClick(R.id.btnMenu2, View.OnClickListener { view ->
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
        })

        setOnClick(R.id.btnSaveProfile, View.OnClickListener {
            val firstName = getEditTextValue(R.id.etFirstName)
            val lastName = getEditTextValue(R.id.etLastName)
            val contact = getEditTextValue(R.id.etContact)

            sharedPref.edit()
                .putString("firstName", firstName)
                .putString("lastName", lastName)
                .putString("contact", contact)
                .apply()

            setTextValue(R.id.tvProfileTitle, "$firstName $lastName")
            toast("Profile Updated!")
        })
    }

    private fun setupBottomNav() {
        findViewById<ImageButton>(R.id.btnhome).setColorFilter(Color.GRAY)
        findViewById<ImageButton>(R.id.btnlist).setColorFilter(Color.GRAY)

        findViewById<View>(R.id.btnProfileBottom).setOnClickListener {
            // already here
        }
        findViewById<View>(R.id.btnPiggyBottom).setOnClickListener {
            navigateTo(PiggyActivity::class.java)
        }
        findViewById<View>(R.id.btnSettingsBottom).setOnClickListener {
            navigateTo(SettingsActivity::class.java)
        }

        findViewById<ImageButton>(R.id.btnhome).setOnClickListener {
            navigateTo(DashboardActivity::class.java)
        }
        findViewById<ImageButton>(R.id.btnlist).setOnClickListener {
            navigateTo(DashboardListActivity::class.java)
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
}