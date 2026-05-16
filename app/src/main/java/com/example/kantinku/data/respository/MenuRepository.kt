package com.example.kantinku.data.repository

import com.example.kantinku.data.database.MenuDao
import com.example.kantinku.data.entity.Menu
import kotlinx.coroutines.flow.Flow

class MenuRepository(private val menuDao: MenuDao) {
    fun getAllMenu(): Flow<List<Menu>> = menuDao.getAllMenu()
    fun getMenuByCategory(category: String): Flow<List<Menu>> = menuDao.getMenuByCategory(category)

    suspend fun addMenu(menu: Menu) = menuDao.insertMenu(menu)
    suspend fun updateMenu(menu: Menu) = menuDao.updateMenu(menu)
    suspend fun deleteMenu(menu: Menu) = menuDao.deleteMenu(menu)
    suspend fun reduceStock(menuId: Int, qty: Int) = menuDao.reduceStock(menuId, qty)
    suspend fun getMenuById(menuId: Int): Menu? = menuDao.getMenuById(menuId)
}