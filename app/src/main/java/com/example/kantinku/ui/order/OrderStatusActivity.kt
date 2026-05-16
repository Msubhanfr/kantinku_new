package com.example.kantinku.ui.order

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityOrderStatusBinding
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class OrderStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrderStatusBinding
    private lateinit var sessionManager: SessionManager

    // Timer untuk estimasi
    private var timer: CountDownTimer? = null
    private var remainingSeconds = 600 // 10 menit dalam detik

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Status Pesanan"

        sessionManager = SessionManager(this)

        // Get data dari intent
        val orderId = intent.getStringExtra("order_id") ?: "ORD-88291"

        setupOrderInfo(orderId)
        setupProgressTimeline()
        setupTimer()
        setupOrderDetails()
        setupHelpButton()
    }

    private fun setupOrderInfo(orderId: String) {
        binding.tvOrderId.text = "#$orderId"
        binding.tvStatusTitle.text = "Pesananmu lagi diolah nih!"

        // Simulasi estimasi waktu
        binding.tvEstimationTime.text = "10 menit"
    }

    private fun setupTimer() {
        timer = object : CountDownTimer(remainingSeconds * 1000L, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = (millisUntilFinished / 1000) % 60

                binding.tvTimer.text = String.format("%02d:%02d", minutes, seconds)
                binding.tvEstimationTime.text = "${minutes} menit ${seconds} detik"

                // Update progress berdasarkan waktu
                updateProgressBasedOnTime(millisUntilFinished)
            }

            override fun onFinish() {
                binding.tvTimer.text = "00:00"
                binding.tvEstimationTime.text = "Siap diambil!"
                binding.tvStatusTitle.text = "Pesananmu sudah siap!"

                // Update semua status ke selesai
                completeAllSteps()

                Toast.makeText(this@OrderStatusActivity, "Pesanan sudah siap diambil!", Toast.LENGTH_LONG).show()
            }
        }.start()
    }

    private fun updateProgressBasedOnTime(millisLeft: Long) {
        val totalSeconds = 600L
        val elapsedSeconds = totalSeconds - (millisLeft / 1000)
        val progress = (elapsedSeconds.toFloat() / totalSeconds.toFloat()) * 100

        binding.progressBar.progress = progress.toInt()

        // Update timeline berdasarkan progress
        when {
            progress >= 100 -> {
                updateStepStatus(4, true) // Selesai
                updateStepStatus(3, true) // Siap diambil
                updateStepStatus(2, true) // Sedang dimasak
                updateStepStatus(1, true) // Pesanan diterima
            }
            progress >= 75 -> {
                updateStepStatus(3, true) // Siap diambil
                updateStepStatus(2, true) // Sedang dimasak
                updateStepStatus(1, true) // Pesanan diterima
                updateStepStatus(4, false)
            }
            progress >= 40 -> {
                updateStepStatus(2, true) // Sedang dimasak
                updateStepStatus(1, true) // Pesanan diterima
                updateStepStatus(3, false)
                updateStepStatus(4, false)
            }
            progress >= 10 -> {
                updateStepStatus(1, true) // Pesanan diterima
                updateStepStatus(2, false)
                updateStepStatus(3, false)
                updateStepStatus(4, false)
            }
        }
    }

    private fun setupProgressTimeline() {
        val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = dateFormat.format(Date())

        // Step 1: Pesanan diterima
        binding.tvStep1Time.text = currentTime
        binding.tvStep1Location.text = "Kantin Bu Sumi"

        // Step 2: Sedang dimasak
        binding.tvStep2Desc.text = "Chef sedang menyiapkan hidanganmu"

        // Step 3: Siap diambil
        binding.tvStep3Desc.text = "Segera ambil di counter 3"

        // Set status awal
        updateStepStatus(1, true)
        updateStepStatus(2, false)
        updateStepStatus(3, false)
        updateStepStatus(4, false)
    }

    private fun updateStepStatus(step: Int, isActive: Boolean) {
        val activeColor = ContextCompat.getColor(this, R.color.primary)
        val inactiveColor = ContextCompat.getColor(this, R.color.on_surface_disabled)
        val textActiveColor = ContextCompat.getColor(this, R.color.on_surface)
        val textInactiveColor = ContextCompat.getColor(this, R.color.on_surface_secondary)

        when (step) {
            1 -> {
                binding.ivStep1Icon.setColorFilter(if (isActive) activeColor else inactiveColor)
                binding.tvStep1Title.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.tvStep1Time.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.tvStep1Location.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.viewStep1.setBackgroundColor(if (isActive) activeColor else inactiveColor)
            }
            2 -> {
                binding.ivStep2Icon.setColorFilter(if (isActive) activeColor else inactiveColor)
                binding.tvStep2Title.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.tvStep2Desc.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.viewStep2.setBackgroundColor(if (isActive) activeColor else inactiveColor)
            }
            3 -> {
                binding.ivStep3Icon.setColorFilter(if (isActive) activeColor else inactiveColor)
                binding.tvStep3Title.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.tvStep3Desc.setTextColor(if (isActive) textActiveColor else textInactiveColor)
                binding.viewStep3.setBackgroundColor(if (isActive) activeColor else inactiveColor)
            }
            4 -> {
                binding.ivStep4Icon.setColorFilter(if (isActive) activeColor else inactiveColor)
                binding.tvStep4Title.setTextColor(if (isActive) textActiveColor else textInactiveColor)
            }
        }
    }

    private fun completeAllSteps() {
        updateStepStatus(1, true)
        updateStepStatus(2, true)
        updateStepStatus(3, true)
        updateStepStatus(4, true)

        binding.progressBar.progress = 100
    }

    private fun setupOrderDetails() {
        // Menu pesanan (dummy data)
        binding.tvMenuItem.text = "Nasi Goreng Spesial + Telur"
        binding.tvItemPrice.text = CurrencyFormatter.format(18000)

        // Lokasi
        binding.tvLocation.text = "Gedung C, Lt 1"

        // Metode bayar
        binding.tvPaymentMethod.text = "Saldo Kantin"
    }

    private fun setupHelpButton() {
        binding.btnHelp.setOnClickListener {
            androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Bantuan Pesanan")
                .setMessage("""
                    Butuh bantuan dengan pesananmu?
                    
                    📞 Hubungi: 0812-3456-7890
                    💬 Chat CS: cs@kantinku.com
                    🏠 Datang langsung ke kantin
                    
                    Atau tekan tombol "Laporkan Masalah" untuk melaporkan pesanan yang bermasalah.
                """.trimIndent())
                .setPositiveButton("Laporkan Masalah") { _, _ ->
                    Toast.makeText(this, "Masalah akan segera kami tindak lanjuti", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Tutup", null)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}