package com.example.let_me_have_one.Beers.presentation.ui.beerList

import android.graphics.Color
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.let_me_have_one.Beers.Network.LiveDataInternetConnection
import com.example.let_me_have_one.Beers.adapter.beerAdapter
import com.example.let_me_have_one.Beers.other.Constants.QUERY_PAGE_SIZE
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel

import com.example.let_me_have_one.R

import com.example.let_me_have_one.databinding.FragmentBeerListBinding

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


        var snackStop : Boolean = false
        cld = activity?.let { LiveDataInternetConnection(it.application) }!!

        cld.observe(viewLifecycleOwner,{isConnected->
            if(isConnected){

                if(snackStop){

                    viewModel._loading.value = false

                    val snackbar = Snackbar.make(view,"Connected Succesfully",Snackbar.LENGTH_SHORT)
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.DKGRAY)
                    snackbar.show()
                    snackStop = false
                }

                viewModel.getFromRetrofit()

            }else{

                viewModel._loading.value = true

                val snackbar = Snackbar.make(view,"No Internet Connection",Snackbar.LENGTH_INDEFINITE).setAction("Go Offline",{
                    findNavController().navigate(R.id.action_beerList_to_favoriteBeer)
                })

                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.DKGRAY)
                snackbar.show()

                snackStop = true

            }
        })





        viewModel.loading.observe(viewLifecycleOwner, { ltrue ->
            IsShimmerEffectOn(isDisplayed = ltrue)
        })

        viewModel.beers.observe(viewLifecycleOwner, {beers->
            for (beer in beers) {
                Log.d("Tag", "onViewCreated using Retrofit call: ${beer.name}")
            }

            val result = beers

            beerAdapter.submitList(result)

        })

        viewModel.getBeerByName.observe(viewLifecycleOwner, {

            // Log.d("adapter","Size of the fetched list is ${it.size}")

            //beerAdapter.submitList(it)
        })


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {

                    viewModel.searchWithQuery(query)

                }else if(query == null){
                    viewModel.getFromRetrofit()
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                Log.d("adapter", "Adapter called from search view")
                if (newQuery != null) {
                    val newQuery = "%${newQuery}%"
                    viewModel.getBeerByName(newQuery)

                }else if(newQuery == null){
                    viewModel.getFromRoom()
                }
                return false
            }

        })



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
        beerAdapter = activity?.let { beerAdapter(viewModel, it) }!!
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


}




