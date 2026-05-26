package com.example.myapplication.screens.settings

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.screens.piggy.PiggyActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.toast

class SettingsActivity : Activity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_settings)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        setOnClick(R.id.btnSettingsMenu, View.OnClickListener { view ->
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

        setupSettingsButtons()
        setupCurrencySelector()
        setupBottomNav()

        // Update currency display
        val savedCurrency = sharedPref.getString("currency", "PHP") ?: "PHP"
        findViewById<TextView>(R.id.tvCurrencyValue).text = savedCurrency
    }

    private fun setupSettingsButtons() {
        // GET PRO button
        findViewById<View>(R.id.btnGetPro).setOnClickListener {
            toast("Pro feature coming soon!")
        }

        // SECURITY
        findViewById<View>(R.id.btnSecurity).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Security")
                .setItems(arrayOf("Change Password", "Privacy Settings", "Two-Factor Authentication")) { _, which ->
                    when (which) {
                        0 -> showChangePasswordDialog()
                        1 -> toast("Privacy Settings - Coming soon")
                        2 -> toast("Two-Factor Authentication - Coming soon")
                    }
                }
                .setNegativeButton("Close", null)
                .show()
        }

        // LANGUAGE
        findViewById<View>(R.id.btnLanguage).setOnClickListener {
            val currentLang = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("language", "en")
            val languages = arrayOf("English", "Filipino")
            val langCodes = arrayOf("en", "fil")
            val checkedItem = if (currentLang == "fil") 1 else 0

            AlertDialog.Builder(this)
                .setTitle("Select Language")
                .setSingleChoiceItems(languages, checkedItem) { dialog, which ->
                    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    sharedPref.edit().putString("language", langCodes[which]).apply()
                    toast("Language set to ${languages[which]}")
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // THEME
        findViewById<View>(R.id.btnTheme).setOnClickListener {
            val currentTheme = getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("theme", "dark")
            val themes = arrayOf("Dark Mode", "Light Mode", "System Default")
            val themeValues = arrayOf("dark", "light", "system")
            val checkedItem = themeValues.indexOf(currentTheme).coerceAtLeast(0)

            AlertDialog.Builder(this)
                .setTitle("Select Theme")
                .setSingleChoiceItems(themes, checkedItem) { dialog, which ->
                    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                    sharedPref.edit().putString("theme", themeValues[which]).apply()
                    toast("Theme changed to ${themes[which]}")
                    dialog.dismiss()
                    // Recreate activity to apply new theme
                    recreate()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // ABOUT
        findViewById<View>(R.id.btnAbout).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("About CashBuddy")
                .setMessage(
                    """
                    CashBuddy v1.0.0

                    Your personal finance companion that helps you track your cash, manage your piggy bank, and stay on top of your finances.

                    Developed with ❤️ for smarter money management.

                    © 2025 CashBuddy. All rights reserved.
                    """.trimIndent()
                )
                .setPositiveButton("OK", null)
                .show()
        }

        // LOG OUT (from settings list)
        findViewById<View>(R.id.btnLogoutSetting).setOnClickListener {
            showLogoutConfirm(getSharedPreferences("MyPrefs", MODE_PRIVATE))
        }

        // DELETE ACCOUNT
        findViewById<View>(R.id.btnDeleteAccount).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you absolutely sure you want to delete your account? This action cannot be undone and all your data will be permanently lost.")
                .setPositiveButton("Delete") { _, _ ->
                    AlertDialog.Builder(this)
                        .setTitle("Confirm Deletion")
                        .setMessage("This will permanently erase all your data. Continue?")
                        .setPositiveButton("Yes, Delete Everything") { _, _ ->
                            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                            sharedPref.edit().clear().apply()
                            toast("Account deleted successfully")
                            navigateTo(LoginActivity::class.java, clearStack = true)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // CURRENCY SELECTOR
    private fun setupCurrencySelector() {
        findViewById<View>(R.id.btnCurrency).setOnClickListener {
            val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
            val currentCurrency = sharedPref.getString("currency", "PHP") ?: "PHP"
            val currencies = arrayOf("PHP", "USD", "EUR", "JPY", "GBP", "KRW", "SGD", "MYR")
            val checkedItem = currencies.indexOf(currentCurrency).coerceAtLeast(0)

            AlertDialog.Builder(this)
                .setTitle("Select Currency")
                .setSingleChoiceItems(currencies, checkedItem) { dialog, which ->
                    val selected = currencies[which]
                    sharedPref.edit().putString("currency", selected).apply()
                    findViewById<TextView>(R.id.tvCurrencyValue).text = selected
                    toast("Currency set to $selected")
                    dialog.dismiss()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    // CHANGE PASSWORD
    private fun showChangePasswordDialog() {
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val currentPassword = sharedPref.getString("password", "") ?: ""

        val container = layoutInflater.inflate(R.layout.dialog_change_password, null)
        val etCurrentPw = container.findViewById<EditText>(R.id.etCurrentPassword)
        val etNewPw = container.findViewById<EditText>(R.id.etNewPassword)
        val etConfirmPw = container.findViewById<EditText>(R.id.etConfirmPassword)

        val dialog = AlertDialog.Builder(this)
            .setTitle(null)
            .setView(container)
            .setPositiveButton("Save", null)
            .setNegativeButton("Cancel", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            val current = etCurrentPw.text.toString().trim()
            val newPw = etNewPw.text.toString().trim()
            val confirmPw = etConfirmPw.text.toString().trim()

            if (current.isEmpty() || newPw.isEmpty() || confirmPw.isEmpty()) {
                toast("All fields are required")
                return@setOnClickListener
            }

            if (current != currentPassword) {
                toast("Current password is incorrect")
                return@setOnClickListener
            }

            if (newPw.length < 4) {
                toast("Password must be at least 4 characters")
                return@setOnClickListener
            }

            if (newPw != confirmPw) {
                toast("New passwords do not match")
                return@setOnClickListener
            }

            sharedPref.edit().putString("password", newPw).apply()
            toast("Password changed successfully!")
            dialog.dismiss()
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

    @SuppressLint("MissingInflatedId")
    private fun setupBottomNav() {
        findViewById<ImageButton>(R.id.btnhome).setColorFilter(Color.GRAY)
        findViewById<ImageButton>(R.id.btnlist).setColorFilter(Color.GRAY)

        findViewById<View>(R.id.btnProfileBottom).setOnClickListener {
            navigateTo(DashboardProfileActivity::class.java)
        }
        findViewById<View>(R.id.btnPiggyBottom).setOnClickListener {
            navigateTo(PiggyActivity::class.java)
        }
        findViewById<View>(R.id.btnSettingsBottom).setOnClickListener {
            // already here
        }

        findViewById<ImageButton>(R.id.btnhome).setOnClickListener {
            navigateTo(DashboardActivity::class.java)
        }
        findViewById<ImageButton>(R.id.btnlist).setOnClickListener {
            navigateTo(DashboardListActivity::class.java)
        }
    }
}