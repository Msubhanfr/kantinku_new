package com.example.kantinku.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kantinku.data.entity.Menu
import com.example.kantinku.data.entity.Order
import com.example.kantinku.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [User::class, Menu::class, Order::class],
    version = 2,
    exportSchema = false
)
abstract class KantinDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun menuDao(): MenuDao
    abstract fun orderDao(): OrderDao

    companion object {
        @Volatile
        private var INSTANCE: KantinDatabase? = null

        fun getInstance(context: Context): KantinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KantinDatabase::class.java,
                    "kantin_ku_db"
                ).addCallback(DatabaseCallback())
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateData(database)
                    }
                }
            }

            private suspend fun populateData(database: KantinDatabase) {
                // Sample Menu Data
                val menus = listOf(
                    Menu(name = "Nasi Goreng Special", price = 18000, stock = 20, category = "Makanan", description = "Nasi goreng dengan telur, ayam, dan bakso"),
                    Menu(name = "Mie Ayam Bakso", price = 15000, stock = 15, category = "Makanan", description = "Mie ayam dengan bakso sapi"),
                    Menu(name = "Sate Ayam (10 tusuk)", price = 25000, stock = 10, category = "Makanan", description = "Sate ayam dengan bumbu kacang"),
                    Menu(name = "Ayam Geprek", price = 17000, stock = 18, category = "Makanan", description = "Ayam geprek sambal bawang"),
                    Menu(name = "Es Teh Manis", price = 5000, stock = 50, category = "Minuman", description = "Teh manis dingin segar"),
                    Menu(name = "Es Jeruk", price = 7000, stock = 40, category = "Minuman", description = "Jeruk peras segar"),
                    Menu(name = "Kopi Hitam", price = 8000, stock = 30, category = "Minuman", description = "Kopi hitam robusta"),
                    Menu(name = "Kentang Goreng", price = 12000, stock = 25, category = "Snack", description = "Kentang goreng crispy"),
                    Menu(name = "Pisang Goreng", price = 10000, stock = 25, category = "Snack", description = "Pisang goreng crispy")
                )

                menus.forEach { menu ->
                    database.menuDao().insertMenu(menu)
                }
            }
        }
    }
}