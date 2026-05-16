package com.example.kantinku.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemReviewBinding
import java.text.NumberFormat
import java.util.Locale

class ReviewAdapter(
    private val onItemClick: (ReviewItem) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    private var reviews = listOf<ReviewItem>()

    fun updateList(newList: List<ReviewItem>) {
        reviews = newList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(reviews[position])
        holder.itemView.setOnClickListener { onItemClick(reviews[position]) }
    }

    override fun getItemCount() = reviews.size

    class ReviewViewHolder(private val binding: ItemReviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewItem) {
            binding.tvMenuName.text = review.menuName
            binding.tvWarungName.text = review.warungName
            binding.tvDate.text = review.date
            binding.tvRating.text = String.format("%.1f", review.rating)
            binding.ratingBar.rating = review.rating
            binding.tvReview.text = review.review

            // Tags
            binding.chipContainer.removeAllViews()
            review.tags.forEach { tag ->
                val chip = com.google.android.material.chip.Chip(binding.root.context)
                chip.text = tag
                chip.isClickable = false
                chip.chipBackgroundColor = android.content.res.ColorStateList.valueOf(
                    binding.root.context.getColor(com.example.kantinku.R.color.primary_light)
                )
                chip.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.primary))
                binding.chipContainer.addView(chip)
            }
        }
    }
}