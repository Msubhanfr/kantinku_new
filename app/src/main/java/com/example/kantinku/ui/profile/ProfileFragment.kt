package com.example.kantinku.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.databinding.FragmentProfileBinding
import com.example.kantinku.ui.login.LoginActivity
import com.example.kantinku.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager
    private lateinit var reviewAdapter: ReviewAdapter

    // Dummy data for reviews
    private val reviews = mutableListOf<ReviewItem>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())

        setupUserProfile()
        setupStats()
        setupRatingSection()
        setupReviewHistory()
        setupBottomNav()
        setupLogout()
        loadDummyReviews()
    }

    private fun setupUserProfile() {
        val username = sessionManager.getUsername()
        val userId = sessionManager.getUserId()

        binding.tvUserName.text = when (username) {
            "admin" -> "Admin Kantin"
            "student1" -> "Arya Wijaya"
            else -> username
        }

        binding.tvUserNim.text = when (username) {
            "admin" -> "Admin • Kantin-Ku"
            "student1" -> "2108561023 • Informatika"
            else -> "$userId • Member"
        }

        binding.tvUserEmail.text = when (username) {
            "admin" -> "admin@kantinku.com"
            "student1" -> "arya.wijaya@student.univ.ac.id"
            else -> "user@kantinku.com"
        }
    }

    private fun setupStats() {
        binding.tvTotalOrders.text = "128"
        binding.tvLoyaltyPoints.text = "2,450"
    }

    private fun setupRatingSection() {
        binding.btnRateNow.setOnClickListener {
            showRatingDialog()
        }

        // Set rating value (dummy)
        binding.ratingDisplay.rating = 4.8f

        binding.tvReviewsCount.text = "12"
        binding.tvAverageRating.text = "4.8"

        // Rate button for warung
        binding.btnRateWarung.setOnClickListener {
            showWarungRatingDialog()
        }
    }

    private fun showRatingDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rating, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val etReview = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etReview)

        AlertDialog.Builder(requireContext())
            .setTitle("Berikan Ulasan")
            .setMessage("Puas dengan Ayam Gepreknya? Berikan ulasan untuk Warung Bu Sastro")
            .setView(dialogView)
            .setPositiveButton("Kirim") { _, _ ->
                val rating = ratingBar.rating
                val review = etReview.text.toString()

                if (review.isNotEmpty()) {
                    saveReview(rating, review)
                } else {
                    Toast.makeText(requireContext(), "Tulis ulasan Anda", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showWarungRatingDialog() {
        val warungList = arrayOf("Warung Bu Sastro", "Kantin Teknik", "Warung Mbok Galak", "Gudeq Bu Tjitro")

        AlertDialog.Builder(requireContext())
            .setTitle("Pilih Warung")
            .setItems(warungList) { _, which ->
                val warungName = warungList[which]
                showRatingForWarung(warungName)
            }
            .show()
    }

    private fun showRatingForWarung(warungName: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_rating, null)
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
        val etReview = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etReview)

        AlertDialog.Builder(requireContext())
            .setTitle("Rating untuk $warungName")
            .setMessage("Bagaimana pengalaman Anda berbelanja di $warungName?")
            .setView(dialogView)
            .setPositiveButton("Kirim") { _, _ ->
                val rating = ratingBar.rating
                val review = etReview.text.toString()

                if (review.isNotEmpty()) {
                    saveWarungReview(warungName, rating, review)
                } else {
                    Toast.makeText(requireContext(), "Tulis ulasan Anda", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun saveReview(rating: Float, reviewText: String) {
        lifecycleScope.launch {
            // Simulate saving
            binding.progressBar.visibility = View.VISIBLE
            delay(1000)

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id"))
            val newReview = ReviewItem(
                id = reviews.size + 1,
                menuName = "Ayam Geprek Spesial",
                warungName = "Warung Bu Sastro",
                date = dateFormat.format(Date()),
                rating = rating,
                review = reviewText,
                tags = listOf("REKOMENDASI", "PEDAS MAMPUS")
            )

            reviews.add(0, newReview)
            reviewAdapter.updateList(reviews)

            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Terima kasih atas ulasannya!", Toast.LENGTH_SHORT).show()

            // Update stats
            updateReviewStats()
        }
    }

    private fun saveWarungReview(warungName: String, rating: Float, reviewText: String) {
        lifecycleScope.launch {
            binding.progressBar.visibility = View.VISIBLE
            delay(1000)

            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale("id"))
            val newReview = ReviewItem(
                id = reviews.size + 1,
                menuName = "Menu Favorit",
                warungName = warungName,
                date = dateFormat.format(Date()),
                rating = rating,
                review = reviewText,
                tags = listOf("REKOMENDASI")
            )

            reviews.add(0, newReview)
            reviewAdapter.updateList(reviews)

            binding.progressBar.visibility = View.GONE
            Toast.makeText(requireContext(), "Ulasan untuk $warungName telah disimpan!", Toast.LENGTH_SHORT).show()

            updateReviewStats()
        }
    }

    private fun updateReviewStats() {
        val totalReviews = reviews.size
        val averageRating = reviews.map { it.rating }.average().toFloat()

        binding.tvReviewsCount.text = totalReviews.toString()
        binding.tvAverageRating.text = String.format("%.1f", averageRating)
        binding.ratingDisplay.rating = averageRating
    }

    private fun setupReviewHistory() {
        reviewAdapter = ReviewAdapter { review ->
            // Show review detail
            showReviewDetail(review)
        }
        binding.rvReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReviews.adapter = reviewAdapter

        binding.tvSeeAllReviews.setOnClickListener {
            Toast.makeText(requireContext(), "Semua ulasan: ${reviews.size} ulasan", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showReviewDetail(review: ReviewItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(review.menuName)
            .setMessage("""
                Warung: ${review.warungName}
                Tanggal: ${review.date}
                Rating: ${review.rating} ★
                
                ${review.review}
                
                Tags: ${review.tags.joinToString(", ")}
            """.trimIndent())
            .setPositiveButton("Tutup", null)
            .show()
    }

    private fun loadDummyReviews() {
        reviews.addAll(
            listOf(
                ReviewItem(
                    id = 1,
                    menuName = "Ayam Geprek Spesial",
                    warungName = "Warung Bu Sastro",
                    date = "12 Okt 2023",
                    rating = 5.0f,
                    review = "Sambalnya juara banget! Pedasnya nendang tapi masih berasa gurihnya. Porsi nasi juga pas buat mahasiswa yang laper abis kelas.",
                    tags = listOf("REKOMENDASI", "PEDAS MAMPUS")
                ),
                ReviewItem(
                    id = 2,
                    menuName = "Es Teh Tarik",
                    warungName = "Kantin Teknik",
                    date = "05 Okt 2023",
                    rating = 4.0f,
                    review = "Enak, manisnya pas. Tapi nunggunya agak lama pas jam makan siang. Mungkin bisa ditingkatkan speed pelayanannya.",
                    tags = listOf("SEGAR", "MANIS PAS")
                ),
                ReviewItem(
                    id = 3,
                    menuName = "Nasi Campur Spesial",
                    warungName = "Warung Mbok Galak",
                    date = "28 Sep 2023",
                    rating = 4.5f,
                    review = "Rasanya enak, porsinya banyak. Sayang agak lama waktu deliverynya.",
                    tags = listOf("ENAK", "PORSI BANYAK")
                )
            )
        )
        reviewAdapter.updateList(reviews)
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
            Toast.makeText(requireContext(), "History", Toast.LENGTH_SHORT).show()
        }
        binding.navProfile.setOnClickListener {
            // Already in profile
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

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya") { _, _ ->
                    sessionManager.logout()
                    startActivity(android.content.Intent(requireContext(), LoginActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Tidak", null)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

// Data Classes
data class ReviewItem(
    val id: Int,
    val menuName: String,
    val warungName: String,
    val date: String,
    val rating: Float,
    val review: String,
    val tags: List<String>
)