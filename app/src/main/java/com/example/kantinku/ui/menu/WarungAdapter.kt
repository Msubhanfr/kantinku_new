package com.example.kantinku.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemWarungBinding

class WarungAdapter(
    private val warungList: List<Warung>,
    private val onItemClick: (Warung) -> Unit
) : RecyclerView.Adapter<WarungAdapter.WarungViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarungViewHolder {
        val binding = ItemWarungBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WarungViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WarungViewHolder, position: Int) {
        holder.bind(warungList[position])
        holder.itemView.setOnClickListener { onItemClick(warungList[position]) }
    }

    override fun getItemCount() = warungList.size

    class WarungViewHolder(private val binding: ItemWarungBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(warung: Warung) {
            binding.tvWarungName.text = warung.name
            binding.tvLabel.text = warung.label
            binding.tvOpenTime.text = "${warung.openTime} - ${warung.closeTime}"
            binding.tvDistance.text = warung.distance
            binding.tvRating.text = warung.rating.toString()
        }
    }
}