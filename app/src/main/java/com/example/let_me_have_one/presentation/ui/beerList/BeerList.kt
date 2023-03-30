package com.example.let_me_have_one.presentation.ui.beerList

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Query
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.let_me_have_one.R
import com.example.let_me_have_one.adapter.beerAdapter
import com.example.let_me_have_one.databinding.ActivityMainBinding
import com.example.let_me_have_one.databinding.FragmentBeerListBinding
import com.example.let_me_have_one.db.model
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BeerList : Fragment() {

    lateinit var binding: FragmentBeerListBinding
    val viewModel: BeerListViewModel by viewModels()
    private lateinit var beerAdapter: beerAdapter

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

        viewModel.loading.observe(viewLifecycleOwner, { ltrue ->
            IsShimmerEffectOn(isDisplayed = ltrue)
        })
//
        viewModel.NoOfItems.observe(viewLifecycleOwner, { count ->
            if (count == 0) {
                viewModel.getFromRetrofit()
            }


        })
//
//
//
//
        viewModel.beers.observe(viewLifecycleOwner, { beers ->
            for (beer in beers) {
                Log.d("Tag", "onViewCreated using Retrofit call: ${beer.name}")
            }

            //Converting the url to bitmap and passing them to Room object

            for (beer in beers) { //Passing the values of Retrofit model(BeerModel) to Roomdb model (model)
                Glide.with(this).asBitmap().load(beer.image_url)
                    .into(object : CustomTarget<Bitmap?>() {
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap?>?
                        ) {
                            viewModel.insertBeer(
                                model(
                                    beer.pk,
                                    resource,
                                    beer.name,
                                    beer.tagline,
                                    beer.abv,
                                    beer.description,
                                    beer.food_pairing,
                                    beer.brewers_tips
                                )
                            )
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                            //
                        }


                    })
            }


        })

        viewModel.getFromRoom()
        // Getting the values back from Room
        viewModel.resultFromRoom?.observe(viewLifecycleOwner, { dbBeer ->

            beerAdapter.submitList(dbBeer)

            for (beer in dbBeer) {
                Log.d("Tag", "onViewCreated fetched data using Room : ${beer.tagLine}")
            }

        })
//
//
        viewModel.getBeerByName.observe(viewLifecycleOwner, {

            //Bu Log.d("adapter","Size of the fetched list is ${it.size}")

            beerAdapter.submitList(it)
        })


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("adapter", "Adapter called")
                    viewModel.getBeerByName(query)
                    Log.d("adapter", "Get beer by name called")


                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                Log.d("adapter", "Adapter called from search view")
                if (newQuery != null) {
                    viewModel.getBeerByName(newQuery)
                }
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
    }
}


