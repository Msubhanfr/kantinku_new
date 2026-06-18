package com.example.kantinku.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityMainBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.ui.history.HistoryFragment
import com.example.kantinku.ui.home.HomeFragment
import com.example.kantinku.ui.menu.MenuFragment
import com.example.kantinku.ui.notification.NotificationFragment
import com.example.kantinku.ui.profile.ProfileFragment
import com.example.kantinku.ui.seller.SellerDashboardActivity
import com.example.kantinku.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        // Cek role admin
        if (sessionManager.isSeller()) {
            startActivity(Intent(this, SellerDashboardActivity::class.java))
            finish()
            return
        }

        // Setup untuk user biasa
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            setNavActive(R.id.navHome)
        }

        setupBottomNav()
        setupToolbarIcons()
        handleIntentNavigation()
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener {
            loadFragment(HomeFragment())
            setNavActive(R.id.navHome)
        }

        binding.navMenu.setOnClickListener {
            loadFragment(MenuFragment())
            setNavActive(R.id.navMenu)
        }

        binding.navCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        binding.navHistory.setOnClickListener {
            loadFragment(HistoryFragment())
            setNavActive(R.id.navHistory)
        }

        binding.navProfile.setOnClickListener {
            loadFragment(ProfileFragment())
            setNavActive(R.id.navProfile)
        }
    }

    private fun setupToolbarIcons() {
        // 🔥 PERBAIKI: Gunakan supportFragmentManager untuk navigasi ke NotificationFragment
        binding.ivNotification.setOnClickListener {
            val fragment = NotificationFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        binding.ivAvatar.setOnClickListener {
            loadFragment(ProfileFragment())
            setNavActive(R.id.navProfile)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun setNavActive(activeId: Int) {
        resetNavIcons()

        val (textView, color) = when (activeId) {
            R.id.navHome -> binding.navHome to R.color.primary
            R.id.navMenu -> binding.navMenu to R.color.primary
            R.id.navHistory -> binding.navHistory to R.color.primary
            R.id.navProfile -> binding.navProfile to R.color.primary
            else -> null to R.color.on_surface_secondary
        }

        textView?.let {
            it.setTextColor(ContextCompat.getColor(this, color))
            // 🔥 PERBAIKI: Gunakan TextViewCompat
            TextViewCompat.setCompoundDrawableTintList(
                it,
                ContextCompat.getColorStateList(this, color)
            )
        }
    }

    private fun resetNavIcons() {
        val navItems = listOf(
            binding.navHome,
            binding.navMenu,
            binding.navCart,
            binding.navHistory,
            binding.navProfile
        )

        val color = ContextCompat.getColorStateList(this, R.color.on_surface_secondary)

        navItems.forEach { navItem ->
            navItem.setTextColor(ContextCompat.getColor(this, R.color.on_surface_secondary))
            // 🔥 PERBAIKI: Gunakan TextViewCompat
            TextViewCompat.setCompoundDrawableTintList(navItem, color)
        }
    }

    private fun handleIntentNavigation() {
        val navigateTo = intent.getStringExtra("navigate_to")
        when (navigateTo) {
            "home" -> {
                loadFragment(HomeFragment())
                setNavActive(R.id.navHome)
            }
            "menu" -> {
                loadFragment(MenuFragment())
                setNavActive(R.id.navMenu)
            }
            "history" -> {
                loadFragment(HistoryFragment())
                setNavActive(R.id.navHistory)
            }
            "profile" -> {
                loadFragment(ProfileFragment())
                setNavActive(R.id.navProfile)
            }
            else -> {
                // Default ke Home
                loadFragment(HomeFragment())
                setNavActive(R.id.navHome)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> {
                val fragment = NotificationFragment()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment)
                    .addToBackStack(null)
                    .commit()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}