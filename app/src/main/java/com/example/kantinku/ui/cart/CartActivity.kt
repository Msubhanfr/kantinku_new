package com.example.kantinku.ui.cart

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivityCartBinding
import com.example.kantinku.ui.payment.PaymentActivity
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var flashDealAdapter: FlashDealAdapter

    private val cartItems = mutableListOf<CartItem>()

    private val flashDeals = listOf(
        FlashDeal(1, "Pisang Goreng Madu", "Sempurna untuk camilan sore sambil belajar!", 15000, 12000, R.drawable.ic_food)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Keranjang Belanja"

        sessionManager = SessionManager(this)

        // 🔥 TERIMA DATA DARI INTENT (dari Home/Menu)
        val menuId = intent.getIntExtra("menu_id", -1)
        val menuName = intent.getStringExtra("menu_name")
        val menuPrice = intent.getIntExtra("menu_price", 0)

        setupRecyclerViews()
        setupFlashDealSection()
        setupBottomNav()

        // Jika ada data dari intent, tambahkan ke cart
        if (menuId != -1 && menuName != null) {
            addToCartDirectly(menuId, menuName, menuPrice)
        } else {
            loadCartItems()
        }

        updateOrderSummary()
    }

    private fun addToCartDirectly(id: Int, name: String, price: Int) {
        val existingItem = cartItems.find { it.id == id }
        if (existingItem != null) {
            updateQuantity(existingItem, existingItem.quantity + 1)
        } else {
            cartItems.add(CartItem(id, name, price, 1, R.drawable.ic_food))
            cartAdapter.updateList(cartItems)
            updateOrderSummary()
        }

        // Sembunyikan empty cart jika ada
        if (cartItems.isNotEmpty()) {
            binding.cvEmptyCart.visibility = android.view.View.GONE
            binding.rvCart.visibility = android.view.View.VISIBLE
        }

        Toast.makeText(this, "$name ditambahkan ke keranjang", Toast.LENGTH_SHORT).show()
    }

    private fun loadCartItems() {
        // Load dari database atau shared preferences
        if (cartItems.isEmpty()) {
            binding.cvEmptyCart.visibility = android.view.View.VISIBLE
            binding.rvCart.visibility = android.view.View.GONE
        } else {
            binding.cvEmptyCart.visibility = android.view.View.GONE
            binding.rvCart.visibility = android.view.View.VISIBLE
        }
    }

    private fun setupRecyclerViews() {
        cartAdapter = CartAdapter(
            cartItems,
            onQuantityChange = { item, newQuantity -> updateQuantity(item, newQuantity) },
            onRemove = { item -> removeItem(item) }
        )
        binding.rvCart.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        binding.rvCart.adapter = cartAdapter

        flashDealAdapter = FlashDealAdapter(flashDeals) { deal -> addFlashDealToCart(deal) }
        binding.rvFlashDeal.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        binding.rvFlashDeal.adapter = flashDealAdapter
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
            binding.cvEmptyCart.visibility = android.view.View.VISIBLE
            binding.rvCart.visibility = android.view.View.GONE
        }
    }

    private fun addFlashDealToCart(deal: FlashDeal) {
        val existingItem = cartItems.find { it.name == deal.name }
        if (existingItem != null) {
            updateQuantity(existingItem, existingItem.quantity + 1)
        } else {
            cartItems.add(CartItem(deal.id, deal.name, deal.dealPrice, 1, deal.imageRes))
            cartAdapter.updateList(cartItems)
            updateOrderSummary()

            if (binding.cvEmptyCart.visibility == android.view.View.VISIBLE) {
                binding.cvEmptyCart.visibility = android.view.View.GONE
                binding.rvCart.visibility = android.view.View.VISIBLE
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

        // Update button checkout
        binding.btnCheckout.text = "Bayar ${CurrencyFormatter.format(total)} →"
    }

    private fun setupFlashDealSection() {
        binding.tvFlashDealTimer.text = "BERAKHIR DALAM 15:32"
        binding.btnCheckout.setOnClickListener {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Keranjang belanja masih kosong!", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("total_amount", binding.tvTotal.text.toString())
                startActivity(intent)
            }
        }
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener {
            finish()
        }
        binding.navOrders.setOnClickListener {
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show()
        }
        binding.navCart.setOnClickListener { /* Already in cart */ }
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

data class CartItem(val id: Int, val name: String, val price: Int, val quantity: Int, val imageRes: Int)
data class FlashDeal(val id: Int, val name: String, val description: String, val originalPrice: Int, val dealPrice: Int, val imageRes: Int)