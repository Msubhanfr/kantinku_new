package com.example.kantinku.ui.cart

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemFlashDealBinding
import com.example.kantinku.utils.CurrencyFormatter

class FlashDealAdapter(
    private val deals: List<FlashDeal>,
    private val onAddToCart: (FlashDeal) -> Unit
) : RecyclerView.Adapter<FlashDealAdapter.FlashDealViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashDealViewHolder {
        val binding = ItemFlashDealBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashDealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashDealViewHolder, position: Int) {
        holder.bind(deals[position])
        holder.binding.btnAdd.setOnClickListener {
            onAddToCart(deals[position])
        }
    }

    override fun getItemCount() = deals.size

    class FlashDealViewHolder(val binding: ItemFlashDealBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(deal: FlashDeal) {
            binding.tvDealName.text = deal.name
            binding.tvDealDescription.text = deal.description

            // Harga asli dengan efek strikethrough
            binding.tvOriginalPrice.text = CurrencyFormatter.format(deal.originalPrice)
            binding.tvOriginalPrice.paintFlags = binding.tvOriginalPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            // Harga setelah diskon
            binding.tvDealPrice.text = CurrencyFormatter.format(deal.dealPrice)
        }
    }
}