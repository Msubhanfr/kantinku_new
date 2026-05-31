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
        holder.bind(menus[position])

        val menu = menus[position]
        holder.binding.btnEdit.setOnClickListener { onAction(menu, "edit") }
        holder.binding.btnDelete.setOnClickListener { onAction(menu, "delete") }
        holder.binding.switchAvailable.setOnCheckedChangeListener(null)
        holder.binding.switchAvailable.isChecked = menu.isAvailable
        holder.binding.switchAvailable.setOnCheckedChangeListener { _, isChecked ->
            onAction(menu.copy(isAvailable = isChecked), "toggle")
        }
    }

    override fun getItemCount() = menus.size

    class MenuViewHolder(private val binding: ItemSellerMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: SellerMenuItem) {
            binding.tvMenuName.text = menu.name
            binding.tvPrice.text = CurrencyFormatter.format(menu.price)
            binding.tvStock.text = "Stok: ${menu.stock}"
            binding.tvCategory.text = menu.category
        }
    }
}