package com.example.kantinku.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityLoginBinding
import com.example.kantinku.ui.main.MainActivity
import com.example.kantinku.utils.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager
    private var isLoginMode = true
    private var isVerificationSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        if (sessionManager.isLoggedIn()) {
            navigateToMain()
        }

        setupTabs()
        setupListeners()
        setupFooterLinks()
    }

    private fun setupTabs() {
        binding.tabLogin.setOnClickListener {
            isLoginMode = true
            updateTabUI()
            resetVerificationState()
        }

        binding.tabRegister.setOnClickListener {
            isLoginMode = false
            updateTabUI()
            resetVerificationState()
        }

        updateTabUI()
    }

    private fun updateTabUI() {
        if (isLoginMode) {
            binding.tabLogin.setTextColor(ContextCompat.getColor(this, R.color.primary))
            binding.tabLogin.setBackgroundResource(R.drawable.bg_tab_active)
            binding.tabRegister.setTextColor(ContextCompat.getColor(this, R.color.on_surface_secondary))
            binding.tabRegister.setBackgroundResource(R.drawable.bg_tab_inactive)

            binding.tvFormTitle.text = "Halo, Sobat Kampus!"
            binding.tvFormSubtitle.text = "Gunakan identitas kampusmu untuk ${getString(R.string.login)}."
            binding.btnSubmit.text = "Minta Kode Verifikasi →"  // ← Baris 65

        } else {
            binding.tabRegister.setTextColor(ContextCompat.getColor(this, R.color.primary))
            binding.tabRegister.setBackgroundResource(R.drawable.bg_tab_active)
            binding.tabLogin.setTextColor(ContextCompat.getColor(this, R.color.on_surface_secondary))
            binding.tabLogin.setBackgroundResource(R.drawable.bg_tab_inactive)

            binding.tvFormTitle.text = "Bergabung Sekarang!"
            binding.tvFormSubtitle.text = "Daftar menggunakan identitas kampusmu."
            binding.btnSubmit.text = "Daftar & Verifikasi →"
        }
    }

    private fun setupListeners() {
        binding.btnSubmit.setOnClickListener {
            val nimOrEmail = binding.etNimOrEmail.text.toString().trim()

            if (nimOrEmail.isEmpty()) {
                Toast.makeText(this, "Masukkan NIM atau Email Kampus", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isVerificationSent) {
                requestVerificationCode(nimOrEmail)
            } else {
                verifyCode(nimOrEmail)
            }
        }

        binding.btnSsoLogin.setOnClickListener {
            Toast.makeText(this, "Fitur SSO Kampus segera hadir", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestVerificationCode(nimOrEmail: String) {
        showProgress(true)

        binding.root.postDelayed({
            showProgress(false)
            isVerificationSent = true
            showVerificationCodeUI(nimOrEmail)
            Toast.makeText(this, "Kode verifikasi telah dikirim ke $nimOrEmail", Toast.LENGTH_LONG).show()
        }, 1500)
    }

    private fun showVerificationCodeUI(nimOrEmail: String) {
        binding.etNimOrEmail.visibility = View.GONE
        binding.inputLayoutNim.visibility = View.GONE

        binding.inputLayoutVerificationCode.visibility = View.VISIBLE
        binding.etVerificationCode.visibility = View.VISIBLE

        binding.btnSubmit.text = "Verifikasi Kode →"

        binding.tvVerificationInfo.visibility = View.VISIBLE
        binding.tvVerificationInfo.text = "Kode verifikasi dikirim ke $nimOrEmail"

        startResendTimer()
    }

    private fun startResendTimer() {
        binding.tvResendCode.visibility = View.VISIBLE
        binding.tvResendCode.isEnabled = false
        binding.tvResendCode.setTextColor(ContextCompat.getColor(this, R.color.on_surface_disabled))

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000
                binding.tvResendCode.text = "Kirim ulang kode ($seconds detik)"
            }

            override fun onFinish() {
                binding.tvResendCode.text = "Kirim ulang kode"
                binding.tvResendCode.isEnabled = true
                binding.tvResendCode.setTextColor(ContextCompat.getColor(this@LoginActivity, R.color.primary))
                binding.tvResendCode.setOnClickListener {
                    val nimOrEmail = binding.etNimOrEmail.text.toString().trim()
                    requestVerificationCode(nimOrEmail)
                }
            }
        }.start()
    }

    private fun verifyCode(nimOrEmail: String) {
        val verificationCode = binding.etVerificationCode.text.toString().trim()

        if (verificationCode.isEmpty()) {
            Toast.makeText(this, "Masukkan kode verifikasi", Toast.LENGTH_SHORT).show()
            return
        }

        showProgress(true)

        binding.root.postDelayed({
            showProgress(false)

            if (isLoginMode) {
                sessionManager.saveUser(1, nimOrEmail.take(8), false)
                navigateToMain()
            } else {
                Toast.makeText(this, "Pendaftaran berhasil! Silakan login.", Toast.LENGTH_SHORT).show()
                resetToLoginMode()
            }
        }, 1500)
    }

    private fun resetToLoginMode() {
        isLoginMode = true
        isVerificationSent = false
        resetVerificationState()
        updateTabUI()
    }

    private fun resetVerificationState() {
        isVerificationSent = false

        binding.etNimOrEmail.visibility = View.VISIBLE
        binding.inputLayoutNim.visibility = View.VISIBLE
        binding.inputLayoutVerificationCode.visibility = View.GONE
        binding.etVerificationCode.visibility = View.GONE
        binding.tvVerificationInfo.visibility = View.GONE
        binding.tvResendCode.visibility = View.GONE

        binding.etNimOrEmail.text?.clear()
        binding.etVerificationCode.text?.clear()

        binding.btnSubmit.text = if (isLoginMode) "Minta Kode Verifikasi →" else "Daftar & Verifikasi →"
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.btnSubmit.isEnabled = !show
        binding.btnSsoLogin.isEnabled = !show
    }

    private fun setupFooterLinks() {
        binding.tvHelp.setOnClickListener {
            Toast.makeText(this, "Bantuan: Hubungi admin kantin", Toast.LENGTH_SHORT).show()
        }
        binding.tvPrivacy.setOnClickListener {
            Toast.makeText(this, "Kebijakan Privasi Kantin-Ku", Toast.LENGTH_SHORT).show()
        }
        binding.tvTerms.setOnClickListener {
            Toast.makeText(this, "Syarat & Ketentuan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}