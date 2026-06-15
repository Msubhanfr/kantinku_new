package com.example.kantinku.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.FragmentHistoryBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var historyAdapter: HistoryAdapter
    private var currentFilter = "Semua"

    // Data dummy untuk riwayat pesanan
    private val allOrders = mutableListOf<OrderHistory>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupTabs()
        setupRecyclerView()
        loadDummyData()
        setupPromoSection()
        setupBottomNav()
    }

    private fun loadDummyData() {
        allOrders.addAll(
            listOf(
                OrderHistory(
                    id = "KTN-001",
                    warungName = "Warung Bu Sri",
                    date = "12 Okt 2023",
                    time = "12:45",
                    totalPrice = 25000,
                    status = "Selesai",
                    items = listOf(
                        OrderItemHistory("Nasi Campur Spesial", 1, 18000),
                        OrderItemHistory("Es Teh Manis", 1, 5000),
                        OrderItemHistory("Telur Dadar", 1, 2000)
                    )
                ),
                OrderHistory(
                    id = "KTN-002",
                    warungName = "Kopi Kampus",
                    date = "10 Okt 2023",
                    time = "09:15",
                    totalPrice = 18000,
                    status = "Selesai",
                    items = listOf(
                        OrderItemHistory("Kopi Hitam", 2, 10000),
                        OrderItemHistory("Pisang Goreng", 1, 8000)
                    )
                ),
                OrderHistory(
                    id = "KTN-003",
                    warungName = "Ayam Geprek Juara",
                    date = "08 Okt 2023",
                    time = "13:20",
                    totalPrice = 32500,
                    status = "Selesai",
                    items = listOf(
                        OrderItemHistory("Ayam Geprek Spesial", 1, 22000),
                        OrderItemHistory("Es Jeruk", 1, 7000),
                        OrderItemHistory("Nasi Putih", 1, 3500)
                    )
                ),
                OrderHistory(
                    id = "KTN-004",
                    warungName = "Sate Madura",
                    date = "05 Okt 2023",
                    time = "18:30",
                    totalPrice = 45000,
                    status = "Dibatalkan",
                    items = listOf(
                        OrderItemHistory("Sate Ayam (10 tusuk)", 1, 35000),
                        OrderItemHistory("Es Teh Manis", 2, 10000)
                    )
                ),
                OrderHistory(
                    id = "KTN-005",
                    warungName = "Warung Mbok Galak",
                    date = "01 Okt 2023",
                    time = "11:30",
                    totalPrice = 28000,
                    status = "Selesai",
                    items = listOf(
                        OrderItemHistory("Nasi Goreng Special", 1, 18000),
                        OrderItemHistory("Es Cincau", 1, 10000)
                    )
                )
            )
        )

        filterOrders(currentFilter)
    }

    private fun setupTabs() {
        // Tab Semua
        binding.tabAll.setOnClickListener {
            currentFilter = "Semua"
            updateTabUI(binding.tabAll, binding.tabCompleted, binding.tabCancelled)
            filterOrders("Semua")
        }

        // Tab Selesai
        binding.tabCompleted.setOnClickListener {
            currentFilter = "Selesai"
            updateTabUI(binding.tabCompleted, binding.tabAll, binding.tabCancelled)
            filterOrders("Selesai")
        }

        // Tab Dibatalkan
        binding.tabCancelled.setOnClickListener {
            currentFilter = "Dibatalkan"
            updateTabUI(binding.tabCancelled, binding.tabAll, binding.tabCompleted)
            filterOrders("Dibatalkan")
        }

        // Set default tab aktif
        updateTabUI(binding.tabAll, binding.tabCompleted, binding.tabCancelled)
    }

    private fun updateTabUI(activeTab: TextView, tab2: TextView, tab3: TextView) {
        // Active tab style
        activeTab.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
        activeTab.setBackgroundResource(R.drawable.bg_tab_active_filter)
        activeTab.setPadding(24, 8, 24, 8)

        // Inactive tabs style
        val inactiveTabs = listOf(tab2, tab3)
        inactiveTabs.forEach { tab ->
            tab.setTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface_secondary))
            tab.setBackgroundResource(android.R.color.transparent)
            tab.setPadding(24, 8, 24, 8)
        }
    }

    private fun setupRecyclerView() {
        historyAdapter = HistoryAdapter(
            onReorderClick = { order ->
                reorder(order)
            },
            onItemClick = { order ->
                showOrderDetail(order)
            }
        )
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = historyAdapter
    }

    private fun filterOrders(status: String) {
        val filtered = if (status == "Semua") {
            allOrders
        } else {
            allOrders.filter { it.status == status }
        }

        historyAdapter.submitList(filtered)

        if (filtered.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
        }
    }

    private fun reorder(order: OrderHistory) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            delay(800)
            binding.progressBar.visibility = View.GONE

            Toast.makeText(requireContext(), "Menambahkan pesanan dari ${order.warungName} ke keranjang", Toast.LENGTH_LONG).show()

            // Navigate to Cart
            val intent = android.content.Intent(requireContext(), CartActivity::class.java)
            intent.putExtra("reorder_id", order.id)
            startActivity(intent)
        }
    }

    private fun showOrderDetail(order: OrderHistory) {
        val itemsText = order.items.joinToString("\n") {
            "${it.name} x${it.quantity} - ${CurrencyFormatter.format(it.price)}"
        }

        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Detail Pesanan")
            .setMessage("""
                ID Pesanan: ${order.id}
                Warung: ${order.warungName}
                Tanggal: ${order.date} • ${order.time}
                Status: ${order.status}
                
                ─────────────────
                $itemsText
                ─────────────────
                
                Total: ${CurrencyFormatter.format(order.totalPrice)}
            """.trimIndent())
            .setPositiveButton("Tutup", null)
            .setNeutralButton("Pesan Lagi") { _, _ ->
                reorder(order)
            }
            .show()
    }

    private fun setupPromoSection() {
        binding.btnPromoClaim.setOnClickListener {
            Toast.makeText(requireContext(), "Kupon promo: NASIGILA10 - Diskon 10% untuk Nasi Goreng Gila!", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener {
            navigateToHome()
        }
        binding.navOrders.setOnClickListener {
            Toast.makeText(requireContext(), "Orders", Toast.LENGTH_SHORT).show()
        }
        binding.navCart.setOnClickListener {
            navigateToCart()
        }
        binding.navHistory.setOnClickListener {
            // Already in history
        }
        binding.navProfile.setOnClickListener {
            navigateToProfile()
        }
    }

    private fun navigateToHome() {
        val fragment = com.example.kantinku.ui.home.HomeFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun navigateToCart() {
        val intent = android.content.Intent(requireContext(), com.example.kantinku.ui.cart.CartActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToProfile() {
        val fragment = com.example.kantinku.ui.profile.ProfileFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Data Classes
data class OrderHistory(
    val id: String,
    val warungName: String,
    val date: String,
    val time: String,
    val totalPrice: Int,
    val status: String,
    val items: List<OrderItemHistory>
)

data class OrderItemHistory(
    val name: String,
    val quantity: Int,
    val price: Int
)