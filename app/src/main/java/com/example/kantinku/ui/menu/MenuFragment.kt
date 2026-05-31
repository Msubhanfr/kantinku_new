package com.example.kantinku.ui.menu

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.FragmentMenuBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var warungAdapter: WarungAdapter
    private lateinit var menuPopulerAdapter: MenuPopulerAdapter

    // Data Dummy Warung
    private val warungList = listOf(
        Warung(1, "Warung Mbok Darmi", "TERLARIS", "07:00", "17:00", "200m", R.drawable.ic_restaurant, 4.8, true),
        Warung(2, "Gudeq Bu Tjitro", "TRADISIONAL", "08:00", "15:00", "450m", R.drawable.ic_restaurant, 4.5, true),
        Warung(3, "Sate & Sop Kambing", "FAVORIT", "10:00", "21:00", "800m", R.drawable.ic_restaurant, 4.9, true),
        Warung(4, "Bakso Malang Cak Man", "TERENAK", "09:00", "18:00", "320m", R.drawable.ic_restaurant, 4.7, false),
        Warung(5, "Nasi Padang Uni", "HOMEMADE", "11:00", "14:00", "550m", R.drawable.ic_restaurant, 4.6, true)
    )

    // Data Dummy Menu Populer
    private val menuPopulerList = listOf(
        MenuPopuler(1, "Nasi Goreng Spesial", "Nasi goreng dengan telur, ayam, bakso, dan kerupuk", 18000, 12, R.drawable.ic_food),
        MenuPopuler(2, "Ayam Penyet Sambal Ijo", "Ayam ungkep dengan sambal cabe ijo pedas mantap.", 18000, 0, R.drawable.ic_food),
        MenuPopuler(3, "Gado-Gado Segar", "Sayuran segar dengan bumbu kacang pilihan", 15000, 8, R.drawable.ic_food),
        MenuPopuler(4, "Mie Ayam Bakso", "Mie ayam dengan bakso sapi dan pangsit", 17000, 15, R.drawable.ic_food),
        MenuPopuler(5, "Es Teh Manis", "Teh manis dingin segar", 5000, 50, R.drawable.ic_food)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupWarungSection()
        setupMenuPopulerSection()
        setupBottomNav()
        setupListeners()
    }

    private fun setupWarungSection() {
        warungAdapter = WarungAdapter(warungList) { warung ->
            Toast.makeText(requireContext(), "Pilih ${warung.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvWarung.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWarung.adapter = warungAdapter
    }

    private fun setupMenuPopulerSection() {
        // 🔥 UPDATE: Langsung ke CartActivity ketika klik Pesan
        menuPopulerAdapter = MenuPopulerAdapter(menuPopulerList) { menu ->
            if (menu.stock > 0) {
                val intent = Intent(requireContext(), CartActivity::class.java)
                intent.putExtra("menu_id", menu.id)
                intent.putExtra("menu_name", menu.name)
                intent.putExtra("menu_price", menu.price)
                intent.putExtra("menu_image", menu.imageRes)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "Maaf, ${menu.name} sedang habis!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvMenuPopuler.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuPopuler.adapter = menuPopulerAdapter
    }

    private fun setupBottomNav() {
        binding.navHome.setOnClickListener { navigateToHome() }
        binding.navOrders.setOnClickListener { /* Already in menu */ }
        binding.navCart.setOnClickListener { navigateToCart() }
        binding.navHistory.setOnClickListener { navigateToHistory() }
        binding.navProfile.setOnClickListener { navigateToProfile() }
    }

    private fun setupListeners() {
        binding.tvSeeAllWarung.setOnClickListener {
            Toast.makeText(requireContext(), "Lihat semua warung", Toast.LENGTH_SHORT).show()
        }

        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) {
                Toast.makeText(requireContext(), "Mencari: $query", Toast.LENGTH_SHORT).show()
            }
            true
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
        startActivity(Intent(requireContext(), CartActivity::class.java))
    }

    private fun navigateToHistory() {
        val fragment = com.example.kantinku.ui.history.HistoryFragment()
        parentFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .addToBackStack(null)
            .commit()
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

data class Warung(
    val id: Int,
    val name: String,
    val label: String,
    val openTime: String,
    val closeTime: String,
    val distance: String,
    val imageRes: Int,
    val rating: Double,
    val isOpen: Boolean
)

data class MenuPopuler(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val stock: Int,
    val imageRes: Int
)