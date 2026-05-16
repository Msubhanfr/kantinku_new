package com.example.kantinku.data.model

import java.util.Date

data class NotificationData(
    val id: String,
    val title: String,
    val message: String,
    val type: NotificationType,
    val orderId: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val imageUrl: String? = null
)

enum class NotificationType {
    ORDER_STATUS,    // Pesanan diterima, dimasak, siap
    PROMO,           // Promo dan diskon
    REMINDER,        // Pengingat saldo, poin
    ANNOUNCEMENT,    // Pengumuman umum
    RATING_REQUEST   // Minta rating
}

data class NotificationCount(
    val unreadCount: Int = 0
)