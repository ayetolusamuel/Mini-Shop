
package com.codingwithset.minie_commerce.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class Products(

    @PrimaryKey @field:SerializedName("id") var id: String,
    @field:SerializedName("name") var name: String,
    @field:SerializedName("regular_price") var regular_price: String,
    @field:SerializedName("sale_price") var sale_price: String,
    @field:SerializedName("weight") var weight: String?,
    @field:SerializedName("stock_status") var stock_status: String?,
    @field:SerializedName("stock_quantity") val stock_quantity: Int?,
    @field:SerializedName("average_rating") val average_rating: String?,
    @field:SerializedName("rating_count") val rating_count: Int?,
    @field:SerializedName("short_description") val short_description: String,
    @field:SerializedName("sku") val sku: String,
    @field:SerializedName("related_ids") val related_ids: List<Int>,
    @field:SerializedName("permalink") val permalink_link: String,
    @field:SerializedName("images") val images: List<Image>


)
@Entity
class Image(@PrimaryKey @field:SerializedName("src") val src: String)















