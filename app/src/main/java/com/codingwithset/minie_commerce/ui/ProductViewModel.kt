package com.codingwithset.minie_commerce.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.codingwithset.minie_commerce.model.ProductResult
import com.codingwithset.minie_commerce.model.Products
import com.codingwithset.minie_commerce.data.ProductRepository


/**
 * ViewModel for the [ProductActivity] screen.
 * The [ProductViewModel] class communicate with the [ProductRepository] to get the data.
 */
class ProductViewModel(private val repository: ProductRepository) : ViewModel() {






    /*
    this field handle the [ProductResult]
     */
    private val productResult = MutableLiveData<ProductResult>()

    private val _networkStates = repository.networkState

    /*
    this field will be used in [ProductActivity] for refresh purpose
    When user swipe to refresh, the networkState will help us to perform some task with the view to be displayed.
     */
    val networkStates get() = _networkStates

    /*
    when [ProductViewModel] is called in [ProductActivity]
    this function [getData] is called in initialisation
     */
    init {
        getData()
    }


/*
retrieve [ProductResult] and the result is pass to data field
note that the result pass to data field declare in ProductResult = [LiveData<PagedList<Products>>]

 */
    val productList: LiveData<PagedList<Products>> = Transformations.switchMap(productResult) {
        it.data

    }
    /*
    This handle the network error,incase if occur while PagedList.BoundaryCallBack trying to quering the webservice
    the value retrieve which is [LiveData<String>] is passed to [networkErrors]
     */
    val networkErrors: LiveData<String> = Transformations.switchMap(productResult) {
        it.networkErrors
    }

    /*
    the function help to retrieve products and network error base on [ProductResult] class
    the result is set on the [productResult]  field
     */
    private fun getData() {
        productResult.value = repository.getProduct()
    }





}
