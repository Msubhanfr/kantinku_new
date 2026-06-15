package com.example.kantinku.ui.seller

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.databinding.ItemSellerMenuBinding
import com.example.kantinku.utils.CurrencyFormatter

class SellerMenuAdapter(
    private var menus: List<SellerMenuItem>,
    private val onAction: (SellerMenuItem, String) -> Unit
) : RecyclerView.Adapter<SellerMenuAdapter.MenuViewHolder>() {

    fun updateList(newMenus: List<SellerMenuItem>) {
        menus = newMenus
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemSellerMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menus[position], onAction)
    }

    override fun getItemCount() = menus.size

    class MenuViewHolder(private val binding: ItemSellerMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: SellerMenuItem, onAction: (SellerMenuItem, String) -> Unit) {
            binding.tvMenuName.text = menu.name
            binding.tvPrice.text = CurrencyFormatter.format(menu.price)
            binding.tvStock.text = "Stok: ${menu.stock}"
            binding.tvCategory.text = menu.category

            binding.btnEdit.setOnClickListener { onAction(menu, "edit") }
            binding.btnDelete.setOnClickListener { onAction(menu, "delete") }
            binding.switchAvailable.setOnCheckedChangeListener(null)
            binding.switchAvailable.isChecked = menu.isAvailable
            binding.switchAvailable.setOnCheckedChangeListener { _, isChecked ->
                onAction(menu.copy(isAvailable = isChecked), "toggle")
            }
        }
    }
}