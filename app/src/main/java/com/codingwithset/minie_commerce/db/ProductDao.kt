

package com.codingwithset.minie_commerce.db

import androidx.paging.DataSource
import androidx.room.*
import com.codingwithset.minie_commerce.model.Products

/**
 * Room data access object for accessing the [Products] table.
 */
@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Products>)


    @Query("SELECT * FROM products ORDER BY name")
    fun getAllProduct(): DataSource.Factory<Int, Products>

    @Delete()
    fun deleteProducts(products: List<Products>)

    @Query("SELECT * FROM products")
    fun productsList(): List<Products>

    @Query("SELECT * FROM products WHERE name LIKE :name ORDER BY  name ASC")
    fun getAllProductForFilter(name: String): DataSource.Factory<Int,Products>





}
