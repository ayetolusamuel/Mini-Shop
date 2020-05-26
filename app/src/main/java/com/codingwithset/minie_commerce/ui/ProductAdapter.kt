package com.codingwithset.minie_commerce.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codingwithset.currencysign.Utils
import com.codingwithset.minie_commerce.R
import com.codingwithset.minie_commerce.databinding.LayoutProductBinding
import com.codingwithset.minie_commerce.model.Products


class ProductAdapter(private val context: Context) :
    PagedListAdapter<Products, ProductViewHolder>(DIFF_CALLBACK){

    //this handle the viewBinding to avoid findByView
    private var _binding: LayoutProductBinding? = null
    private val binding get() = _binding!!


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_product, parent, false)
        _binding = LayoutProductBinding.bind(view)


        return ProductViewHolder(binding)
    }

    @SuppressLint("NewApi")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = getItem(position)

        //this listener handle event when the user tap the product,
        //this open a browser to display the product page from the website.
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(product!!.permalink_link)))
        }


        try {
            holder.name.text = product?.name

            var description = product?.short_description

            //convert the product description to String, reason
            //description retrieve from webservice is in html format.
            description = Html.fromHtml(description, Html.FROM_HTML_MODE_LEGACY).toString()
            holder.shortDescription.text = description


            if (product!!.sale_price.isEmpty()) {
                product.sale_price = "0.0"

            }
            //am using my lib to format & add naira sign to the result of price retrieve from the web service
            //for more details about the library visit https://github.com/ayetolusamuel/currencysign
            holder.actualPrice.text = Utils.inNaira(context, product.sale_price)


            //get list of images, then loop through to get the Image,
            //then retrieve the image link[String], which is used to by the glide library to display the image
            val images = product.images
            var imageValues: String? = null
            for (image in images) {
                imageValues = image.src
            }

            //glide library to handle the product image,
            //image_loading in drawable folder display if the product is not available
            Glide.with(context).load(imageValues).circleCrop().placeholder(R.drawable.image_loading)
                .into(holder.productImage)


            //if the product is outofstock the quantity should be 0
            //else display the quantity
            var quantity = product.stock_quantity.toString()
           if (product.stock_status.equals(context.getString(R.string.outofstock))) {
                quantity = "0"
               // holder.stockStatus.setTextColor(Color.parseColor("#ff9900"))
            }
            holder.quantity.text = "($quantity)"





            holder.stockStatus.text = product.stock_status
            holder.rating.rating = product.average_rating!!.toFloat()

            holder.ratingCount.text = "(${product.rating_count.toString()})"


        } catch (exception: NullPointerException) {
            Log.e("ProductAdapter", "description is null")
        }





    }

    companion object {
        private val DIFF_CALLBACK = object :
            DiffUtil.ItemCallback<Products>() {
            // Product details may have changed if reloaded from the database,
            // but ID is fixed.
            override fun areItemsTheSame(
                oldConcert: Products,
                newConcert: Products
            ) = oldConcert.id == newConcert.id

            override fun areContentsTheSame(
                oldConcert: Products,
                newConcert: Products
            ) = oldConcert == newConcert
        }
    }


}

class ProductViewHolder(binding: LayoutProductBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val name = binding.name
    val shortDescription = binding.description
    val actualPrice = binding.actualPrice
    val productImage = binding.userImage
    val stockStatus: TextView = binding.status
    val rating = binding.productRating
    val ratingCount = binding.rateCount
    val quantity = binding.quantityValue

}
