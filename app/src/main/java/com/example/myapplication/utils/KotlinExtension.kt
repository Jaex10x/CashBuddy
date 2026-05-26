package com.example.myapplication.utils

import android.app.Activity
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Activity.getEditTextValue(id: Int): String {
    return findViewById<EditText>(id).text.toString()
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Activity.requireText(id: Int, errorMessage: String): Boolean {
    val editText = findViewById<EditText>(id)
    return if (editText.text.toString().trim().isEmpty()) {
        editText.error = errorMessage
        false
    } else {
        true
    }
}

fun Activity.setOnClick(id: Int, listener: View.OnClickListener) {
    findViewById<View>(id).setOnClickListener(listener)
}

fun Activity.setEditTextValue(id: Int, value: String) {
    findViewById<EditText>(id).setText(value)
}

fun Activity.setTextValue(id: Int, value: String) {
    findViewById<TextView>(id).text = value
}

fun Activity.navigateTo(destination: Class<*>, clearStack: Boolean = false) {
    val intent = Intent(this, destination)
    if (clearStack) intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}

fun Activity.applyCurrentTheme() {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val themePref = sharedPref.getString("theme", "dark") ?: "dark"
    val themeRes = when (themePref) {
        "light" -> R.style.Theme_MyApplication_Light
        "system" -> R.style.Theme_MyApplication_System
        else -> R.style.Theme_MyApplication_Dark
    }
    setTheme(themeRes)
}

fun Activity.loadSpendItems(containerId: Int) {
    val container = findViewById<LinearLayout>(containerId)
    container.removeAllViews()

    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val jsonStr = sharedPref.getString("spendItems", "[]") ?: "[]"
    val jsonArray = JSONArray(jsonStr)

    if (jsonArray.length() == 0) {
        val emptyText = TextView(this)
        emptyText.text = "No spending yet"
        emptyText.setTextColor(resources.getColor(android.R.color.darker_gray, theme))
        emptyText.textSize = 14f
        emptyText.setPadding(0, 24, 0, 24)
        emptyText.gravity = android.view.Gravity.CENTER
        container.addView(emptyText)
        return
    }

    val format = NumberFormat.getNumberInstance(Locale.US)
    format.minimumFractionDigits = 2
    format.maximumFractionDigits = 2

    for (i in jsonArray.length() - 1 downTo 0) {
        val item = jsonArray.getJSONObject(i)
        val description = item.getString("description")
        val amount = item.getDouble("amount")
        val date = item.getString("date")

        val itemView = layoutInflater.inflate(R.layout.item_spending, null)
        itemView.findViewById<TextView>(R.id.tvSpendCategory).text = "Cash Spend - $date"
        itemView.findViewById<TextView>(R.id.tvSpendDescription).text = description
        itemView.findViewById<TextView>(R.id.tvSpendAmount).text = "-P ${format.format(amount)}"
        container.addView(itemView)
    }
}

fun Activity.saveSpendItem(description: String, amount: Double) {
    val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
    val jsonStr = sharedPref.getString("spendItems", "[]") ?: "[]"
    val jsonArray = JSONArray(jsonStr)

    val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.US)
    val item = JSONObject().apply {
        put("description", description)
        put("amount", amount)
        put("date", dateFormat.format(Date()))
        put("timestamp", System.currentTimeMillis())
    }
    jsonArray.put(item)
    sharedPref.edit().putString("spendItems", jsonArray.toString()).apply()
}