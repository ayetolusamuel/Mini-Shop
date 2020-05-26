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
        binding.recyclerView.addItemDecoration(decoration)


        //if the call icon is click [callSeller] is called.
        binding.call.setOnClickListener {
            it.callSeller()
        }

        //function that handle retrieval of products list
        getProductList()


        /*
        action perform if swipeRefresh happen,
       if there is internet access [refresh] function,
       else loading view visibility is set to gone & swipeRefreshing is set to false, so that the swipeRefresh dialog will be visible
         */

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            if (checkInternetAccess()) {
                refresh()

            } else {
                binding.loading.gone()
                binding.swipeRefresh.isRefreshing = false
            }


        }

        /*
     action perform if retry is clicked,
    if there is internet access [refresh] function,
    else loading view visibility is set to gone & swipeRefreshing is set to false, so that the swipeRefresh dialog will be visible
      */
        binding.retry.setOnClickListener {
            if (checkInternetAccess()) {
                binding.retry.gone()
                refresh()

            } else {
                binding.loading.gone()
                binding.retry.visible()
            }

        }


        /*
        network error from [ProductResult]
         */
        viewModel.networkErrors.observe(this, Observer {
            println("whoops $it")
            Toast.makeText(this, "Wooops $it", Toast.LENGTH_SHORT).show()
            binding.retry.visible()
            binding.emptyList.text = getString(R.string.check_internet)
            binding.loading.gone()

            //the retry visibility should be set to gone in as much the product list is not empty
            if (productLists!!.isNotEmpty())
                binding.retry.gone()

        })


        binding.root
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
            binding.searchProduct.visible()
            initSearchView()

        })
    }

    private fun initRecyclerview() {
        binding.recyclerView.apply {
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
            binding.emptyList.visible()
            binding.recyclerView.gone()
            binding.loading.visible()
        } else {
            binding.emptyList.gone()
            binding.recyclerView.visible()
            binding.loading.gone()
        }
    }

    private fun refresh() {
        viewModel.networkStates.loadToRefresh().observe(this, Observer {
            if (it.status == NetworkState.LOADING.status) {
                binding.emptyList.text = getString(R.string.retrieve_data)
                binding.loading.visible()

            }
            if (it.status == NetworkState.LOADED.status) {
                binding.swipeRefresh.isRefreshing = false
            }
        })
    }

    private fun initSearchView() {

        binding.searchProduct.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                if (query.trim().isNotEmpty()) {
                    viewModel.setFilterName("%${query}%")

                    viewModel.teamAllList.observe(this@ProductActivity, Observer {
                        productAdapter.submitList(null)
                        productAdapter.submitList(it)

                        if (productAdapter.itemCount == 0) {
                            binding.relLayout.visible()
                            binding.checkProductNameTextView.text =
                                getString(R.string.error_message, query)
                            hideKeyboard()
                        } else {
                            binding.relLayout.gone()
                        }
                    })
                }
                else {
                    getProductList()
                    binding.relLayout.gone()
                }

                return false
            }
        })
    }


}
