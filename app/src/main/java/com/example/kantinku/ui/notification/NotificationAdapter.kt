package com.example.kantinku.ui.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kantinku.R
import com.example.kantinku.data.model.NotificationData
import com.example.kantinku.data.model.NotificationType
import com.example.kantinku.databinding.ItemNotificationBinding
import java.text.SimpleDateFormat
import java.util.*

class NotificationAdapter(
    private val onItemClick: (NotificationData) -> Unit,
    private val onDeleteClick: (NotificationData) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private var notifications = listOf<NotificationData>()

    fun submitList(list: List<NotificationData>) {
        notifications = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notifications[position], onItemClick, onDeleteClick)
    }

    override fun getItemCount() = notifications.size

    class NotificationViewHolder(private val binding: ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            notification: NotificationData,
            onItemClick: (NotificationData) -> Unit,
            onDeleteClick: (NotificationData) -> Unit
        ) {
            binding.tvTitle.text = notification.title
            binding.tvMessage.text = notification.message
            binding.tvTime.text = formatTime(notification.timestamp)

            // Set icon berdasarkan tipe notifikasi
            val iconRes = when (notification.type) {
                NotificationType.ORDER_STATUS -> R.drawable.ic_order_status
                NotificationType.PROMO -> R.drawable.ic_promo
                NotificationType.REMINDER -> R.drawable.ic_reminder
                NotificationType.ANNOUNCEMENT -> R.drawable.ic_announcement
                NotificationType.RATING_REQUEST -> R.drawable.ic_rating
            }
            binding.ivIcon.setImageResource(iconRes)

            // Tampilkan indikator belum dibaca
            if (!notification.isRead) {
                binding.viewUnread.visibility = android.view.View.VISIBLE
                binding.cardView.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.primary_light)
                )
            } else {
                binding.viewUnread.visibility = android.view.View.GONE
                binding.cardView.setCardBackgroundColor(
                    binding.root.context.getColor(R.color.surface_container_lowest)
                )
            }

            binding.root.setOnClickListener { onItemClick(notification) }
            binding.btnDelete.setOnClickListener { onDeleteClick(notification) }
        }

        private fun formatTime(timestamp: Long): String {
            val now = Date()
            val time = Date(timestamp)
            val diff = now.time - time.time

            return when {
                diff < 60000 -> "Baru saja"
                diff < 3600000 -> "${diff / 60000} menit lalu"
                diff < 86400000 -> "${diff / 3600000} jam lalu"
                else -> SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(time)
            }
        }
    }
}