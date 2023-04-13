package com.example.calculator

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.calculator.databinding.ActivityMainBinding
import com.example.calculator.util.Calculator
import kotlin.math.*

class MainActivity : AppCompatActivity() {
    val TAG: String = "로그"
    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var screen: EditText
    private lateinit var screenText: String
    private val operators = listOf('+', '-', '*', '/')
    private var isEnd: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        screen = binding.screen
        screenText = screen.text.toString()

        binding.btn1.setOnClickListener { reload(); screen.append("1"); Log.d(TAG, "MainActivity - onCreate() called")}
        binding.btn2.setOnClickListener { reload(); screen.append("2") }
        binding.btn3.setOnClickListener { reload(); screen.append("3") }
        binding.btn4.setOnClickListener { reload(); screen.append("4") }
        binding.btn5.setOnClickListener { reload(); screen.append("5") }
        binding.btn6.setOnClickListener { reload(); screen.append("6") }
        binding.btn7.setOnClickListener { reload(); screen.append("7") }
        binding.btn8.setOnClickListener { reload(); screen.append("8") }
        binding.btn9.setOnClickListener { reload(); screen.append("9") }
        binding.btn0.setOnClickListener { reload(); screen.append("0") }
        binding.btnLeft.setOnClickListener { reload(); screen.append("(") }
        binding.btnRight.setOnClickListener { reload(); screen.append(")") }
        binding.btnDot.setOnClickListener { reload(); screen.append(".") }
        binding.btnClear.setOnClickListener { reload(); screen.setText("") }
        binding.btnSum.setOnClickListener { reload(); if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("+") }
        binding.btnMinus.setOnClickListener { reload(); if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("-") }
        binding.btnMul.setOnClickListener { reload(); if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("*") }
        binding.btnDiv.setOnClickListener { reload(); if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("/") }
        binding.btnResult.setOnClickListener {
            reload()
            isEnd = true
            Log.d(TAG, "$screenText - Clicked")
            screen.setText(Calculator.calc(screenText).toString())
        }
    }

    private fun reload() {
        screenText = screen.text.toString()
        if (isEnd) {
            screen.setText("")
            isEnd = false
        }
    }

}