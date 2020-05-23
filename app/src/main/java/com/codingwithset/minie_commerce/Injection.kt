package com.codingwithset.minie_commerce

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.codingwithset.minie_commerce.api.ProductService
import com.codingwithset.minie_commerce.data.ProductBoundaryCallback
import com.codingwithset.minie_commerce.db.ProductLocalCache
import com.codingwithset.minie_commerce.data.ProductRepository
import com.codingwithset.minie_commerce.db.ProductDatabase
import com.codingwithset.minie_commerce.ui.ViewModelFactory
import java.util.concurrent.Executors


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ProductLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): ProductLocalCache {
        val database = ProductDatabase.getInstance(context)
        return ProductLocalCache(database.productDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [ProductRepository] based on the [ProductService] and a
     * [ProductLocalCache]
     */
    private fun provideProductRepository(context: Context): ProductRepository {
        return ProductRepository(ProductService.create(), provideCache(context))
    }




    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(context: Context): ViewModelProvider.Factory {
        return ViewModelFactory(provideProductRepository(context))
    }
}
