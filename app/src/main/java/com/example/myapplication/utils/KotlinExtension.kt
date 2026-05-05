package com.example.myapplication.utils

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R

fun Activity.getEditTextValue(id: Int): String {
    return findViewById<EditText>(id).text.toString()
}

fun Activity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

// Add this to KotlinExtension.kt
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