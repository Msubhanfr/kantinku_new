package com.example.kantinku.ui.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.data.model.OrderItem
import com.example.kantinku.databinding.ItemPaymentOrderBinding
import com.example.kantinku.utils.CurrencyFormatter
import java.text.NumberFormat
import java.util.Locale

class PaymentOrderAdapter(
    private var items: List<OrderItem>
) : RecyclerView.Adapter<PaymentOrderAdapter.OrderViewHolder>() {

    fun updateList(newItems: List<OrderItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemPaymentOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    class OrderViewHolder(private val binding: ItemPaymentOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderItem) {
            binding.tvItemName.text = item.name
            binding.tvItemPrice.text = CurrencyFormatter.format(item.price)
            binding.tvItemQuantity.text = "${item.quantity}x"

            if (item.note.isNotEmpty()) {
                binding.tvItemNote.visibility = ViewGroup.VISIBLE
                binding.tvItemNote.text = "Catatan: ${item.note}"
            } else {
                binding.tvItemNote.visibility = ViewGroup.GONE
            }
        }
    }
}