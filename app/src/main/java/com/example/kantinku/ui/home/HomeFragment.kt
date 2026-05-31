package com.example.kantinku.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.FragmentHomeBinding
import com.example.kantinku.ui.cart.CartActivity
import com.example.kantinku.utils.SessionManager
import java.text.NumberFormat
import java.util.Locale

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var popularWarungAdapter: PopularWarungAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter

    private val popularWarungList = listOf(
        PopularWarung(1, "Warung Mbok Galak", "10-15 min", "Jawa & Prasmanan", 4.8, "200m", R.drawable.ic_restaurant, true),
        PopularWarung(2, "Ayam Geprek Juara", "5-10 min", "Ayam & Sambal", 4.9, "350m", R.drawable.ic_restaurant, true),
        PopularWarung(3, "Sate Madura Asli", "15-20 min", "Sate & Nasi", 4.7, "500m", R.drawable.ic_restaurant, true),
        PopularWarung(4, "Bakso Malang", "10-15 min", "Bakso & Mie", 4.6, "180m", R.drawable.ic_restaurant, false),
        PopularWarung(5, "Nasi Campur Spesial", "12-17 min", "Nasi & Lauk", 4.8, "420m", R.drawable.ic_restaurant, true)
    )

    private val recommendationList = listOf(
        RecommendationMenu(1, "Nasi Goreng Special", "Nasi goreng dengan telur, ayam, bakso, dan kerupuk", 18000, "FAVORITMU", R.drawable.ic_food, 4.8, 234),
        RecommendationMenu(2, "Ayam Geprek Sambal Matah", "Ayam geprek crispy dengan sambal bawang segar", 22000, "BESTSELLER", R.drawable.ic_food, 4.9, 189),
        RecommendationMenu(3, "Es Teh Jeruk Nipis", "Kesegaran alami untuk teman makan siangmu", 8000, "HOT DEAL", R.drawable.ic_food, 4.7, 456),
        RecommendationMenu(4, "Gado-Gado Spesial", "Sayur segar dengan bumbu kacang pilihan", 15000, "FAVORITMU", R.drawable.ic_food, 4.8, 167),
        RecommendationMenu(5, "Mie Ayam Bakso", "Mie ayam dengan bakso sapi dan pangsit goreng", 17000, "PROMO", R.drawable.ic_food, 4.6, 198)
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupUserGreeting()
        setupPopularWarung()
        setupRecommendations()
        setupListeners()
    }

    private fun setupUserGreeting() {
        val username = sessionManager.getUsername()
        val greetingName = when {
            username == "admin" -> "Admin"
            username == "student1" -> "Arya"
            username.isNotEmpty() -> username
            else -> "Food Lover"
        }
        binding.tvGreeting.text = "Halo, $greetingName!"
    }

    private fun setupPopularWarung() {
        popularWarungAdapter = PopularWarungAdapter(popularWarungList) { warung ->
            Toast.makeText(requireContext(), "Memilih ${warung.name}", Toast.LENGTH_SHORT).show()
        }
        binding.rvPopularWarung.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularWarung.adapter = popularWarungAdapter
    }

    private fun setupRecommendations() {
        // 🔥 UPDATE: Langsung ke CartActivity ketika klik Pesan
        recommendationAdapter = RecommendationAdapter(recommendationList) { menu ->
            val intent = Intent(requireContext(), CartActivity::class.java)
            intent.putExtra("menu_id", menu.id)
            intent.putExtra("menu_name", menu.name)
            intent.putExtra("menu_price", menu.price)
            intent.putExtra("menu_image", menu.imageRes)
            startActivity(intent)
        }
        binding.rvRecommendations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecommendations.adapter = recommendationAdapter
    }

    private fun setupListeners() {
        binding.btnClaimPromo.setOnClickListener {
            showPromoDialog()
        }

        binding.root.findViewById<TextView>(R.id.tvSeeAllWarung)?.setOnClickListener {
            Toast.makeText(requireContext(), "Lihat semua warung", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPromoDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("🎉 Special Promo!")
            .setMessage("Diskon 10% untuk pesanan pertama Anda!\n\nGunakan kode: KANTIN10")
            .setPositiveButton("Klaim Sekarang") { _, _ ->
                Toast.makeText(requireContext(), "Kode promo: KANTIN10", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Nanti Saja", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class PopularWarung(
    val id: Int,
    val name: String,
    val estimatedTime: String,
    val cuisineType: String,
    val rating: Double,
    val distance: String,
    val imageRes: Int,
    val isOpen: Boolean
)

data class RecommendationMenu(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val type: String,
    val imageRes: Int,
    val rating: Double,
    val soldCount: Int
)