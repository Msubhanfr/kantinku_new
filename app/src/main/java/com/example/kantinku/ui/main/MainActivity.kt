package com.example.kantinku.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityMainBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.ui.history.HistoryFragment
import com.example.kantinku.ui.home.HomeFragment
import com.example.kantinku.ui.menu.MenuFragment
import com.example.kantinku.ui.notification.NotificationFragment
import com.example.kantinku.ui.profile.ProfileFragment
import com.example.kantinku.utils.SessionManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        // Default fragment = Home
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
            setNavActive(R.id.navHome)
        }

        setupBottomNav()
        setupToolbarIcons()

        // Handle navigation from intent (dari halaman lain)
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
        binding.ivNotification.setOnClickListener {
            val notificationFragment = NotificationFragment()
            notificationFragment.show(supportFragmentManager, "notification")
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
        // Reset all icons to inactive
        resetNavIcons()

        // Set active icon based on selected nav
        when (activeId) {
            R.id.navHome -> {
                binding.navHome.findViewById<ImageView>(R.id.imgIcon)?.setImageResource(R.drawable.ic_home)
                (binding.navHome.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.primary))
                (binding.navHome.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.primary))
            }
            R.id.navMenu -> {
                (binding.navMenu.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.primary))
                (binding.navMenu.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.primary))
            }
            R.id.navHistory -> {
                (binding.navHistory.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.primary))
                (binding.navHistory.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.primary))
            }
            R.id.navProfile -> {
                (binding.navProfile.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.primary))
                (binding.navProfile.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.primary))
            }
        }
    }

    private fun resetNavIcons() {
        val navItems = listOf(binding.navHome, binding.navMenu, binding.navCart, binding.navHistory, binding.navProfile)

        navItems.forEach { navItem ->
            (navItem.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.on_surface_secondary))
            (navItem.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.on_surface_secondary))
        }

        // Reset cart icon (karena cart pindah ke activity)
        (binding.navCart.getChildAt(0) as? ImageView)?.setColorFilter(getColor(R.color.on_surface_secondary))
        (binding.navCart.getChildAt(1) as? TextView)?.setTextColor(getColor(R.color.on_surface_secondary))
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
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_notification -> {
                val notificationFragment = NotificationFragment()
                notificationFragment.show(supportFragmentManager, "notification")
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}