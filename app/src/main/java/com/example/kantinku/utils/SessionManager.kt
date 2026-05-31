package com.example.kantinku.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("KantinKuPrefs", Context.MODE_PRIVATE)

    fun saveUser(userId: Int, username: String, isSeller: Boolean) {
        prefs.edit().putInt("user_id", userId)
            .putString("username", username)
            .putBoolean("is_seller", isSeller)
            .putBoolean("is_logged_in", true).apply()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun getUserId(): Int = prefs.getInt("user_id", -1)
    fun getUsername(): String = prefs.getString("username", "") ?: ""
    fun isSeller(): Boolean = prefs.getBoolean("is_seller", false)

    fun logout() = prefs.edit().clear().apply()

    // Tambahkan method ini
    fun isFirstTimeLaunch(): Boolean {
        val prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        return !prefs.getBoolean("is_onboarding_completed", false)
    }

    fun setOnboardingCompleted() {
        val prefs = context.getSharedPreferences("onboarding", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("is_onboarding_completed", true).apply()
    }
}