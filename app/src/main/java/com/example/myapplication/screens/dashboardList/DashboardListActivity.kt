package com.example.myapplication.screens.dashboardList

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.screens.piggy.PiggyActivity
import com.example.myapplication.screens.settings.SettingsActivity
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.loadSpendItems
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.saveSpendItem
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.toast
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DashboardListActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardlist_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Load spend items dynamically
        loadSpendItems(R.id.spendContainerList)

        // Add spend button
        findViewById<View>(R.id.btnAddSpend).setOnClickListener {
            showSpendDialog()
        }

        setupBottomNav()

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

    private fun showSpendDialog() {
        val container = layoutInflater.inflate(R.layout.dialog_spend, null)
        val etDescription = container.findViewById<EditText>(R.id.etSpendDescription)
        val etAmount = container.findViewById<EditText>(R.id.etSpendAmount)

        val dialog = AlertDialog.Builder(this)
            .setTitle(null)
            .setView(container)
            .setPositiveButton("Confirm", null)
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val description = etDescription.text.toString().trim()
            val amountText = etAmount.text.toString().trim()

            if (description.isEmpty()) {
                toast("Please describe what you spent on")
                return@setOnClickListener
            }
            if (amountText.isEmpty()) {
                toast("Please enter an amount")
                return@setOnClickListener
            }
            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                toast("Please enter a valid positive amount")
                return@setOnClickListener
            }

            // Deduct from piggy balance
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val currentBalance = sharedPref.getFloat("piggyBalance", 0.0f).toDouble()
            if (amount > currentBalance) {
                toast("Insufficient balance!")
                return@setOnClickListener
            }
            val newBalance = currentBalance - amount
            sharedPref.edit().putFloat("piggyBalance", newBalance.toFloat()).apply()

            // Record transaction for piggy bank history
            addTransaction("delete", amount, newBalance)

            saveSpendItem(description, amount)
            loadSpendItems(R.id.spendContainerList)
            toast("Spent ₱${String.format("%.2f", amount)} on $description")
            dialog.dismiss()
        }
    }

    private fun addTransaction(type: String, amount: Double, balanceAfter: Double) {
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val jsonStr = sharedPref.getString("transactions", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonStr)

        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US)
        val transaction = JSONObject().apply {
            put("type", type)
            put("amount", amount)
            put("balance", balanceAfter)
            put("timestamp", dateFormat.format(Date()))
        }
        jsonArray.put(transaction)
        sharedPref.edit().putString("transactions", jsonArray.toString()).apply()
    }

    private fun setupBottomNav() {
        findViewById<ImageButton>(R.id.btnhome).setColorFilter(Color.GRAY)
        findViewById<ImageButton>(R.id.btnlist).setColorFilter(Color.GREEN)

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
            navigateTo(DashboardActivity::class.java)
        }
        findViewById<ImageButton>(R.id.btnlist).setOnClickListener {
            // already here
        }
    }
}