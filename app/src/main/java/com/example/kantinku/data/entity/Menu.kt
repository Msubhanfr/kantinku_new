package com.example.kantinku.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class Menu(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val price: Int,
    val stock: Int,
    val category: String = "Makanan",
    val description: String = "",
    val imageUrl: String = "",
    val isAvailable: Boolean = true
)