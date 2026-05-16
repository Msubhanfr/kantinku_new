package com.example.kantinku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val password: String,
    val email: String,
    val fullName: String = "",
    val isSeller: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)