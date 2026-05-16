package com.example.kantinku.data.database

import androidx.room.*
import com.example.kantinku.data.entity.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderTime DESC")
    fun getOrdersByUser(userId: Int): Flow<List<Order>>

    @Query("SELECT * FROM orders ORDER BY orderTime DESC")
    fun getAllOrders(): Flow<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order)

    @Update
    suspend fun updateOrder(order: Order): Int

    @Delete
    suspend fun deleteOrder(order: Order): Int

    @Query("SELECT SUM(totalPrice) FROM orders WHERE userId = :userId AND status = 'completed'")
    suspend fun getTotalSpending(userId: Int): Int?
}