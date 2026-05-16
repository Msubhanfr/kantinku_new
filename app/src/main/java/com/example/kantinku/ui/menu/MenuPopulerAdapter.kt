package com.example.kantinku.ui.menu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.data.entity.Menu
import com.example.kantinku.databinding.ItemMenuPopulerBinding
import com.example.kantinku.utils.CurrencyFormatter

class MenuPopulerAdapter(
    private val onItemClick: (Menu) -> Unit
) : RecyclerView.Adapter<MenuPopulerAdapter.MenuPopulerViewHolder>() {

    private var menuList = listOf<Menu>()

    fun submitList(list: List<Menu>) {
        menuList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuPopulerViewHolder {
        val binding = ItemMenuPopulerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuPopulerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuPopulerViewHolder, position: Int) {
        holder.bind(menuList[position])
        holder.itemView.setOnClickListener { onItemClick(menuList[position]) }
    }

    override fun getItemCount() = menuList.size

    class MenuPopulerViewHolder(private val binding: ItemMenuPopulerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: Menu) {
            binding.tvNumber.text = "${adapterPosition + 1}."
            binding.tvMenuName.text = menu.name
            binding.tvDescription.text = menu.description
            binding.tvPrice.text = CurrencyFormatter.format(menu.price)

            if (menu.stock <= 0) {
                binding.tvStockWarning.visibility = ViewGroup.VISIBLE
                binding.tvStockWarning.text = "STOK HABIS"
                binding.tvPrice.alpha = 0.5f
            } else {
                binding.tvStockWarning.visibility = ViewGroup.GONE
                binding.tvPrice.alpha = 1f
            }
        }
    }
}