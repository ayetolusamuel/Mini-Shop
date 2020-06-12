package com.codingwithset.minie_commerce.api

import android.util.Log
import com.codingwithset.minie_commerce.model.Products
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private const val TAG = "ApiService"

/*
this function query web service for data via retrofit library
if data retrieve successful i.e the [response.isSuccessful], meaning that data is retrieve success
else [onFailure] will handle the error
 */
fun getProductsResult(
    service: ProductService,
    customer_key: String,
    customer_secret: String,
    page: Int,
    itemsPerPage: Int,
    onSuccess: (products: List<Products>) -> Unit,
    onError: (error: String) -> Unit
) {


    service.getProducts(customer_key, customer_secret, page, itemsPerPage).enqueue(
        object : Callback<List<Products>> {
            override fun onFailure(call: Call<List<Products>>?, t: Throwable) {
                Log.d(TAG, "Error ${t.message}")
                onError(t.message ?: "unknown error")
            }

            override fun onResponse(
                call: Call<List<Products>>?,
                response: Response<List<Products>>
            ) {
                Log.d(TAG, "got a response $response")
                if (response.isSuccessful) {
                    val products: List<Products> = response.body()!!
                    onSuccess(products)
                } else {
                    onError(response.errorBody()?.string() ?: "Unknown error")
                }
            }
        }
    )
}

/**
 * Website<ecommerce built with woocommerce plugin> API communication setup via Retrofit.
 */
interface ProductService {
    /**
     * Get products from web service
     */
    @GET("products")
    fun getProducts(
        @Query("consumer_key") consumer_key: String,
        @Query("consumer_secret") consumer_secret: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Call<List<Products>>

    companion object {
        private const val BASE_URL = "https://akwe.com.ng/wp-json/wc/v3/"

        fun create(): ProductService {
            val logger = HttpLoggingInterceptor()
            logger.level = Level.BASIC

            val client = OkHttpClient.Builder()
                .connectTimeout(60000, TimeUnit.SECONDS)
                .writeTimeout(120000, TimeUnit.SECONDS)
                .readTimeout(120000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ProductService::class.java)
        }
    }
}


















