package com.example.kantinku.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    fun format(amount: Int): String {
        val format = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        return format.format(amount)
    }
}