package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardProfileActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardprofile_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val etFirstName = findViewById<EditText>(R.id.etFirstName)
        val etLastName = findViewById<EditText>(R.id.etLastName)
        val etContact = findViewById<EditText>(R.id.etContact)
        val btnSaveProfile = findViewById<Button>(R.id.btnSaveProfile)
        val tvProfileTitle = findViewById<TextView>(R.id.tvProfileTitle)
        val firstName = sharedPref.getString("firstName", "")
        val lastName = sharedPref.getString("lastName", "")
        etFirstName.setText(sharedPref.getString("firstName", ""))
        etLastName.setText(sharedPref.getString("lastName", ""))
        etContact.setText(sharedPref.getString("contact", ""))
        tvProfileTitle.text = "$firstName $lastName"

        val btnMenu2 = findViewById<ImageButton>(R.id.btnMenu2)
        btnMenu2.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menu.add(0, 1, 0, "Home")
            popup.menu.add(0, 2, 1, "List")
            popup.menu.add(0, 3, 2, "Personal Details")
            popup.menu.add(0, 4, 3, "Piggy Bank")
            popup.menu.add(0, 5, 4, "Settings")
            popup.menu.add(0, 6, 5, "log out")

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
                    6 -> {
                        sharedPref.edit().putBoolean("isLoggedIn", false).apply()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                }
                true
            }

            popup.show()
        }
        btnSaveProfile.setOnClickListener {

            sharedPref.edit()
                .putString("firstName", etFirstName.text.toString())
                .putString("lastName", etLastName.text.toString())
                .putString("contact", etContact.text.toString())
                .apply()

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
        }
        btnSaveProfile.setOnClickListener {

            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()

            sharedPref.edit()
                .putString("firstName", firstName)
                .putString("lastName", lastName)
                .putString("contact", etContact.text.toString())

                .apply()

            tvProfileTitle.text = "$firstName $lastName"

            Toast.makeText(this, "Profile Updated!", Toast.LENGTH_SHORT).show()
        }

    }
}