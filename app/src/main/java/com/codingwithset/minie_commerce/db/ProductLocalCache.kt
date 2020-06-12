package com.codingwithset.minie_commerce.db

import android.util.Log
import androidx.paging.DataSource
import com.codingwithset.minie_commerce.model.Products
import java.util.concurrent.Executor


/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class ProductLocalCache(
    private val productDao: ProductDao,
    private val ioExecutor: Executor
) {




    /**
     * Insert a list of products in the database, on a background thread.
     */
    fun insert(products: List<Products>, insertFinished: () -> Unit) {
        ioExecutor.execute {
            Log.d("ProductLocalCache", "inserting ${products.size} products")
            productDao.insert(products)
            insertFinished()
        }
    }



    /*
    get all product from cache database<room> via paging
     */
    fun getAllProduct(): DataSource.Factory<Int, Products> {
        return productDao.getAllProduct()
    }



    //get all product base on the search key which is the name of product
    //if search word contain in the product name.
    fun getAllProductForFilter(name: String):DataSource.Factory<Int,Products>{
        return productDao.getAllProductForFilter(name)
    }




}
