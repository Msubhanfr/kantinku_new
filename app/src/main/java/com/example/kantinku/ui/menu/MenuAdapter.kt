package com.example.kantinku.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.data.entity.Menu
import com.example.kantinku.databinding.ItemMenuBinding
import com.example.kantinku.utils.CurrencyFormatter

class MenuAdapter(
    private val onItemClick: (Menu) -> Unit,
    private val onOrderClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private var menuList = listOf<Menu>()

    fun submitList(list: List<Menu>) {
        menuList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
        holder.itemView.setOnClickListener { onItemClick(menuList[position]) }
        holder.binding.btnOrder.setOnClickListener { onOrderClick(menuList[position]) }
    }

    override fun getItemCount() = menuList.size

    class MenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.tvMenuName.text = menu.name
            binding.tvMenuPrice.text = CurrencyFormatter.format(menu.price)
            binding.tvMenuStock.text = "Stok: ${menu.stock}"
            binding.tvMenuDescription.text = menu.description
        }
    }
}