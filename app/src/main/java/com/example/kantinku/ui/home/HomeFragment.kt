package com.example.kantinku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.databinding.FragmentHomeBinding
import com.example.kantinku.utils.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var popularWarungAdapter: PopularWarungAdapter
    private lateinit var recommendationAdapter: RecommendationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupUserGreeting()
        setupHeroSection()
        setupPopularWarung()
        setupRecommendations()
        setupFloatingDock()
    }

    private fun setupUserGreeting() {
        val username = sessionManager.getUsername()
        binding.tvGreeting.text = "Halo, ${username.takeIf { it.isNotEmpty() } ?: "Food Lover"}!"
    }

    private fun setupHeroSection() {
        // Asymmetric hero image - bleeding off edge
        binding.ivHeroImage.clipToOutline = true

        binding.btnClaimPromo.setOnClickListener {
            showPromoDialog()
        }
    }

    private fun setupPopularWarung() {
        val warungList = listOf(
            PopularWarung(
                name = "Warung Mbok Galak",
                estimatedTime = "10-15 min",
                cuisineType = "Jawa & Prasmanan",
                rating = 4.8,
                imageRes = R.drawable.img_warung1
            ),
            PopularWarung(
                name = "Ayam Geprek",
                estimatedTime = "5-10 min",
                cuisineType = "Ayam & Sambal",
                rating = 4.5,
                imageRes = R.drawable.img_warung2
            ),
            PopularWarung(
                name = "Sate Madura",
                estimatedTime = "15-20 min",
                cuisineType = "Sate & Nasi",
                rating = 4.7,
                imageRes = R.drawable.img_warung3
            )
        )

        popularWarungAdapter = PopularWarungAdapter(warungList)
        binding.rvPopularWarung.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPopularWarung.adapter = popularWarungAdapter
    }

    private fun setupRecommendations() {
        val menuList = listOf(
            RecommendationMenu(
                name = "Gado-Gado Spesial",
                description = "Sayur segar dengan bumbu kacang pilihan, disajikan dengan kerupuk dan telur",
                price = 15000,
                type = "FAVORITMU",
                imageRes = R.drawable.img_gado_gado
            ),
            RecommendationMenu(
                name = "Es Teh Jeruk Nipis",
                description = "Kesegaran alami untuk teman makan siangmu",
                price = 5000,
                type = "BESTSELLER",
                imageRes = R.drawable.img_es_teh
            ),
            RecommendationMenu(
                name = "Nasi Goreng Special",
                description = "Nasi goreng dengan telur, ayam, dan bakso",
                price = 18000,
                type = "CHEF'S PICK",
                imageRes = R.drawable.img_nasi_goreng
            )
        )

        // Asymmetric layout: first item larger
        recommendationAdapter = RecommendationAdapter(menuList) { menu ->
            addToCart(menu)
        }
        binding.rvRecommendations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecommendations.adapter = recommendationAdapter
    }

    private fun setupFloatingDock() {
        // Glassmorphism effect applied in XML
        binding.bottomDock.clipToOutline = true
    }

    private fun showPromoDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("🎉 Special Promo!")
            .setMessage("Diskon 10% untuk pesanan pertama Anda!\n\nGunakan kode: FLUID10")
            .setPositiveButton("Klaim Sekarang") { _, _ ->
                // Apply promo
            }
            .setNegativeButton("Nanti Saja", null)
            .show()
    }

    private fun addToCart(menu: RecommendationMenu) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Tambah ke Keranjang")
            .setMessage("${menu.name}\n${formatRupiah(menu.price)}\n\nIngin menambahkan ke keranjang?")
            .setPositiveButton("Tambah") { _, _ ->
                // Add to cart logic
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun formatRupiah(amount: Int): String {
        val formatter = java.text.NumberFormat.getCurrencyInstance(java.util.Locale("in", "ID"))
        return formatter.format(amount)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

data class PopularWarung(
    val name: String,
    val estimatedTime: String,
    val cuisineType: String,
    val rating: Double,
    val imageRes: Int
)

data class RecommendationMenu(
    val name: String,
    val description: String,
    val price: Int,
    val type: String,
    val imageRes: Int
)