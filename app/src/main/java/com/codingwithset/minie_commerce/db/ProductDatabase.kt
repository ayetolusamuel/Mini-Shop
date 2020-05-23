
package com.codingwithset.minie_commerce.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

import com.codingwithset.minie_commerce.model.Converter
import com.codingwithset.minie_commerce.model.Image
import com.codingwithset.minie_commerce.model.Products

/**
 * Database schema that holds the list of products.
 */
@Database(
    entities = [Products::class, Image::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converter::class)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {

        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getInstance(context: Context): ProductDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ProductDatabase::class.java, "products.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
