package com.example.kantinku.ui.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityOnboardingBinding
import com.example.kantinku.ui.login.LoginActivity
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    private val onboardingItems = listOf(
        OnboardingItem(
            title = "Pesan Makanan Tanpa Antri",
            description = "Cukup dengan beberapa klik, pesananmu akan langsung diproses",
            imageRes = R.drawable.ic_onboarding_1
        ),
        OnboardingItem(
            title = "Pembayaran Digital",
            description = "Bayar pakai Saldo Kampus, QRIS, atau E-Wallet favoritmu",
            imageRes = R.drawable.ic_onboarding_2
        ),
        OnboardingItem(
            title = "Lacak Pesanan Real-time",
            description = "Pantau status pesananmu dari dapur hingga siap diambil",
            imageRes = R.drawable.ic_onboarding_3
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = OnboardingAdapter(onboardingItems)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            // No text needed
        }.attach()

        binding.btnNext.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingItems.size - 1) {
                binding.viewPager.currentItem = currentItem + 1
            } else {
                finishOnboarding()
            }
        }

        binding.btnSkip.setOnClickListener {
            finishOnboarding()
        }
    }

    private fun finishOnboarding() {
        val sharedPref = getSharedPreferences("onboarding", MODE_PRIVATE)
        sharedPref.edit().putBoolean("is_onboarding_completed", true).apply()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
}

data class OnboardingItem(
    val title: String,
    val description: String,
    val imageRes: Int
)