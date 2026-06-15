package com.example.kantinku.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.data.model.OrderItem
import com.example.kantinku.databinding.ActivityPaymentBinding
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var orderAdapter: PaymentOrderAdapter
    private var selectedPaymentMethod = PaymentMethod.SALDO_KAMPUS

    enum class PaymentMethod {
        SALDO_KAMPUS, QRIS, GOPAY, OVO, DANA
    }

    // 🔥 DATA DUMMY UNTUK ORDER ITEMS
    private val orderItems = listOf(
        OrderItem(
            id = 1,
            name = "Ayam Geprek Sambal Matah",
            price = 22000,
            quantity = 1,
            note = "Level pedas 3, tambah terong"
        ),
        OrderItem(
            id = 2,
            name = "Es Teh Manis Jumbo",
            price = 10000,
            quantity = 2,
            note = "Es nya sedikit, gula dikurangi"
        ),
        OrderItem(
            id = 3,
            name = "Nasi Putih",
            price = 5000,
            quantity = 1,
            note = ""
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pembayaran"

        sessionManager = SessionManager(this)

        setupOrderSummary()
        setupPaymentMethods()
        setupPaymentButton()
    }

    private fun setupOrderSummary() {
        // 🔥 TAMPILKAN DATA DUMMY
        val orderId = intent.getStringExtra("order_id") ?: "KTN-${System.currentTimeMillis()}"
        binding.tvOrderId.text = "ID Pesanan: #$orderId"

        // Setup adapter untuk order items
        orderAdapter = PaymentOrderAdapter(orderItems)
        binding.rvOrderItems.layoutManager = LinearLayoutManager(this)
        binding.rvOrderItems.adapter = orderAdapter

        // Hitung total harga dari data dummy
        val subtotal = orderItems.sumOf { it.price * it.quantity }
        val serviceFee = 2000
        val total = subtotal + serviceFee

        binding.tvSubtotal.text = CurrencyFormatter.format(subtotal)
        binding.tvServiceFee.text = CurrencyFormatter.format(serviceFee)
        binding.tvTotalPayment.text = CurrencyFormatter.format(total)
    }

    private fun setupPaymentMethods() {
        // Saldo Kampus
        binding.cardSaldoKampus.setOnClickListener {
            selectPaymentMethod(PaymentMethod.SALDO_KAMPUS)
            showSaldoDetail(true)
        }

        // QRIS
        binding.cardQris.setOnClickListener {
            selectPaymentMethod(PaymentMethod.QRIS)
            showSaldoDetail(false)
        }

        // E-Wallets
        binding.cardGopay.setOnClickListener {
            selectPaymentMethod(PaymentMethod.GOPAY)
            showSaldoDetail(false)
        }

        binding.cardOvo.setOnClickListener {
            selectPaymentMethod(PaymentMethod.OVO)
            showSaldoDetail(false)
        }

        binding.cardDana.setOnClickListener {
            selectPaymentMethod(PaymentMethod.DANA)
            showSaldoDetail(false)
        }

        // Default selection
        selectPaymentMethod(PaymentMethod.SALDO_KAMPUS)
    }

    private fun selectPaymentMethod(method: PaymentMethod) {
        selectedPaymentMethod = method

        // Reset all backgrounds
        resetCardSelection()

        // Highlight selected card
        when (method) {
            PaymentMethod.SALDO_KAMPUS -> highlightCard(binding.cardSaldoKampus, binding.chkSaldoKampus)
            PaymentMethod.QRIS -> highlightCard(binding.cardQris, binding.chkQris)
            PaymentMethod.GOPAY -> highlightCard(binding.cardGopay, binding.chkGopay)
            PaymentMethod.OVO -> highlightCard(binding.cardOvo, binding.chkOvo)
            PaymentMethod.DANA -> highlightCard(binding.cardDana, binding.chkDana)
        }

        // Update discount label
        if (method == PaymentMethod.SALDO_KAMPUS) {
            binding.tvDiscountLabel.visibility = android.view.View.VISIBLE
        } else {
            binding.tvDiscountLabel.visibility = android.view.View.GONE
        }
    }

    private fun resetCardSelection() {
        binding.cardSaldoKampus.setCardBackgroundColor(getColor(R.color.surface_container_lowest))
        binding.chkSaldoKampus.isChecked = false
        binding.cardQris.setCardBackgroundColor(getColor(R.color.surface_container_lowest))
        binding.chkQris.isChecked = false
        binding.cardGopay.setCardBackgroundColor(getColor(R.color.surface_container_lowest))
        binding.chkGopay.isChecked = false
        binding.cardOvo.setCardBackgroundColor(getColor(R.color.surface_container_lowest))
        binding.chkOvo.isChecked = false
        binding.cardDana.setCardBackgroundColor(getColor(R.color.surface_container_lowest))
        binding.chkDana.isChecked = false
    }

    private fun highlightCard(card: androidx.cardview.widget.CardView, checkBox: android.widget.CheckBox) {
        card.setCardBackgroundColor(getColor(R.color.primary_light))
        checkBox.isChecked = true
    }

    private fun showSaldoDetail(show: Boolean) {
        if (show) {
            binding.tvSaldoAmount.visibility = android.view.View.VISIBLE
            binding.tvSaldoHint.visibility = android.view.View.VISIBLE
        } else {
            binding.tvSaldoAmount.visibility = android.view.View.GONE
            binding.tvSaldoHint.visibility = android.view.View.GONE
        }
    }

    private fun setupPaymentButton() {
        binding.btnBayarSekarang.setOnClickListener {
            processPayment()
        }
    }

    private fun processPayment() {
        binding.btnBayarSekarang.isEnabled = false
        binding.btnBayarSekarang.text = "Memproses..."

        binding.btnBayarSekarang.postDelayed({
            val total = orderItems.sumOf { it.price * it.quantity } + 2000

            when (selectedPaymentMethod) {
                PaymentMethod.SALDO_KAMPUS -> {
                    if (total <= 156500) {
                        Toast.makeText(this, "✅ Pembayaran berhasil menggunakan Saldo Kampus!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this, "❌ Saldo tidak mencukupi!", Toast.LENGTH_SHORT).show()
                    }
                }
                PaymentMethod.QRIS -> {
                    showQRISDialog()
                }
                PaymentMethod.GOPAY, PaymentMethod.OVO, PaymentMethod.DANA -> {
                    Toast.makeText(this, "✅ Redirect ke ${selectedPaymentMethod.name}...", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnBayarSekarang.isEnabled = true
            binding.btnBayarSekarang.text = "Bayar Sekarang"
        }, 2000)
    }

    private fun showQRISDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Scan QRIS")
            .setMessage("Silakan scan QR Code berikut untuk menyelesaikan pembayaran\n\nNominal: ${binding.tvTotalPayment.text}")
            .setPositiveButton("OK") { _, _ ->
                Toast.makeText(this, "✅ Pembayaran QRIS berhasil!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}