package com.rama.txori.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.rama.txori.CsActivity
import com.rama.txori.R

class TimerActivity : CsActivity() {

    private lateinit var timerButton: TextView
    private lateinit var editView: LinearLayout
    private lateinit var timerInput: EditText
    private lateinit var addTimer: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_timer)

        val root = findViewById<View>(android.R.id.content)
        applyEdgeToEdgePadding(root)
        applyFont(root)

        timerButton = findViewById(R.id.timer_button)
        editView = findViewById(R.id.edit_view)
        timerInput = findViewById(R.id.timer)
        addTimer = findViewById(R.id.add_timer)

        timerButton.text = "00:00:00"

        timerButton.setOnClickListener {
            showEditor()
        }

        addTimer.setOnClickListener {
            applyInput()
        }
    }

    private fun showEditor() {
        timerButton.visibility = View.GONE
        editView.visibility = View.VISIBLE

        val digits = timerButton.text.toString()
            .filter { it.isDigit() }
            .trimStart('0')

        timerInput.setText(digits)
        timerInput.setSelection(timerInput.text.length)
        timerInput.requestFocus()

        showKeyboard()
    }

    private fun applyInput() {
        val digits = timerInput.text.toString()
            .filter { it.isDigit() }
            .takeLast(6)

        timerButton.text = formatDigits(digits)

        editView.visibility = View.GONE
        timerButton.visibility = View.VISIBLE

        hideKeyboard()
    }

    private fun formatDigits(digits: String): String {
        val padded = digits.padStart(6, '0')

        val hh = padded.substring(0, 2)
        val mm = padded.substring(2, 4)
        val ss = padded.substring(4, 6)

        return "$hh:$mm:$ss"
    }

    private fun showKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(timerInput, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(timerInput.windowToken, 0)
    }
}