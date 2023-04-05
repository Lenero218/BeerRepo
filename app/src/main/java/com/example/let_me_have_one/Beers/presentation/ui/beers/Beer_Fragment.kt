package com.example.let_me_have_one.Beers.presentation.ui.beers


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.let_me_have_one.Beers.adapter.recomendationListAdapter
import com.example.let_me_have_one.R

import com.example.let_me_have_one.databinding.FragmentBeerBinding
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.presentation.ui.BeerListViewModel


import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Beer_Fragment : Fragment() {

    val viewModel: BeerListViewModel by viewModels()
    lateinit var binding : FragmentBeerBinding
    val args : Beer_FragmentArgs by navArgs()
    lateinit var recRv : RecyclerView
    lateinit var adapter : recomendationListAdapter




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, com.example.let_me_have_one.R.layout.fragment_beer_, container, false)
        return binding.root

      //  return inflater.inflate(R.layout.fragment_add_to__cart_, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val modelRetro = args.beerModelFromRetro

        setupRecyclerView()

        var amountAfterDiscount = (modelRetro.amount.toDouble() - modelRetro.amount.toDouble() * (modelRetro.currentOffer.toDouble()/100)).toInt()

        binding.apply {
            Glide.with(this@Beer_Fragment)
                .load(modelRetro.image_url)
                .into(beerImage)


            title.setText(modelRetro.name)
            tagline.setText(modelRetro.tagline)
            amount.setText(modelRetro.amount.toString())
            noOfReviews.setText(modelRetro.no_of_reviews.toString())
            currentOffer.setText(modelRetro.currentOffer.toString())
            finalAmount.setText(amountAfterDiscount.toString())
        }

        Log.d("recCheck","Checking the abv value and it is found ${modelRetro.abv} and calling viewModel")

        modelRetro.abv?.let { viewModel.getRecomendedBeers(it.toInt()-1,modelRetro.abv.toInt()+1) }

        Log.d("recCheck","Submitting the list with size ")

        viewModel.getRecomendedBeers.observe(viewLifecycleOwner,{
            Log.d("recCheck","Submitted the recomendation list")
            adapter.submitList(it)
        })

         binding.addToCart.setOnClickListener{
            //Write functionality to Add to the cart and then implement the dismiss
            //dismiss()

             Glide.with(it).asBitmap().load(modelRetro.image_url)
                 .into(object : CustomTarget<Bitmap?>() {
                     override fun onResourceReady(
                         resource: Bitmap,
                         transition: Transition<in Bitmap?>?
                     ) {
                         viewModel.insertBeer(model(modelRetro.pk,resource,modelRetro.name,modelRetro.tagline,modelRetro.abv,
                             modelRetro.description,modelRetro.food_pairing,modelRetro.brewers_tips,modelRetro.amount,true,modelRetro.isFavorite,modelRetro.rating,modelRetro.currentOffer,amountAfterDiscount,modelRetro.no_of_reviews))
                         Log.d("favorite","Added to cart into Room db as favorite ")
                     }

                     override fun onLoadCleared(placeholder: Drawable?) {
                         //
                     }

                 })


            Toast.makeText(it.context,"Added to cart!!",Toast.LENGTH_SHORT).show()
             //findNavController().navigate(com.example.let_me_have_one.R.id.action_beer_Fragment_to_add_to_Cart_Fragment)
        }

        binding.buyNow.setOnClickListener{

            findNavController().navigate(com.example.let_me_have_one.R.id.action_beer_Fragment_to_payment_Fragment)
        }

        binding.title.setOnClickListener( {
            AlertDialog.Builder(activity)
                .setMessage(
                    modelRetro.description
                )
                .setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })
                .setTitle("About me!")
                .setIcon(R.drawable.beer)
                .show()
            false
      })





    }

    private fun setupRecyclerView() {
       recRv = binding.recomendationsRV
        recRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = recomendationListAdapter()
        recRv.adapter = adapter
    }


}