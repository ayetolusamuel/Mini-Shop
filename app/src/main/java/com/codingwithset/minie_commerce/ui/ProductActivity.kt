package com.codingwithset.minie_commerce.ui


import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
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

    /*
    viewModel for [ProductViewModel] class
     */
    private lateinit var viewModel: ProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //ProductAdapter declare here because it accept context
        val productAdapter = ProductAdapter(this)

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


        /*
        retrieve [PagedList<Products>] via [ProductViewModel]
        if null is retrieve via [ProductViewModel] [showEmptyList] function will handle view to display
        else result is passed to [ProductAdapter]
         */
        viewModel.productList.observe(this, Observer {
            showEmptyList(it.size == 0)
            productAdapter.submitList(it)
            binding.recyclerView.apply {
                adapter = productAdapter
                layoutManager = LinearLayoutManager(context)
            }


        })


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
            binding.retry.visible()
            binding.loading.gone()

        })


        binding.root
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


}
