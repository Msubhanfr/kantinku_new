package com.example.kantinku.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemPopularWarungBinding

class PopularWarungAdapter(
    private val warungList: List<PopularWarung>
) : RecyclerView.Adapter<PopularWarungAdapter.WarungViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarungViewHolder {
        val binding = ItemPopularWarungBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WarungViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarungViewHolder, position: Int) {
        holder.bind(warungList[position])
    }

    override fun getItemCount() = warungList.size

    class WarungViewHolder(private val binding: ItemPopularWarungBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(warung: PopularWarung) {
            binding.tvWarungName.text = warung.name
            binding.tvEstimatedTime.text = warung.estimatedTime
            binding.tvCuisineType.text = warung.cuisineType
            binding.tvRating.text = warung.rating.toString()
        }
    }
}