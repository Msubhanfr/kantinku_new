package com.example.kantinku.data.repository

import com.example.kantinku.data.model.NotificationData
import com.example.kantinku.data.model.NotificationType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationRepository {

    // State untuk notifikasi
    private val _notifications = MutableStateFlow<List<NotificationData>>(emptyList())
    val notifications: StateFlow<List<NotificationData>> = _notifications.asStateFlow()

    private val _unreadCount = MutableStateFlow(0)
    val unreadCount: StateFlow<Int> = _unreadCount.asStateFlow()

    init {
        loadDummyNotifications()
    }

    private fun loadDummyNotifications() {
        val dummyNotifications = listOf(
            NotificationData(
                id = "1",
                title = "Pesanan Diterima",
                message = "Pesanan #ORD-88291 telah diterima oleh Warung Bu Sumi",
                type = NotificationType.ORDER_STATUS,
                orderId = "ORD-88291",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 5, // 5 menit lalu
                isRead = false
            ),
            NotificationData(
                id = "2",
                title = "Sedang Dimasak",
                message = "Chef sedang menyiapkan Nasi Goreng Spesial untukmu!",
                type = NotificationType.ORDER_STATUS,
                orderId = "ORD-88291",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 3,
                isRead = false
            ),
            NotificationData(
                id = "3",
                title = "🔥 Flash Sale!",
                message = "Diskon 20% untuk Nasi Goreng Gila hanya 1 jam lagi!",
                type = NotificationType.PROMO,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 30,
                isRead = false
            ),
            NotificationData(
                id = "4",
                title = "Saldo Kampus Menipis",
                message = "Saldo Anda tersisa Rp 15.500. Yuk top up sekarang!",
                type = NotificationType.REMINDER,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60,
                isRead = true
            ),
            NotificationData(
                id = "5",
                title = "Pesanan Siap Diambil",
                message = "Pesanan #ORD-88291 sudah siap! Ambil di counter 3",
                type = NotificationType.ORDER_STATUS,
                orderId = "ORD-88291",
                timestamp = System.currentTimeMillis() - 1000 * 60 * 2,
                isRead = false
            ),
            NotificationData(
                id = "6",
                title = "Rating Yuk!",
                message = "Bagaimana pengalamanmu dengan Warung Bu Sumi? Beri rating sekarang!",
                type = NotificationType.RATING_REQUEST,
                timestamp = System.currentTimeMillis() - 1000 * 60 * 60 * 2,
                isRead = true
            )
        )

        _notifications.value = dummyNotifications
        updateUnreadCount()
    }

    private fun updateUnreadCount() {
        _unreadCount.value = _notifications.value.count { !it.isRead }
    }

    fun addNotification(notification: NotificationData) {
        val currentList = _notifications.value.toMutableList()
        currentList.add(0, notification)
        _notifications.value = currentList
        updateUnreadCount()
    }

    fun markAsRead(notificationId: String) {
        val currentList = _notifications.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == notificationId }
        if (index != -1) {
            currentList[index] = currentList[index].copy(isRead = true)
            _notifications.value = currentList
            updateUnreadCount()
        }
    }

    fun markAllAsRead() {
        val currentList = _notifications.value.map { it.copy(isRead = true) }
        _notifications.value = currentList
        _unreadCount.value = 0
    }

    fun deleteNotification(notificationId: String) {
        val currentList = _notifications.value.filter { it.id != notificationId }
        _notifications.value = currentList
        updateUnreadCount()
    }

    // Simulasi notifikasi real-time
    suspend fun simulateRealtimeNotification() {
        delay(15000) // 15 detik setelah aplikasi dibuka
        val newNotification = NotificationData(
            id = System.currentTimeMillis().toString(),
            title = "✨ Promo Spesial untukmu!",
            message = "Gunakan kode KANTIN10 untuk diskon 10% pesanan pertama",
            type = NotificationType.PROMO,
            timestamp = System.currentTimeMillis(),
            isRead = false
        )
        addNotification(newNotification)
    }
}