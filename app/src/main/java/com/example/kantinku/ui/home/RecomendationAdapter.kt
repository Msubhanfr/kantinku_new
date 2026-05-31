package com.example.kantinku.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.R
import com.example.kantinku.databinding.ItemRecommendationBinding
import java.text.NumberFormat
import java.util.Locale

class RecommendationAdapter(
    private val menuList: List<RecommendationMenu>,
    private val onAddToCart: (RecommendationMenu) -> Unit
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        holder.bind(menuList[position])
        holder.binding.btnAddToCart.setOnClickListener {
            onAddToCart(menuList[position])
        }
    }

    override fun getItemCount() = menuList.size

    class RecommendationViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(menu: RecommendationMenu) {
            val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))

            // Set badge type
            when (menu.type) {
                "BESTSELLER" -> {
                    binding.tvType.text = "🏆 BESTSELLER"
                    binding.tvType.setTextColor(binding.root.context.getColor(R.color.tertiary))
                }
                "HOT DEAL" -> {
                    binding.tvType.text = "🔥 HOT DEAL"
                    binding.tvType.setTextColor(binding.root.context.getColor(R.color.error))
                }
                "PROMO" -> {
                    binding.tvType.text = "🎁 PROMO"
                    binding.tvType.setTextColor(binding.root.context.getColor(R.color.success))
                }
                else -> {
                    binding.tvType.text = "⭐ FAVORITMU"
                    binding.tvType.setTextColor(binding.root.context.getColor(R.color.primary))
                }
            }

            binding.tvMenuName.text = menu.name
            binding.tvDescription.text = menu.description
            binding.tvPrice.text = formatter.format(menu.price)
            binding.tvRating.text = menu.rating.toString()
            binding.tvSold.text = "${menu.soldCount}+ terjual"
        }
    }
}