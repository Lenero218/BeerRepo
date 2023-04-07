package com.example.let_me_have_one.Beers.presentation.ui.beers.randomBeer

import android.app.AlertDialog
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
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.let_me_have_one.Beers.Network.LiveDataInternetConnection
import com.example.let_me_have_one.Beers.adapter.randomAdapter
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FragmentRandomBeerBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Random_Beer_Fragment : Fragment() {

   lateinit var binding : FragmentRandomBeerBinding

   lateinit var adapterSearch : randomAdapter

   lateinit var adapterLightBeer : randomAdapter

   lateinit var adapterStrongBeer : randomAdapter

   lateinit var adapterMediumBeer : randomAdapter

   val viewModel : BeerListViewModel by viewModels()

   private lateinit var foodRView : RecyclerView

   private lateinit var lightRView : RecyclerView

   private lateinit var mediumRView : RecyclerView

   private lateinit var strongRView : RecyclerView



    private lateinit var cld : LiveDataInternetConnection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_random__beer_, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFoodRecyclerView()

        setupLightBeersRecyclerView()

        setupMediumBeersRecyclerView()
//
        setupStrongBeersRecyclerView()

        var snackStop  = false


        val isConnected = checkForInternet(requireContext())

        if(isConnected){

            viewModel._loading.value = false

            viewModel.getLightBeers()

            viewModel.getMediumBeers()

            viewModel.getStrongBeers()

        }


        cld = activity?.let { LiveDataInternetConnection(it.application) }!!
        cld.observe(viewLifecycleOwner,{isConnected->
            if(isConnected){

                if(snackStop){



                    val snackbar = Snackbar.make(view,"Connected Succesfully", Snackbar.LENGTH_SHORT)
                    val snackBarView = snackbar.view
                    snackBarView.setBackgroundColor(Color.DKGRAY)
                    snackbar.show()
                    snackStop = false
                }

                viewModel.getLightBeers()

                viewModel.getMediumBeers()

                viewModel.getStrongBeers()

            }else{



                val snackbar = Snackbar.make(view,"No Internet Connection", Snackbar.LENGTH_LONG).setAction("Go Offline",{
                    findNavController().navigate(R.id.action_random_Beer_Fragment_to_favoriteBeer)
                })



                val snackBarView = snackbar.view
                snackBarView.setBackgroundColor(Color.DKGRAY)
                snackbar.show()

                snackStop = true

            }
        })



        viewModel.loading.observe(viewLifecycleOwner,{
                IsShimmerEffectOn(it)
        })






        viewModel.getBeerForFood.observe(viewLifecycleOwner,{
            Log.d("check","Attempting to submit list")
            adapterSearch.submitList(it)
        })

        viewModel.getLightBeers.observe(viewLifecycleOwner,{
            adapterLightBeer.submitList(it)
        })
//
        viewModel.getMediumBeers.observe(viewLifecycleOwner,{
            adapterMediumBeer.submitList(it)
        })
//
        viewModel.getStrongBeers.observe(viewLifecycleOwner,{
            adapterStrongBeer.submitList(it)
        })




        binding.randSearchView .setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    Log.d("nakli","called getFoodFromRetro")

                    val isConnected = checkForInternet(requireContext())

                    if(isConnected)
                    viewModel.getFoodFromRetro(query)
                    else{
                        AlertDialog.Builder(activity)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Internet Connection Alert")
                            .setMessage("No Internet Connection")
                            .setPositiveButton(
                                "Close"
                            ) { dialog, which -> dialog.cancel() }.show()
                    }

                }else if(query == null){

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




    }

    private fun setupStrongBeersRecyclerView() {
       strongRView = binding.StrongBeerRv

        adapterStrongBeer = randomAdapter(viewModel,requireContext())
        strongRView.adapter = adapterStrongBeer
        strongRView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

    }

    private fun setupMediumBeersRecyclerView() {
        mediumRView = binding.mediumBeerRv

        adapterMediumBeer = randomAdapter(viewModel,requireContext())
        mediumRView.adapter = adapterMediumBeer
        mediumRView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

    }


    //Shimmering effect
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

    private fun setupLightBeersRecyclerView() {
        lightRView = binding.lightBeerRv
        adapterLightBeer = randomAdapter(viewModel,requireContext())
        lightRView.adapter = adapterLightBeer
        lightRView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }

    fun setupFoodRecyclerView(){


        foodRView = binding.foodRv

        adapterSearch = randomAdapter(viewModel,requireContext())
        foodRView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        foodRView.adapter = adapterSearch





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