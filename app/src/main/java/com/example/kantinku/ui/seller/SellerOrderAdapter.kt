package com.example.kantinku.ui.seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemSellerOrderBinding
import com.example.kantinku.utils.CurrencyFormatter
import java.text.SimpleDateFormat
import java.util.*

class SellerOrderAdapter(
    private var orders: List<SellerOrder>,
    private val onAction: (SellerOrder, String) -> Unit
) : RecyclerView.Adapter<SellerOrderAdapter.OrderViewHolder>() {

    fun updateList(newOrders: List<SellerOrder>) {
        orders = newOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemSellerOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position], onAction)
    }

    override fun getItemCount() = orders.size

    class OrderViewHolder(private val binding: ItemSellerOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: SellerOrder, onAction: (SellerOrder, String) -> Unit) {
            val dateFormat = SimpleDateFormat("HH:mm • dd/MM", Locale.getDefault())

            binding.tvOrderId.text = "#${order.id}"
            binding.tvCustomerName.text = order.customerName
            binding.tvOrderTime.text = dateFormat.format(Date(order.orderTime))
            binding.tvItems.text = order.items.joinToString("\n")
            binding.tvTotal.text = CurrencyFormatter.format(order.total)
            binding.tvPaymentMethod.text = order.paymentMethod

            when (order.status) {
                "pending" -> {
                    binding.tvStatus.text = "Menunggu"
                    binding.tvStatus.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.tertiary))

                    binding.btnAccept.visibility = ViewGroup.VISIBLE
                    binding.btnReady.visibility = ViewGroup.GONE
                    binding.btnAccept.setOnClickListener { onAction(order, "accept") }
                    binding.btnReject.setOnClickListener { onAction(order, "reject") }
                }
                "processing" -> {
                    binding.tvStatus.text = "Dimasak"
                    binding.tvStatus.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.primary))

                    binding.btnAccept.visibility = ViewGroup.GONE
                    binding.btnReady.visibility = ViewGroup.VISIBLE
                    binding.btnReady.setOnClickListener { onAction(order, "ready") }
                    binding.btnReject.visibility = ViewGroup.GONE
                }
                "ready" -> {
                    binding.tvStatus.text = "Siap Diambil"
                    binding.tvStatus.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.success))

                    binding.btnAccept.visibility = ViewGroup.GONE
                    binding.btnReady.visibility = ViewGroup.GONE
                    binding.btnReject.visibility = ViewGroup.GONE
                }
                "completed" -> {
                    binding.tvStatus.text = "Selesai"
                    binding.tvStatus.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.success))

                    binding.btnAccept.visibility = ViewGroup.GONE
                    binding.btnReady.visibility = ViewGroup.GONE
                    binding.btnReject.visibility = ViewGroup.GONE
                }
                "rejected" -> {
                    binding.tvStatus.text = "Ditolak"
                    binding.tvStatus.setTextColor(binding.root.context.getColor(com.example.kantinku.R.color.error))

                    binding.btnAccept.visibility = ViewGroup.GONE
                    binding.btnReady.visibility = ViewGroup.GONE
                    binding.btnReject.visibility = ViewGroup.GONE
                }
            }
        }
    }
}