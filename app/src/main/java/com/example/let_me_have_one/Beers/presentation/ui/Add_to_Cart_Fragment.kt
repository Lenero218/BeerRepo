package com.example.let_me_have_one.Beers.presentation.ui

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
import com.example.let_me_have_one.Beers.adapter.addToCartAdapter
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FragmentAddToCartBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Add_to_Cart_Fragment : Fragment() {

    val viewModel: BeerListViewModel by viewModels()

    lateinit var binding : FragmentAddToCartBinding

    private lateinit var addToCartbeerAdapter: addToCartAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_to__cart_, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        viewModel.getAllBeerForCart(true)

        addToCartbeerAdapter.notifyDataSetChanged()
        binding.addToCartRecyclerView.scrollToPosition(addToCartbeerAdapter.itemCount-1)




        // Getting the values back from Room
        viewModel.getBeerForCart.observe(viewLifecycleOwner, { dbBeer ->

            dbBeer?.let{
                addToCartbeerAdapter.submitList(it)
                for (beer in it) {
                    Log.d("Tag", "onViewCreated fetched data using Room : ${beer.tagLine}")
                }
            }

            binding.addToCartRecyclerView.adapter?.notifyDataSetChanged()
        })

        viewModel.getBeerByName.observe(viewLifecycleOwner,{dbBeer->

            dbBeer?.let{
                addToCartbeerAdapter.submitList(it)
                for (beer in it) {
                    Log.d("Tag", "onViewCreated fetched data using Room : ${beer.tagLine}")
                }
            }

        })


        binding.addToCartSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val newQuery = "%${query}%"
                    viewModel.getBeerByName(newQuery)


                }else if(query == null){
                    viewModel.getAllBeerForCart(true)
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {
                Log.d("adapter", "Adapter called from search view")
                if (newQuery != null) {
                    val newQuery = "%${newQuery}%"
                    viewModel.getBeerByName(newQuery)

                }else if(newQuery == null){
                    viewModel.getAllBeerForCart(true)
                }
                return false
            }

        })



    }

    private fun setupRecyclerView() {
        binding.addToCartRecyclerView.apply {

            layoutManager = LinearLayoutManager(requireContext())
            addToCartbeerAdapter = addToCartAdapter(viewModel)
            adapter=addToCartbeerAdapter




        }
      //  binding.addToCartRecyclerView.layoutManager = reverseOrder<>()

    }




}