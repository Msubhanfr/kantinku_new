package com.example.kantinku.data.database

import androidx.room.*
import com.example.kantinku.data.entity.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu WHERE isAvailable = 1 ORDER BY id DESC")
    fun getAllMenu(): Flow<List<Menu>>

    @Query("SELECT * FROM menu WHERE category = :category AND isAvailable = 1")
    fun getMenuByCategory(category: String): Flow<List<Menu>>

    @Insert
    suspend fun insertMenu(menu: Menu)

    @Update
    suspend fun updateMenu(menu: Menu): Int

    @Delete
    suspend fun deleteMenu(menu: Menu): Int

    @Query("UPDATE menu SET stock = stock - :qty WHERE id = :menuId")
    suspend fun reduceStock(menuId: Int, qty: Int)

    @Query("SELECT * FROM menu WHERE id = :menuId")
    suspend fun getMenuById(menuId: Int): Menu?
}