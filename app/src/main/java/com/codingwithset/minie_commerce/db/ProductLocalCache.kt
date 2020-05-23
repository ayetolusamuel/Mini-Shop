package com.codingwithset.minie_commerce.db

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.codingwithset.minie_commerce.model.Products
import com.codingwithset.minie_commerce.utils.checkInternetAccess
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 */
class ProductLocalCache(
    private val productDao: ProductDao,
    private val ioExecutor: Executor
) {

    private var _productList: MutableLiveData<List<Products>> = MutableLiveData()
    val productList get() = _productList
    private val _isNetwork: MutableLiveData<Boolean> = MutableLiveData()

    init {
        getList()
    }

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

    /**
     * Delete all products from database, on a background thread.
     */
    fun deleteAllProducts(products: List<Products>, deleteFinished: () -> Unit) {
        ioExecutor.run {
            execute {
                Log.d("ProductLocalCache", "Deleting  ${products.size} products")
                productDao.deleteProducts(productDao.productsList())
                deleteFinished()
            }
        }
    }

    /*
    get all product from cache database<room> via paging
     */
    fun getAllProduct(): DataSource.Factory<Int, Products> {
        return productDao.getAllProduct()
    }

    private fun getList() {
        ioExecutor.execute {
            _productList.postValue(productDao.productsList())
        }

    }



}
