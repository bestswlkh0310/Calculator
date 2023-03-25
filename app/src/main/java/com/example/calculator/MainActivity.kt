package com.example.calculator

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.*


class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private val operators = listOf('+', '-', '*', '/')
    private var isEnd: Boolean = false

    override fun onClick (v: View?){
        var screen: EditText = findViewById(R.id.screen)
        val screenText: String = screen.text.toString()
        var result: Double?
        if (isEnd) {
            screen.setText("")
            isEnd = false
        }
        when (v?.id) {
            R.id.btn_1 -> screen.append("1")
            R.id.btn_2 -> screen.append("2")
            R.id.btn_3 -> screen.append("3")
            R.id.btn_4 -> screen.append("4")
            R.id.btn_5 -> screen.append("5")
            R.id.btn_6 -> screen.append("6")
            R.id.btn_7 -> screen.append("7")
            R.id.btn_8 -> screen.append("8")
            R.id.btn_9 -> screen.append("9")
            R.id.btn_0 -> screen.append("0")

            R.id.btn_sum -> {
                if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("+")
            }
            R.id.btn_minus -> screen.append("-")
            R.id.btn_mul -> if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("*")
            R.id.btn_div -> if (screenText.isNotEmpty() && screenText.last() !in operators) screen.append("/")
            R.id.btn_left -> screen.append("(")
            R.id.btn_right -> screen.append(")")
            R.id.btn_dot -> screen.append(".")
            R.id.btn_result -> {
                try {
                    isEnd = true
                    result = eval(screenText)
                    screen.setText(result.toString())
                } catch (e: Error) {
                    Toast.makeText(this, "에러", Toast.LENGTH_LONG).show()
                }
            }
            R.id.btn_clear -> {
                screen.setText("")
            }
        }
    }

    private fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch = 0
            fun nextChar() {
                ch = if (++pos < str.length) str[pos].toInt() else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Unexpected: " + ch.toChar())
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'.code)) x += parseTerm() // addition
                    else if (eat('-'.code)) x -= parseTerm() // subtraction
                    else return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'.code)) x *= parseFactor() // multiplication
                    else if (eat('/'.code)) x /= parseFactor() // division
                    else return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+'.code)) return +parseFactor() // unary plus
                if (eat('-'.code)) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (eat('('.code)) { // parentheses
                    x = parseExpression()
                    if (!eat(')'.code)) throw RuntimeException("Missing ')'")
                } else if (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) { // numbers
                    while (ch >= '0'.code && ch <= '9'.code || ch == '.'.code) nextChar()
                    x = str.substring(startPos, pos).toDouble()
                } else if (ch >= 'a'.code && ch <= 'z'.code) { // functions
                    while (ch >= 'a'.code && ch <= 'z'.code) nextChar()
                    val func = str.substring(startPos, pos)
                    if (eat('('.code)) {
                        x = parseExpression()
                        if (!eat(')'.code)) throw RuntimeException("Missing ')' after argument to $func")
                    } else {
                        x = parseFactor()
                    }
                    x = when (func) {
                        "sqrt" -> sqrt(x)
                        "sin" -> sin(Math.toRadians((x)))
                        "cos" -> cos(Math.toRadians((x)))
                        "tan" -> tan(Math.toRadians((x)))
                        else -> throw java.lang.RuntimeException("Unknown function: $func")
                    }
/*                    x = if (func == "sqrt") sqrt(x) else if (func == "sin") sin(
                            Math.toRadians(x)
                        ) else if (func == "cos") cos(
                            Math.toRadians(x)
                        ) else if (func == "tan") tan(Math.toRadians(x)) else throw RuntimeException(
                            "Unknown function: $func"
                        )*/
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                if (eat('^'.code)) x = x.pow(parseFactor()) // exponentiation
                return x
            }
        }.parse()
    }
}