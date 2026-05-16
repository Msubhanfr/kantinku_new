package com.example.kantinku.ui.cart

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.databinding.ActivityCartBinding
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var flashDealAdapter: FlashDealAdapter

    // Data keranjang
    private val cartItems = mutableListOf<CartItem>()

    // Data flash deal
    private val flashDeals = listOf(
        FlashDeal(
            id = 1,
            name = "Pisang Goreng Madu",
            description = "Sempurna untuk camilan sore sambil belajar!",
            originalPrice = 15000,
            dealPrice = 12000,
            imageRes = com.example.kantinku.R.drawable.ic_food
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Keranjang Belanja"

        sessionManager = SessionManager(this)

        setupCartData()
        setupRecyclerViews()
        setupFlashDealSection()
        setupCheckoutButton()
        setupBottomNav()
        updateOrderSummary()
    }

    private fun setupCartData() {
        // Data dummy untuk testing
        cartItems.addAll(
            listOf(
                CartItem(
                    id = 1,
                    name = "Nasi Campur Spesial",
                    price = 25000,
                    quantity = 1,
                    imageRes = com.example.kantinku.R.drawable.ic_food
                ),
                CartItem(
                    id = 2,
                    name = "Es Teh Manis",
                    price = 5000,
                    quantity = 2,
                    imageRes = com.example.kantinku.R.drawable.ic_drink
                )
            )
        )
    }

    private fun setupRecyclerViews() {
        // Cart RecyclerView
        cartAdapter = CartAdapter(
            cartItems,
            onQuantityChange = { item, newQuantity ->
                updateQuantity(item, newQuantity)
            },
            onRemove = { item ->
                removeItem(item)
            }
        )
        binding.rvCart.layoutManager = LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter

        // Flash Deal RecyclerView
        flashDealAdapter = FlashDealAdapter(flashDeals) { deal ->
            addToCart(deal)
        }
        binding.rvFlashDeal.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvFlashDeal.adapter = flashDealAdapter
    }

    private fun setupFlashDealSection() {
        binding.tvFlashDealTimer.text = "BERAKHIR DALAM 15:32"
    }

    private fun updateQuantity(item: CartItem, newQuantity: Int) {
        val index = cartItems.indexOfFirst { it.id == item.id }
        if (index != -1) {
            cartItems[index] = cartItems[index].copy(quantity = newQuantity)
            cartAdapter.updateItem(cartItems[index])
            updateOrderSummary()
        }
    }

    private fun removeItem(item: CartItem) {
        cartItems.remove(item)
        cartAdapter.updateList(cartItems)
        updateOrderSummary()

        if (cartItems.isEmpty()) {
            binding.rvCart.visibility = android.view.View.GONE
            binding.tvEmptyCart.visibility = android.view.View.VISIBLE
        }
    }

    private fun addToCart(deal: FlashDeal) {
        // Cek apakah sudah ada di keranjang
        val existingItem = cartItems.find { it.name == deal.name }

        if (existingItem != null) {
            updateQuantity(existingItem, existingItem.quantity + 1)
        } else {
            val newItem = CartItem(
                id = deal.id,
                name = deal.name,
                price = deal.dealPrice,
                quantity = 1,
                imageRes = deal.imageRes
            )
            cartItems.add(newItem)
            cartAdapter.updateList(cartItems)
            updateOrderSummary()

            if (binding.rvCart.visibility == android.view.View.GONE) {
                binding.rvCart.visibility = android.view.View.VISIBLE
                binding.tvEmptyCart.visibility = android.view.View.GONE
            }
        }

        Toast.makeText(this, "${deal.name} ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
    }

    private fun updateOrderSummary() {
        val subtotal = cartItems.sumOf { it.price * it.quantity }
        val serviceFee = 2000
        val total = subtotal + serviceFee

        binding.tvSubtotal.text = CurrencyFormatter.format(subtotal)
        binding.tvServiceFee.text = CurrencyFormatter.format(serviceFee)
        binding.tvTotal.text = CurrencyFormatter.format(total)
    }

    private fun checkout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Keranjang kosong!", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate random order ID
        val orderId = "ORD-${(10000..99999).random()}"

        // Navigate ke payment atau langsung ke status
        val intent = Intent(this, OrderStatusActivity::class.java)
        intent.putExtra("order_id", orderId)
        startActivity(intent)
        finish()
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener {
            finish()
        }
        binding.navOrders.setOnClickListener {
            Toast.makeText(this, "Orders", Toast.LENGTH_SHORT).show()
        }
        binding.navCart.setOnClickListener {
            // Already in cart
        }
        binding.navHistory.setOnClickListener {
            Toast.makeText(this, "History", Toast.LENGTH_SHORT).show()
        }
        binding.navProfile.setOnClickListener {
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

// Data Classes
data class CartItem(
    val id: Int,
    val name: String,
    val price: Int,
    val quantity: Int,
    val imageRes: Int
)

data class FlashDeal(
    val id: Int,
    val name: String,
    val description: String,
    val originalPrice: Int,
    val dealPrice: Int,
    val imageRes: Int
)