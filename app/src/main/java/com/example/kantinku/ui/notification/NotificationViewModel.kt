package com.example.kantinku.ui.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kantinku.data.model.NotificationData
import com.example.kantinku.data.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotificationViewModel : ViewModel() {

    private val repository = NotificationRepository()

    val notifications: StateFlow<List<NotificationData>> = repository.notifications
    val unreadCount: StateFlow<Int> = repository.unreadCount

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        // Simulasi notifikasi real-time
        viewModelScope.launch {
            repository.simulateRealtimeNotification()
        }
    }

    fun markAsRead(notificationId: String) {
        repository.markAsRead(notificationId)
    }

    fun markAllAsRead() {
        repository.markAllAsRead()
    }

    fun deleteNotification(notificationId: String) {
        repository.deleteNotification(notificationId)
    }

    fun getUnreadCount(): Int = repository.unreadCount.value
}