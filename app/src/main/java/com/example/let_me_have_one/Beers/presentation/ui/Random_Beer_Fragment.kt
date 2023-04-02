package com.example.let_me_have_one.Beers.presentation.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.let_me_have_one.Beers.presentation.ui.beers.randomBeer.adapter.ParentAdapter
import com.example.let_me_have_one.Beers.presentation.ui.beers.randomBeer.childModel
import com.example.let_me_have_one.Beers.presentation.ui.beers.randomBeer.parentModel
import com.example.let_me_have_one.R
import com.example.let_me_have_one.databinding.FragmentRandomBeerBinding



class Random_Beer_Fragment : Fragment() {

   lateinit var binding : FragmentRandomBeerBinding
   private lateinit var adapter : ParentAdapter
   private lateinit var recyclerView : RecyclerView
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
        setupRecyclerView()
    }

    fun setupRecyclerView(){
        recyclerView = binding.rvParent
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        adapter = context?.let { ParentAdapter(it) }!!
        recyclerView.adapter = adapter



        addDataToList()
    }

    private fun addDataToList() {
        var parentModelClassArrayList : ArrayList<parentModel> = ArrayList(emptyList())
        var childModelClassArrayList : ArrayList<childModel> = ArrayList(emptyList())
        var favorite : ArrayList<childModel> = ArrayList(emptyList())
        var recentlyWatched : ArrayList<childModel> = ArrayList(emptyList())
        var latestList : ArrayList<childModel> = ArrayList(emptyList())

        latestList.add(childModel( null,"A"))
        latestList.add(childModel( null,"B"))
        latestList.add(childModel( null,"C"))
        latestList.add(childModel( null,"D"))
        latestList.add(childModel( null,"E"))
        latestList.add(childModel( null,"F"))
        latestList.add(childModel( null,"G"))
        latestList.add(childModel( null,"H"))
        latestList.add(childModel( null,"I"))
        latestList.add(childModel( null,"J"))

        parentModelClassArrayList.add(parentModel("Food",latestList,R.drawable.beer))

        recentlyWatched.add(childModel(null,"RW1"))
        recentlyWatched.add(childModel(null,"RW2"))
        recentlyWatched.add(childModel(null,"RW3"))
        recentlyWatched.add(childModel(null,"RW4"))
        recentlyWatched.add(childModel(null,"RW5"))
        recentlyWatched.add(childModel(null,"RW6"))
        recentlyWatched.add(childModel(null,"RW7"))
        recentlyWatched.add(childModel(null,"RW8"))
        recentlyWatched.add(childModel(null,"RW9"))

        parentModelClassArrayList.add(parentModel("Weak",recentlyWatched,R.drawable.baseline_favorite_24))

        favorite.add(childModel(null,"FW1"))
        favorite.add(childModel(null,"FW2"))
        favorite.add(childModel(null,"FW3"))
        favorite.add(childModel(null,"FW4"))
        favorite.add(childModel(null,"FW5"))
        favorite.add(childModel(null,"FW6"))
        favorite.add(childModel(null,"FW7"))
        favorite.add(childModel(null,"FW8"))
        favorite.add(childModel(null,"FW9"))

        parentModelClassArrayList.add(parentModel("Medium",favorite,R.drawable.baseline_favorite_24))

        childModelClassArrayList.clear()

        childModelClassArrayList.add(childModel(null,"CM1"))
        childModelClassArrayList.add(childModel(null,"CM2"))
        childModelClassArrayList.add(childModel(null,"CM3"))
        childModelClassArrayList.add(childModel(null,"CM4"))
        childModelClassArrayList.add(childModel(null,"CM5"))
        childModelClassArrayList.add(childModel(null,"CM6"))
        childModelClassArrayList.add(childModel(null,"CM7"))
        childModelClassArrayList.add(childModel(null,"CM8"))
        childModelClassArrayList.add(childModel(null,"CM9"))

        parentModelClassArrayList.add(parentModel("Strong",childModelClassArrayList,R.drawable.random))

        adapter.submitList(parentModelClassArrayList)
        adapter.notifyDataSetChanged()




    }


}