package com.example.myapplication.screens.dashboardProfile

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.getEditTextValue
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.setEditTextValue
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.setTextValue
import com.example.myapplication.utils.toast

class DashboardProfileActivity : Activity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardprofile_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

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
}