package com.example.myapplication.screens.piggy

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.screens.dashboardActivity.DashboardActivity
import com.example.myapplication.screens.dashboardList.DashboardListActivity
import com.example.myapplication.screens.dashboardProfile.DashboardProfileActivity
import com.example.myapplication.screens.login.LoginActivity
import com.example.myapplication.screens.settings.SettingsActivity
import com.example.myapplication.utils.applyCurrentTheme
import com.example.myapplication.utils.loadSpendItems
import com.example.myapplication.utils.navigateTo
import com.example.myapplication.utils.saveSpendItem
import com.example.myapplication.utils.setOnClick
import com.example.myapplication.utils.toast
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PiggyActivity : Activity() {

    private lateinit var balanceTextView: TextView
    private lateinit var transactionContainer: LinearLayout
    private lateinit var tvEmptyHistory: TextView
    private var currentBalance: Double = 0.0
    private var isBalanceHidden: Boolean = false

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        applyCurrentTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboardpiggy_activity)
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        balanceTextView = findViewById(R.id.balanceTextView)
        transactionContainer = findViewById(R.id.transactionContainer)
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory)
        currentBalance = sharedPref.getFloat("piggyBalance", 0.0f).toDouble()
        isBalanceHidden = sharedPref.getBoolean("isBalanceHidden", false)

        updateBalanceDisplay()
        loadTransactionHistory()

        setOnClick(R.id.btnPiggyMenu, View.OnClickListener { view ->
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
                        showConfirmDialog(
                            title = "Log Out",
                            message = "Are you sure you want to log out?"
                        ) {
                            sharedPref.edit().putBoolean("isLoggedIn", false).apply()
                            navigateTo(LoginActivity::class.java, clearStack = true)
                        }
                    }
                }
                true
            }
            popup.show()
        })

        setupBottomNav()
        setupCashButtons()
    }

    private fun updateBalanceDisplay() {
        if (isBalanceHidden) {
            balanceTextView.text = "••••••••"
        } else {
            val format = NumberFormat.getNumberInstance(Locale.US)
            format.minimumFractionDigits = 2
            format.maximumFractionDigits = 2
            balanceTextView.text = "₱ ${format.format(currentBalance)}"
        }
    }

    private fun saveBalance() {
        getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()
            .putFloat("piggyBalance", currentBalance.toFloat())
            .apply()
    }

    private fun addTransaction(type: String, amount: Double) {
        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val jsonStr = sharedPref.getString("transactions", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonStr)

        val dateFormat = SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.US)
        val transaction = JSONObject().apply {
            put("type", type)
            put("amount", amount)
            put("balance", currentBalance)
            put("timestamp", dateFormat.format(Date()))
        }
        jsonArray.put(transaction)

        sharedPref.edit().putString("transactions", jsonArray.toString()).apply()
        loadTransactionHistory()
    }

    private fun loadTransactionHistory() {
        transactionContainer.removeViews(1, transactionContainer.childCount - 1)

        val sharedPref = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val jsonStr = sharedPref.getString("transactions", "[]") ?: "[]"
        val jsonArray = JSONArray(jsonStr)

        if (jsonArray.length() == 0) {
            tvEmptyHistory.visibility = View.VISIBLE
            return
        }
        tvEmptyHistory.visibility = View.GONE

        val format = NumberFormat.getNumberInstance(Locale.US)
        format.minimumFractionDigits = 2
        format.maximumFractionDigits = 2

        for (i in jsonArray.length() - 1 downTo 0) {
            val txn = jsonArray.getJSONObject(i)
            val type = txn.getString("type")
            val amount = txn.getDouble("amount")
            val timestamp = txn.getString("timestamp")

            val itemView = layoutInflater.inflate(R.layout.transaction_item, null)
            val isAdd = type == "add"
            itemView.findViewById<TextView>(R.id.tvTxnType).text =
                if (isAdd) "+ ₱${format.format(amount)}" else "- ₱${format.format(amount)}"
            itemView.findViewById<TextView>(R.id.tvTxnType).setTextColor(
                if (isAdd) resources.getColor(android.R.color.holo_blue_light) else resources.getColor(android.R.color.holo_blue_dark)
            )
            itemView.findViewById<TextView>(R.id.tvTxnLabel).text =
                if (isAdd) "Cash Added" else "Cash Deducted"
            itemView.findViewById<TextView>(R.id.tvTxnTimestamp).text = timestamp
            // Update indicator icon and color
            val indicatorIcon = itemView.findViewById<TextView>(R.id.txnIndicatorIcon)
            indicatorIcon.text = if (isAdd) "+" else "−"
            indicatorIcon.setTextColor(
                if (isAdd) resources.getColor(android.R.color.holo_blue_light) else resources.getColor(android.R.color.holo_blue_dark)
            )

            transactionContainer.addView(itemView)

            if (i > 0) {
                val divider = View(this)
                divider.layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 1
                ).apply { setMargins(0, 0, 0, 0) }
                divider.setBackgroundColor(resources.getColor(R.color.divider_color, theme))
                transactionContainer.addView(divider)
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun setupCashButtons() {
        findViewById<View>(R.id.btnAddCash).setOnClickListener {
            showAmountDialog("Add Cash", "Enter amount to add:") { amount ->
                currentBalance += amount
                saveBalance()
                updateBalanceDisplay()
                addTransaction("add", amount)
                toast("Successfully added ₱${String.format("%.2f", amount)}")
            }
        }

        findViewById<View>(R.id.btnDeleteCash).setOnClickListener {
            showConfirmDialog(
                title = "Delete Cash",
                message = "Are you sure you want to deduct cash from your balance?"
            ) {
                showSpendDialog()
            }
        }

        findViewById<View>(R.id.btnViewCash).setOnClickListener {
            if (isBalanceHidden) {
                isBalanceHidden = false
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()
                    .putBoolean("isBalanceHidden", false).apply()
                updateBalanceDisplay()
                toast("Balance is now visible")
            } else {
                toast("Balance is already visible")
            }
        }

        findViewById<View>(R.id.btnHideCash).setOnClickListener {
            if (!isBalanceHidden) {
                isBalanceHidden = true
                getSharedPreferences("MyPrefs", MODE_PRIVATE).edit()
                    .putBoolean("isBalanceHidden", true).apply()
                updateBalanceDisplay()
                toast("Balance is now hidden")
            } else {
                toast("Balance is already hidden")
            }
        }
    }

    private fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
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
            if (amount > currentBalance) {
                toast("Insufficient balance!")
                return@setOnClickListener
            }

            currentBalance -= amount
            saveBalance()
            updateBalanceDisplay()
            addTransaction("delete", amount)
            saveSpendItem(description, amount)
            loadSpendItems(R.id.spendContainerHome)
            toast("Spent ₱${String.format("%.2f", amount)} on $description")
            dialog.dismiss()
        }
    }

    private fun showAmountDialog(title: String, message: String, onConfirm: (Double) -> Unit) {
        val input = EditText(this)
        input.hint = "0.00"
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        input.setPadding(48, 24, 48, 24)

        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setView(input)
            .setPositiveButton("Confirm") { _, _ ->
                val text = input.text.toString().trim()
                if (text.isEmpty()) {
                    toast("Please enter an amount")
                    return@setPositiveButton
                }
                val amount = text.toDoubleOrNull()
                if (amount == null || amount <= 0) {
                    toast("Please enter a valid positive amount")
                } else {
                    onConfirm(amount)
                }
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
            // already here
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
}