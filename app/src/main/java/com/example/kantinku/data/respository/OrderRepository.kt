package com.example.kantinku.data.repository

import com.example.kantinku.data.database.OrderDao
import com.example.kantinku.data.entity.Order
import kotlinx.coroutines.flow.Flow

class OrderRepository(private val orderDao: OrderDao) {
    fun getOrdersByUser(userId: Int): Flow<List<Order>> = orderDao.getOrdersByUser(userId)
    suspend fun placeOrder(order: Order) = orderDao.insertOrder(order)
    suspend fun getTotalSpending(userId: Int): Int = orderDao.getTotalSpending(userId) ?: 0
}