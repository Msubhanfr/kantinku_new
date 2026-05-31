package com.example.kantinku.data.database

import androidx.room.*
import com.example.kantinku.data.entity.CartItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    fun getCartItems(userId: Int): Flow<List<CartItemEntity>>

    @Insert
    suspend fun addToCart(cartItem: CartItemEntity)

    @Update
    suspend fun updateCartItem(cartItem: CartItemEntity)

    @Delete
    suspend fun removeFromCart(cartItem: CartItemEntity)

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Int)

    @Query("SELECT SUM(price * quantity) FROM cart_items WHERE userId = :userId")
    suspend fun getCartTotal(userId: Int): Int?
}