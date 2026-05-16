package com.example.kantinku.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityMainBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Kantin-Ku"

        setupNavigation()
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNav.setupWithNavController(navController)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    navController.navigate(item.itemId)
                    return@setOnNavigationItemSelectedListener true
                }
            }
        }
    }
}