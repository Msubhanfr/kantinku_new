package com.example.kantinku.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemHistoryOrderBinding
import com.example.kantinku.utils.CurrencyFormatter

class HistoryAdapter(
    private val onReorderClick: (OrderHistory) -> Unit,
    private val onItemClick: (OrderHistory) -> Unit
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var orders = listOf<OrderHistory>()

    fun submitList(list: List<OrderHistory>) {
        orders = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(orders[position])
        holder.itemView.setOnClickListener { onItemClick(orders[position]) }
        holder.binding.btnReorder.setOnClickListener { onReorderClick(orders[position]) }
    }

    override fun getItemCount() = orders.size

    class HistoryViewHolder(private val binding: ItemHistoryOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: OrderHistory) {
            binding.tvWarungName.text = order.warungName
            binding.tvDateTime.text = "${order.date} • ${order.time}"
            binding.tvTotalPrice.text = CurrencyFormatter.format(order.totalPrice)

            // Set status color
            when (order.status) {
                "Selesai" -> {
                    binding.tvStatus.text = "✓ Selesai"
                    binding.tvStatus.setTextColor(
                        binding.root.context.getColor(com.example.kantinku.R.color.success)
                    )
                }
                "Dibatalkan" -> {
                    binding.tvStatus.text = "✗ Dibatalkan"
                    binding.tvStatus.setTextColor(
                        binding.root.context.getColor(com.example.kantinku.R.color.error)
                    )
                }
                else -> {
                    binding.tvStatus.text = order.status
                }
            }
        }
    }
}