package com.example.kantinku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val menuId: Int,
    val menuName: String,
    val quantity: Int,
    val price: Int,
    val totalPrice: Int,
    val status: String = "pending",
    val paymentMethod: String = "Cash",
    val orderTime: Long = System.currentTimeMillis()
)