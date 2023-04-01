package com.example.let_me_have_one.presentation.ui.beerList

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.let_me_have_one.Network.LiveDataInternetConnection
import com.example.let_me_have_one.R
import com.example.let_me_have_one.adapter.beerAdapter
import com.example.let_me_have_one.databinding.FragmentBeerListBinding
import com.example.let_me_have_one.other.Constants.QUERY_PAGE_SIZE
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerList : Fragment() {

    lateinit var binding: FragmentBeerListBinding
    val viewModel: BeerListViewModel by viewModels()
    private lateinit var beerAdapter: beerAdapter
    private lateinit var cld : LiveDataInternetConnection


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_beer_list, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_beer_list, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        beerAdapter.setOnItemClickListener {
            val bundle= Bundle().apply{
                putSerializable("beerModelFromRetro",it)
            }

            findNavController().navigate(
                R.id.action_beerList_to_beer_Fragment,
                bundle
            )
        }
        cld = activity?.let { LiveDataInternetConnection(it.application) }!!
        cld.observe(viewLifecycleOwner,{isConnected->
            if(isConnected){
                val snackbar = Snackbar.make(view,"Connected Succesfully",Snackbar.LENGTH_SHORT)
                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.DKGRAY)
                snackbar.show()

                viewModel.getFromRetrofit()
            }else{
                val snackbar = Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("Go Offline",{
                    findNavController().navigate(R.id.action_beerList_to_favoriteBeer)
                })

                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.DKGRAY)
                snackbar.show()

            }
        })





        viewModel.loading.observe(viewLifecycleOwner, { ltrue ->
            IsShimmerEffectOn(isDisplayed = ltrue)
        })

        viewModel.beers.observe(viewLifecycleOwner, { beers ->
            for (beer in beers) {
                Log.d("Tag", "onViewCreated using Retrofit call: ${beer.name}")
            }

            val result = beers

            beerAdapter.submitList(result)

            //Converting the url to bitmap and passing them to Room object



//            for (beer in beers) { //Passing the values of Retrofit model(BeerModel) to Roomdb model (model)
//
//                Log.d("insert","Inserting Page : ${viewModel.page}")
//
//                Glide.with(this).asBitmap().load(beer.image_url)
//                    .into(object : CustomTarget<Bitmap?>() {
//                        override fun onResourceReady(
//                            resource: Bitmap,
//                            transition: Transition<in Bitmap?>?
//                        ) {
//
//                            viewModel.insertBeer(
//                                model(
//                                    beer.pk,
//                                    resource,
//                                    beer.name,
//                                    beer.tagline,
//                                    beer.abv,
//                                    beer.description,
//                                    beer.food_pairing,
//                                    beer.brewers_tips
//                                )
//                            )
//                           // viewModel.getFromRoom()
//                        }
//
//                        override fun onLoadCleared(placeholder: Drawable?) {
//                            //
//                        }
//
//
//                    })
//            }


        })
            //Original position of getFromRoom()
        //Change 1 - Get to Search page
        //Change 2 - Position of Loading data in Room
        viewModel.getFromRoom()
        // Getting the values back from Room
        viewModel.resultFromRoom?.observe(viewLifecycleOwner, { dbBeer ->

            dbBeer?.let{
               // beerAdapter.submitList(it)
                for (beer in it) {
                    Log.d("Tag", "onViewCreated fetched data using Room : ${beer.tagLine}")
                }
            }





        })
//
//
        viewModel.getBeerByName.observe(viewLifecycleOwner, {

            //Bu Log.d("adapter","Size of the fetched list is ${it.size}")

           // beerAdapter.submitList(it)
        })


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("adapter", "Adapter called")
                    val newQuery = "${query}%"
                    viewModel.getBeerByName(newQuery)
                    Log.d("adapter", "Get beer by name called")
                }else if(query == null){
                    viewModel.getFromRoom()
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
//                Log.d("adapter", "Adapter called from search view")
//                if (newQuery != null) {
//                    val newQuery = "%${newQuery}%"
//                    viewModel.getBeerByName(newQuery)
//
//                }else if(newQuery == null){
//                    viewModel.getFromRoom()
//                }
                return false
            }

        })







//
//    fun CircularIndeterminateProgressBarDisplayed(isDisplay: Boolean) {
//        val progressBar : ProgressBar = binding.progressBar
//
//        if (isDisplay == true){
//            progressBar.visibility = View.VISIBLE
//        }
//        else if(isDisplay == false) {
//            progressBar.visibility = View.GONE
//        }
//    }
//


    }



    private fun IsShimmerEffectOn(isDisplayed: Boolean) {
        if (isDisplayed) {
            //Starting the shimmering effect
            binding.shimmerViewContainer.apply {
                startShimmer()
                visibility = View.VISIBLE
            }


        } else {
            //Closing the shimmering effect
            binding.shimmerViewContainer.apply {
                stopShimmer()
                visibility = View.GONE
            }
        }
    }

    private fun setupRecyclerView() = binding.recyclerView.apply {
        beerAdapter = beerAdapter()
        adapter = beerAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addOnScrollListener(this@BeerList.scrollListener)


    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }

        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount

            val isNotAtBeginning = firstVisibleItemPosition >= 0

            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE

            val shouldPaginate =
                isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getFromRetrofit()
                isScrolling = false
            }


        }
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }
}




