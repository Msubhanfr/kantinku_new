package com.example.kantinku.utils

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

object ErrorHandler {

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showErrorDialog(context: Context, title: String, message: String) {
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    fun handleNetworkError(context: Context, throwable: Throwable) {
        val message = when {
            throwable.message?.contains("Unable to resolve host") == true ->
                "Tidak ada koneksi internet. Periksa koneksi Anda."
            throwable.message?.contains("timeout") == true ->
                "Koneksi timeout. Silakan coba lagi."
            else ->
                "Terjadi kesalahan: ${throwable.message}"
        }
        showToast(context, message)
    }
}