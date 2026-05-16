package com.example.kantinku.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemPaymentOrderBinding
import com.example.kantinku.utils.CurrencyFormatter

class OrderItemsAdapter(
    private val items: List<OrderItem>
) : RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val binding = ItemPaymentOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class OrderItemViewHolder(private val binding: ItemPaymentOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.tvItemName.text = item.name
            binding.tvItemPrice.text = CurrencyFormatter.format(item.price)
            binding.tvItemQuantity.text = item.quantity
        }
    }
}