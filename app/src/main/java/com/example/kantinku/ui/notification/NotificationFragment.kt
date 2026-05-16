package com.example.kantinku.ui.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kantinku.R
import com.example.kantinku.data.model.NotificationData
import com.example.kantinku.databinding.FragmentNotificationBinding
import com.example.kantinku.ui.order.OrderStatusActivity
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationViewModel
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = NotificationViewModel()

        setupToolbar()
        setupRecyclerView()
        observeNotifications()
        setupListeners()
    }

    private fun setupToolbar() {
        binding.toolbar.title = "Notifikasi"
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(requireContext(), R.color.on_surface))

        // Mark all read button
        binding.toolbar.inflateMenu(R.menu.notification_menu)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_mark_all_read -> {
                    viewModel.markAllAsRead()
                    Toast.makeText(requireContext(), "Semua notifikasi ditandai telah dibaca", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter(
            onItemClick = { notification ->
                handleNotificationClick(notification)
            },
            onDeleteClick = { notification ->
                showDeleteConfirmation(notification)
            }
        )
        binding.rvNotifications.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotifications.adapter = adapter
    }

    private fun handleNotificationClick(notification: NotificationData) {
        // Mark as read
        viewModel.markAsRead(notification.id)

        // Navigate based on notification type
        when (notification.type) {
            NotificationType.ORDER_STATUS -> {
                if (notification.orderId != null) {
                    val intent = android.content.Intent(requireContext(), OrderStatusActivity::class.java)
                    intent.putExtra("order_id", notification.orderId)
                    startActivity(intent)
                }
            }
            NotificationType.PROMO -> {
                Toast.makeText(requireContext(), "Kode promo: KANTIN10", Toast.LENGTH_SHORT).show()
            }
            NotificationType.RATING_REQUEST -> {
                Toast.makeText(requireContext(), "Ayo beri rating untuk warung favoritmu!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(requireContext(), notification.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDeleteConfirmation(notification: NotificationData) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Hapus Notifikasi")
            .setMessage("Apakah Anda yakin ingin menghapus notifikasi ini?")
            .setPositiveButton("Hapus") { _, _ ->
                viewModel.deleteNotification(notification.id)
                Toast.makeText(requireContext(), "Notifikasi dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun observeNotifications() {
        lifecycleScope.launch {
            viewModel.notifications.collect { notifications ->
                adapter.submitList(notifications)

                if (notifications.isEmpty()) {
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.rvNotifications.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.rvNotifications.visibility = View.VISIBLE
                }
            }
        }

        lifecycleScope.launch {
            viewModel.unreadCount.collect { count ->
                if (count > 0) {
                    binding.toolbar.title = "Notifikasi ($count)"
                } else {
                    binding.toolbar.title = "Notifikasi"
                }
            }
        }
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            Toast.makeText(requireContext(), "Notifikasi diperbarui", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}