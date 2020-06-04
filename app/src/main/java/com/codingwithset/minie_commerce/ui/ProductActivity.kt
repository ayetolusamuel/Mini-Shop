package com.codingwithset.minie_commerce.ui


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.codingwithset.minie_commerce.Injection
import com.codingwithset.minie_commerce.R
import com.codingwithset.minie_commerce.databinding.ActivityMainBinding
import com.codingwithset.minie_commerce.model.Products
import com.codingwithset.minie_commerce.utils.*
import kotlinx.android.synthetic.main.activity_main.*


class ProductActivity : AppCompatActivity() {

    //this handle the viewBinding to avoid findByView
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    //keep track of products list
    private var productLists: PagedList<Products>? = null

    //[ProductAdapter] field, am using backing scope because [ProductAdapter] accept context
    private var _productAdapter: ProductAdapter? = null
    private val productAdapter get() = _productAdapter!!

    /*
    viewModel for [ProductViewModel] class
     */
    private lateinit var viewModel: ProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // get the view model
        viewModel = ViewModelProvider(this, Injection.provideViewModelFactory(this)).get(
            ProductViewModel::class.java
        )


        //add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(decoration)


        //if the call icon is click [callSeller] is called.
        call.setOnClickListener {
            it.callSeller()
        }


        chat.setOnClickListener {
            if (checkInternetAccess()) {
                chatSeller()
            } else {
                message("Check internet connection!!!")
            }

        }


        //function that handle retrieval of products list
        getProductList()


        /*
     action perform if retry is clicked,
    if there is internet access [refresh] function,
    else loading view visibility is set to gone & swipeRefreshing is set to false, so that the swipeRefresh dialog will be visible
      */
        retry.setOnClickListener {
            if (checkInternetAccess()) {
                retry.gone()
                refresh()

            } else {
                loading.gone()
                retry.visible()
            }

        }


        /*
        network error from [ProductResult]
         */
        viewModel.networkErrors.observe(this, Observer {
            Toast.makeText(this, "Wooops $it", Toast.LENGTH_SHORT).show()
            retry.visible()
            emptyList.text = getString(R.string.check_internet)
            loading.gone()

            //the retry visibility should be set to gone in as much the product list is not empty
            if (productLists!!.isNotEmpty())
                retry.gone()

        })

        //function that handle swipe refresh
        swipeAction()


        /**
         * this listener handle the searchview to know if focused or not
         * if focused nothing swipe refresh should set to false
         * else call [swipeAction] function
         */
        search_product.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                swipeRefresh.setOnRefreshListener {
                    swipeRefresh.isRefreshing = false
                }
            } else {
                swipeAction()


            }
        }

        binding.root
    }

    /**the [relLayout] is set to gone before swipe refresh is performed
     * if there is internet access the [refresh] function is called.
     * else loading is gone,swipe refreshing is set to false
     * toast is display to user
     */
    private fun swipeAction() {
        relLayout.gone()
        retry.gone()
        swipeRefresh.setOnRefreshListener {
            if (checkInternetAccess()) {
                refresh()

            } else {
                loading.gone()
                swipeRefresh.isRefreshing = false
                message("Check internet connection!!!")
            }


        }
    }

    /*
       retrieve [PagedList<Products>] via [ProductViewModel]
       if null is retrieve via [ProductViewModel] [showEmptyList] function will handle view to display
       else result is passed to [ProductAdapter]
       searchview should also visible the product list is not empty
        */
    private fun getProductList() {
        viewModel.productList.observe(this, Observer {
            _productAdapter = ProductAdapter(this)
            showEmptyList(it.size == 0)

            productLists = it
            productAdapter.submitList(it)
            initRecyclerview()
            initSearchView()

        })
    }

    private fun initRecyclerview() {
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    /*
    if the list of the products is empty, this function will handle the view to display
    the [visible] make view visibility to visible & [gone] make view visibility to gone
     */
    private fun showEmptyList(show: Boolean) {
        if (show) {
            emptyList.visible()
            recyclerView.gone()
            loading.visible()
            call.gone()
            chat.gone()
            binding.searchProduct.gone()
        } else {
            emptyList.gone()
            recyclerView.visible()
            call.visible()
            chat.visible()
            loading.gone()
            binding.searchProduct.visible()

        }
    }

    private fun refresh() {
        viewModel.networkStates.loadToRefresh().observe(this, Observer {
            if (it.status == NetworkState.LOADING.status) {
                relLayout.gone()
                emptyList.text = getString(R.string.retrieve_data)
                loading.visible()

            }
            if (it.status == NetworkState.LOADED.status) {
                swipeRefresh.isRefreshing = false
            }
        })
    }


    private fun initSearchView() {

        binding.searchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            /*
                trigger as user search for product name
                if data retrieve base on search keyword is empty the [submitList] should be set to null
                and the recyclerview should be update to result base on search

             */
            override fun onQueryTextChange(query: String): Boolean {
                if (query.trim().isNotEmpty()) {
                    swipeRefresh.isRefreshing = false
                    relLayout.gone()

                    // appending '%' so we can allow other characters to be before and after the query string
                    viewModel.setFilterName("%${query}%")

                    viewModel.dataList.observe(this@ProductActivity, Observer {
                        productAdapter.submitList(null)
                        productAdapter.submitList(it)

                        //if product is not find base on the search keyword the [binding.relLayout] is visible
                        //hide keyboard
                        if (productAdapter.itemCount == 0) {
//
//
//                        if (query.length >= 0) {
//                            relLayout.gone()
//                            swipeRefresh.setOnRefreshListener { swipeRefresh.isRefreshing = false }
//                        }
//                        swipeRefresh.setOnRefreshListener { swipeRefresh.isRefreshing = false }

                            relLayout.visible()
                            call.gone()
                            chat.gone()

                            please_try_again.text =
                                getString(R.string.error_message, query)
                            try {
                                hideKeyboard()
                            } catch (exc: Exception) {
                                exc.printStackTrace()
                            }

                        } else {
                            relLayout.gone()
                            call.visible()
                            chat.visible()
                        }
                    })
                } else {
                    getProductList()
                    relLayout.gone()
                }

                return false
            }
        })
    }


}
