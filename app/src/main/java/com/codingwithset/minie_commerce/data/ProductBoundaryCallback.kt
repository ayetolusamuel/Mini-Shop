package com.codingwithset.minie_commerce.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.codingwithset.minie_commerce.utils.NetworkState
import com.codingwithset.minie_commerce.api.ProductService
import com.codingwithset.minie_commerce.api.getProductsResult
import com.codingwithset.minie_commerce.db.ProductLocalCache
import com.codingwithset.minie_commerce.model.Products


class ProductBoundaryCallback(
    private val service: ProductService,
    private val cache: ProductLocalCache
) : PagedList.BoundaryCallback<Products>() {




    /*
Define the number of items per page, to be retrieved by the paging library.
In the companion object, add another const val called DATABASE_PAGE_SIZE,
and set it to 50. Our PagedList will then page data from the DataSource in chunks of 20 items.
*/
    companion object {
        private const val NETWORK_PAGE_SIZE = 50

        //you can only perform  Read-Only action with the customer key
        //customer key retrieve from woocommerce plugin Rest-Api
        private const val CUSTOMER_KEY = "ck_7ae18a838d4eef3321447c5fdfd88c96020d0f48"

        //you can only perform  Read-Only action with the customer secret
        //customer secret retrieve from woocommerce plugin Rest-Api
        private const val CUSTOMER_SECRET = "cs_bed4f32d44a97c8bb6e7d23fb3a00b1c762e1969"
    }

    // keep the last requested page.
    // When the request is successful, increment the page number.
    private var lastRequestedPage = 1


    private val _networkErrors = MutableLiveData<String>()


    // LiveData of network errors.
    val networkErrors: LiveData<String>
        get() = _networkErrors

    // avoid triggering multiple requests in the same time
    private var isRequestInProgress = false


    /*
Request data from web service[website] and save into database
the error is set to [networkState] field property which is [MutableLiveData<String>]
the function return LiveData of [NetworkState]
the [deleteAllProducts] function will delete all products.
Note that if database is empty [onZeroItemsLoaded] is called, which trigger the [requestAndSaveData]
 */
    fun loadToRefresh(): LiveData<NetworkState> {
        val networkState = MutableLiveData<NetworkState>()
        networkState.value = NetworkState.LOADING

        cache.deleteAllProducts(cache.productList.value!!){
            networkState.postValue(NetworkState.LOADED)
        }


        return networkState
    }

/*
Request data from web service[website] and save into database
if data insert to database [lastRequestedPage] is increment
the [isRequestInProgress] help to avoid multiple query.
the error is set to [_networkErrors] field property which is [MutableLiveData<String>]
 */
    private fun requestAndSaveData() {

        if (isRequestInProgress) return

        isRequestInProgress = true
        getProductsResult(service, CUSTOMER_KEY,
            CUSTOMER_SECRET, lastRequestedPage, NETWORK_PAGE_SIZE, { products ->

                cache.insert(products) {
                    lastRequestedPage++
                    isRequestInProgress = false
                }
            }, { error ->
                _networkErrors.postValue(error)
                isRequestInProgress = false
            })
    }


    override fun onZeroItemsLoaded() {
        requestAndSaveData()

    }

    override fun onItemAtEndLoaded(itemAtEnd: Products) {
        requestAndSaveData()
    }
}