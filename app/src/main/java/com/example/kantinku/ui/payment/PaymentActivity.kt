package com.example.kantinku.ui.payment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kantinku.databinding.ActivityPaymentBinding
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPaymentBinding
    private lateinit var sessionManager: SessionManager
    private var selectedPaymentMethod = PaymentMethod.SALDO_KAMPUS

    enum class PaymentMethod {
        SALDO_KAMPUS, QRIS, GOPAY, OVO, DANA
    }

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
        // Get data from intent
        val orderId = intent.getStringExtra("order_id") ?: "KTN-8821"
        binding.tvOrderId.text = "ID Pesanan: #$orderId"

        // Dummy data for order items
        val orderItems = listOf(
            OrderItem("Ayam Geprek Sambal Matah", 22000, "1x Porsi"),
            OrderItem("Es Teh Manis Jumbo", 10000, "2x Porsi")
        )

        val orderItemsAdapter = OrderItemsAdapter(orderItems)
        binding.rvOrderItems.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvOrderItems.adapter = orderItemsAdapter

        val subtotal = 32000
        val serviceFee = 2000
        val total = 34000

        binding.tvSubtotal.text = CurrencyFormatter.format(subtotal)
        binding.tvServiceFee.text = CurrencyFormatter.format(serviceFee)
        binding.tvTotalPayment.text = CurrencyFormatter.format(total)
        binding.btnTotalBayar.text = "TOTAL BAYAR\n${CurrencyFormatter.format(total)}"
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
        // Saldo Kampus
        binding.cardSaldoKampus.setCardBackgroundColor(getColor(com.example.kantinku.R.color.surface_container_lowest))
        binding.chkSaldoKampus.isChecked = false

        // QRIS
        binding.cardQris.setCardBackgroundColor(getColor(com.example.kantinku.R.color.surface_container_lowest))
        binding.chkQris.isChecked = false

        // E-Wallets
        binding.cardGopay.setCardBackgroundColor(getColor(com.example.kantinku.R.color.surface_container_lowest))
        binding.chkGopay.isChecked = false
        binding.cardOvo.setCardBackgroundColor(getColor(com.example.kantinku.R.color.surface_container_lowest))
        binding.chkOvo.isChecked = false
        binding.cardDana.setCardBackgroundColor(getColor(com.example.kantinku.R.color.surface_container_lowest))
        binding.chkDana.isChecked = false
    }

    private fun highlightCard(card: androidx.cardview.widget.CardView, checkBox: android.widget.CheckBox) {
        card.setCardBackgroundColor(getColor(com.example.kantinku.R.color.primary_light))
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
        // Show loading
        binding.btnBayarSekarang.isEnabled = false
        binding.btnBayarSekarang.text = "Memproses..."

        lifecycleScope.launch {
            delay(2000) // Simulate payment processing

            val total = 34000

            when (selectedPaymentMethod) {
                PaymentMethod.SALDO_KAMPUS -> {
                    if (total <= 156500) {
                        Toast.makeText(this@PaymentActivity, "Pembayaran berhasil menggunakan Saldo Kampus!", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@PaymentActivity, "Saldo tidak mencukupi!", Toast.LENGTH_SHORT).show()
                    }
                }
                PaymentMethod.QRIS -> {
                    showQRISDialog()
                }
                PaymentMethod.GOPAY, PaymentMethod.OVO, PaymentMethod.DANA -> {
                    Toast.makeText(this@PaymentActivity, "Redirect ke ${selectedPaymentMethod.name}...", Toast.LENGTH_SHORT).show()
                }
            }

            binding.btnBayarSekarang.isEnabled = true
            binding.btnBayarSekarang.text = "Bayar Sekarang"
        }
    }

    private fun showQRISDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Scan QRIS")
            .setMessage("Silakan scan QR Code berikut untuk menyelesaikan pembayaran")
            .setPositiveButton("OK") { _, _ ->
                Toast.makeText(this, "Pembayaran QRIS berhasil!", Toast.LENGTH_SHORT).show()
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

data class OrderItem(
    val name: String,
    val price: Int,
    val quantity: String
)