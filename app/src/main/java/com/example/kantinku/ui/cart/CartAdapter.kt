package com.example.kantinku.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemCartBinding
import com.example.kantinku.utils.CurrencyFormatter

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChange: (CartItem, Int) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<CartItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun updateItem(item: CartItem) {
        val index = items.indexOfFirst { it.id == item.id }
        if (index != -1) {
            items = items.toMutableList().apply { set(index, item) }
            notifyItemChanged(index)
        }
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvItemName.text = item.name
            binding.tvItemPrice.text = CurrencyFormatter.format(item.price)
            binding.tvQuantity.text = item.quantity.toString()

            binding.btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    onQuantityChange(item, item.quantity - 1)
                } else {
                    onRemove(item)
                }
            }

            binding.btnPlus.setOnClickListener {
                onQuantityChange(item, item.quantity + 1)
            }

            binding.btnRemove.setOnClickListener {
                onRemove(item)
            }
        }
    }
}