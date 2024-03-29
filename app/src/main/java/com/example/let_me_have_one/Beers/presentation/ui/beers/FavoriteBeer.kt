package com.example.let_me_have_one.Beers.presentation.ui.beers

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.let_me_have_one.Beers.adapter.favoriteAdapter
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FragmentFavoriteBeerBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoriteBeer : Fragment() {

    val viewModel: BeerListViewModel by viewModels()
    private lateinit var favBeerAdapter: favoriteAdapter
    lateinit var binding : FragmentFavoriteBeerBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favorite_beer,container,false)

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

       viewModel.getAllForFavorite()

        viewModel.getBeerByName.observe(viewLifecycleOwner,{


            favBeerAdapter.submitList(it)

        })

        viewModel.syncCheck.observe(viewLifecycleOwner,{

            viewModel.getAllForFavorite()
            favBeerAdapter.notifyDataSetChanged()

        })

        viewModel.getBeerForFavorite.observe(viewLifecycleOwner,{
            //provide value to the adapter
            favBeerAdapter.submitList(it)

        })


        binding.favSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    val newQuery = "%${query}%"
                    viewModel.getBeerByNameForFavorite(newQuery)


                }else if(query == null){
                    viewModel.getAllForFavorite()
                }
                return false
            }

            override fun onQueryTextChange(newQuery: String?): Boolean {

                if (newQuery != null) {
                    val newQuery = "%${newQuery}%"
                    viewModel.getBeerByNameForFavorite(newQuery)

                }else if(newQuery == null){
                    viewModel.getAllForFavorite()
                }
                return false
            }

        })



        val itemTouchHelperCallback = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val beer = favBeerAdapter.differ.currentList[position]
                viewModel.delete(beer)


                Snackbar.make(view,"Removed from favorites", Snackbar.LENGTH_LONG).apply {

//                    setAction("Undo"){
//
//                        viewModel.insertBeer(beer)
//                        Log.d("Insert","Inserted beer with Offer = ${beer.currentOffer.toString()}")
//                        viewModel.sync()
//                    }
                    show()

                }


            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.favoriteBeerRecyclerView)
        }


    }





    private fun setupRecyclerView() {
        binding.favoriteBeerRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            favBeerAdapter = favoriteAdapter(viewModel)
            adapter = favBeerAdapter
        }
    }


}