package com.example.kantinku.ui.seller

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.ActivitySellerDashboardBinding
import com.example.kantinku.utils.CurrencyFormatter
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SellerDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySellerDashboardBinding

    // Data Statistik
    private var selectedPeriod = "Harian" // Harian, Mingguan, Bulanan
    private lateinit var orderAdapter: SellerOrderAdapter

    // Data dummy untuk pesanan masuk
    private val orders = mutableListOf<SellerOrder>()

    // Data dummy untuk menu
    private val menuItems = mutableListOf<SellerMenuItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySellerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dashboard Penjual"

        setupData()
        setupPeriodTabs()
        setupOrderRecyclerView()
        setupMenuRecyclerView()
        setupBottomNavigation()
        setupFabMenu()
        updateStats()
    }

    private fun setupData() {
        // Data pesanan masuk
        orders.addAll(listOf(
            SellerOrder(
                id = "KTN-001",
                customerName = "Arya Wijaya",
                items = listOf("Nasi Goreng Spesial x1", "Es Teh Manis x2"),
                total = 28000,
                status = "pending",
                orderTime = System.currentTimeMillis() - 1000 * 60 * 5, // 5 menit lalu
                paymentMethod = "Saldo Kampus"
            ),
            SellerOrder(
                id = "KTN-002",
                customerName = "Budi Santoso",
                items = listOf("Ayam Geprek Sambal Ijo x2", "Es Jeruk x1"),
                total = 41000,
                status = "pending",
                orderTime = System.currentTimeMillis() - 1000 * 60 * 15,
                paymentMethod = "QRIS"
            ),
            SellerOrder(
                id = "KTN-003",
                customerName = "Citra Dewi",
                items = listOf("Mie Ayam Bakso x1", "Pisang Goreng x1"),
                total = 25000,
                status = "processing",
                orderTime = System.currentTimeMillis() - 1000 * 60 * 30,
                paymentMethod = "GOPAY"
            ),
            SellerOrder(
                id = "KTN-004",
                customerName = "Dian Pratama",
                items = listOf("Gado-Gado Segar x1", "Es Teh Manis x1"),
                total = 20000,
                status = "completed",
                orderTime = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                paymentMethod = "Saldo Kampus"
            )
        ))

        // Data menu
        menuItems.addAll(listOf(
            SellerMenuItem(1, "Nasi Goreng Spesial", 18000, 12, "Makanan", true),
            SellerMenuItem(2, "Ayam Geprek Sambal Ijo", 18000, 5, "Makanan", true),
            SellerMenuItem(3, "Mie Ayam Bakso", 17000, 8, "Makanan", true),
            SellerMenuItem(4, "Gado-Gado Segar", 15000, 3, "Makanan", true),
            SellerMenuItem(5, "Es Teh Manis", 5000, 20, "Minuman", true),
            SellerMenuItem(6, "Es Jeruk", 7000, 15, "Minuman", true),
            SellerMenuItem(7, "Pisang Goreng", 10000, 10, "Snack", true),
            SellerMenuItem(8, "Kentang Goreng", 12000, 0, "Snack", false)
        ))
    }

    private fun setupPeriodTabs() {
        binding.tabDaily.setOnClickListener {
            selectedPeriod = "Harian"
            updateTabUI(binding.tabDaily, binding.tabWeekly, binding.tabMonthly)
            updateStats()
        }

        binding.tabWeekly.setOnClickListener {
            selectedPeriod = "Mingguan"
            updateTabUI(binding.tabWeekly, binding.tabDaily, binding.tabMonthly)
            updateStats()
        }

        binding.tabMonthly.setOnClickListener {
            selectedPeriod = "Bulanan"
            updateTabUI(binding.tabMonthly, binding.tabDaily, binding.tabWeekly)
            updateStats()
        }

        updateTabUI(binding.tabDaily, binding.tabWeekly, binding.tabMonthly)
    }

    private fun updateTabUI(activeTab: View, tab2: View, tab3: View) {
        val activeColor = getColor(R.color.primary)
        val inactiveColor = getColor(R.color.on_surface_secondary)

        activeTab.findViewById<TextView>(R.id.tvTabText)?.setTextColor(activeColor)
        tab2.findViewById<TextView>(R.id.tvTabText)?.setTextColor(inactiveColor)
        tab3.findViewById<TextView>(R.id.tvTabText)?.setTextColor(inactiveColor)
    }

    private fun updateStats() {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

        // Simulasi data berdasarkan periode
        val (totalRevenue, totalOrders, averageOrder) = when (selectedPeriod) {
            "Harian" -> Triple(850000, 28, 30357)
            "Mingguan" -> Triple(5200000, 175, 29714)
            else -> Triple(18500000, 620, 29839)
        }

        binding.tvTotalRevenue.text = CurrencyFormatter.format(totalRevenue)
        binding.tvTotalOrders.text = totalOrders.toString()
        binding.tvAverageOrder.text = CurrencyFormatter.format(averageOrder)
    }

    private fun setupOrderRecyclerView() {
        orderAdapter = SellerOrderAdapter(orders) { order, action ->
            when (action) {
                "accept" -> {
                    updateOrderStatus(order.id, "processing")
                    Toast.makeText(this, "Pesanan ${order.id} diterima", Toast.LENGTH_SHORT).show()
                }
                "ready" -> {
                    updateOrderStatus(order.id, "ready")
                    Toast.makeText(this, "Pesanan ${order.id} siap diambil", Toast.LENGTH_SHORT).show()
                }
                "reject" -> {
                    updateOrderStatus(order.id, "rejected")
                    Toast.makeText(this, "Pesanan ${order.id} ditolak", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.rvOrders.layoutManager = LinearLayoutManager(this)
        binding.rvOrders.adapter = orderAdapter
    }

    private fun updateOrderStatus(orderId: String, status: String) {
        val index = orders.indexOfFirst { it.id == orderId }
        if (index != -1) {
            orders[index] = orders[index].copy(status = status)
            orderAdapter.updateList(orders)
        }
    }

    private fun setupMenuRecyclerView() {
        val menuAdapter = SellerMenuAdapter(menuItems) { menu, action ->
            when (action) {
                "edit" -> showEditMenuDialog(menu)
                "delete" -> showDeleteMenuDialog(menu)
                "toggle" -> {
                    val newStatus = !menu.isAvailable
                    val index = menuItems.indexOfFirst { it.id == menu.id }
                    if (index != -1) {
                        menuItems[index] = menu.copy(isAvailable = newStatus)
                        Toast.makeText(this, "${menu.name} ${if (newStatus) "diaktifkan" else "dinonaktifkan"}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        binding.rvMenu.layoutManager = LinearLayoutManager(this)
        binding.rvMenu.adapter = menuAdapter
    }

    private fun showEditMenuDialog(menu: SellerMenuItem) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_menu, null)
        val etName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuName)
        val etPrice = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuPrice)
        val etStock = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuStock)

        etName.setText(menu.name)
        etPrice.setText(menu.price.toString())
        etStock.setText(menu.stock.toString())

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Edit Menu")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = etName.text.toString()
                val newPrice = etPrice.text.toString().toIntOrNull() ?: menu.price
                val newStock = etStock.text.toString().toIntOrNull() ?: menu.stock

                val index = menuItems.indexOfFirst { it.id == menu.id }
                if (index != -1) {
                    menuItems[index] = menu.copy(name = newName, price = newPrice, stock = newStock)
                    Toast.makeText(this, "Menu berhasil diupdate", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteMenuDialog(menu: SellerMenuItem) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Hapus Menu")
            .setMessage("Apakah Anda yakin ingin menghapus ${menu.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                menuItems.removeAll { it.id == menu.id }
                Toast.makeText(this, "${menu.name} dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showAddMenuDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_menu, null)
        val etName = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuName)
        val etPrice = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuPrice)
        val etStock = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etMenuStock)

        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Tambah Menu Baru")
            .setView(dialogView)
            .setPositiveButton("Tambah") { _, _ ->
                val name = etName.text.toString()
                val price = etPrice.text.toString().toIntOrNull() ?: 0
                val stock = etStock.text.toString().toIntOrNull() ?: 0

                if (name.isNotEmpty() && price > 0) {
                    val newId = (menuItems.maxOfOrNull { it.id } ?: 0) + 1
                    menuItems.add(SellerMenuItem(newId, name, price, stock, "Makanan", true))
                    Toast.makeText(this, "$name ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Isi semua field dengan benar", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun setupFabMenu() {
        binding.fabAddMenu.setOnClickListener {
            showAddMenuDialog()
        }

        // Switch between orders and menu
        binding.btnOrders.setOnClickListener {
            binding.rvOrders.visibility = View.VISIBLE
            binding.rvMenu.visibility = View.GONE
            binding.btnOrders.setBackgroundColor(getColor(R.color.primary))
            binding.btnOrders.setTextColor(getColor(R.color.white))
            binding.btnMenu.setBackgroundColor(getColor(R.color.surface_container_high))
            binding.btnMenu.setTextColor(getColor(R.color.on_surface_secondary))
        }

        binding.btnMenu.setOnClickListener {
            binding.rvOrders.visibility = View.GONE
            binding.rvMenu.visibility = View.VISIBLE
            binding.btnMenu.setBackgroundColor(getColor(R.color.primary))
            binding.btnMenu.setTextColor(getColor(R.color.white))
            binding.btnOrders.setBackgroundColor(getColor(R.color.surface_container_high))
            binding.btnOrders.setTextColor(getColor(R.color.on_surface_secondary))
        }
    }

    private fun setupBottomNavigation() {
        binding.navHome.setOnClickListener { finish() }
        binding.navOrders.setOnClickListener { /* Already in dashboard */ }
        binding.navCart.setOnClickListener { Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show() }
        binding.navHistory.setOnClickListener { Toast.makeText(this, "History", Toast.LENGTH_SHORT).show() }
        binding.navProfile.setOnClickListener { Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show() }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

data class SellerOrder(
    val id: String,
    val customerName: String,
    val items: List<String>,
    val total: Int,
    val status: String,
    val orderTime: Long,
    val paymentMethod: String
)

data class SellerMenuItem(
    val id: Int,
    val name: String,
    val price: Int,
    val stock: Int,
    val category: String,
    val isAvailable: Boolean
)