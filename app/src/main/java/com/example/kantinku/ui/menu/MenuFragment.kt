package com.example.kantinku.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.FragmentMenuBinding
import com.example.kantinku.data.database.KantinDatabase
import com.example.kantinku.data.entity.Menu
import com.example.kantinku.data.repository.MenuRepository
import com.example.kantinku.utils.CurrencyFormatter
import com.example.kantinku.utils.SessionManager
import kotlinx.coroutines.launch

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var warungAdapter: WarungAdapter
    private lateinit var menuPopulerAdapter: MenuPopulerAdapter
    private lateinit var sessionManager: SessionManager
    private lateinit var menuRepository: MenuRepository

    // Data Warung
    private val warungList = listOf(
        Warung(
            id = 1,
            name = "Warung Mbok Darmi",
            label = "TERLARIS",
            openTime = "07:00",
            closeTime = "17:00",
            distance = "200m",
            imageRes = R.drawable.ic_restaurant,
            rating = 4.8
        ),
        Warung(
            id = 2,
            name = "Gudeq Bu Tjitro",
            label = "TRADISIONAL",
            openTime = "08:00",
            closeTime = "15:00",
            distance = "450m",
            imageRes = R.drawable.ic_restaurant,
            rating = 4.5
        ),
        Warung(
            id = 3,
            name = "Sate & Sop Kambing",
            label = "FAVORIT",
            openTime = "10:00",
            closeTime = "21:00",
            distance = "800m",
            imageRes = R.drawable.ic_restaurant,
            rating = 4.9
        )
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        val database = KantinDatabase.getInstance(requireContext())
        menuRepository = MenuRepository(database.menuDao())

        setupWarungSection()
        setupMenuPopulerSection()
        setupSearch()
        setupBottomNav()
    }

    private fun setupWarungSection() {
        warungAdapter = WarungAdapter(warungList) { warung ->
            Toast.makeText(requireContext(), "Pilih ${warung.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvWarung.layoutManager = LinearLayoutManager(requireContext())
        binding.rvWarung.adapter = warungAdapter

        binding.tvSeeAllWarung.setOnClickListener {
            Toast.makeText(requireContext(), "Lihat semua warung", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupMenuPopulerSection() {
        menuPopulerAdapter = MenuPopulerAdapter { menu ->
            if (menu.stock > 0) {
                Toast.makeText(requireContext(), "Pesan: ${menu.name}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Maaf, ${menu.name} sedang habis!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.rvMenuPopuler.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenuPopuler.adapter = menuPopulerAdapter

        loadMenuPopuler()
    }

    private fun loadMenuPopuler() {
        lifecycleScope.launch {
            menuRepository.getAllMenu().collect { menus ->
                // Filter menu populer (bisa berdasarkan bestseller atau limit 3)
                val popularMenus = menus.take(3)
                menuPopulerAdapter.submitList(popularMenus)
            }
        }
    }

    private fun setupSearch() {
        binding.etSearch.setOnEditorActionListener { _, _, _ ->
            val query = binding.etSearch.text.toString()
            if (query.isNotEmpty()) {
                searchMenu(query)
            }
            true
        }
    }

    private fun searchMenu(query: String) {
        Toast.makeText(requireContext(), "Mencari: $query", Toast.LENGTH_SHORT).show()
    }

    private fun setupBottomNav() {
        // Bottom navigation di fragment_menu.xml sudah ada
        // Handle clicks
        binding.navHome.setOnClickListener {
            // Navigate to home
        }
        binding.navOrders.setOnClickListener {
            Toast.makeText(requireContext(), "Orders", Toast.LENGTH_SHORT).show()
        }
        binding.navCart.setOnClickListener {
            Toast.makeText(requireContext(), "Cart", Toast.LENGTH_SHORT).show()
        }
        binding.navHistory.setOnClickListener {
            Toast.makeText(requireContext(), "History", Toast.LENGTH_SHORT).show()
        }
        binding.navProfile.setOnClickListener {
            Toast.makeText(requireContext(), "Profile", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Data Classes
data class Warung(
    val id: Int,
    val name: String,
    val label: String,
    val openTime: String,
    val closeTime: String,
    val distance: String,
    val imageRes: Int,
    val rating: Double
)