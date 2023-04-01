package com.example.let_me_have_one.presentation.ui.beers


import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.let_me_have_one.databinding.FragmentBeerBinding
import com.example.let_me_have_one.db.model
import com.example.let_me_have_one.presentation.ui.beerList.BeerListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class Beer_Fragment : Fragment() {

    val viewModel: BeerListViewModel by viewModels()
    lateinit var binding : FragmentBeerBinding
    val args :Beer_FragmentArgs by navArgs()





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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val modelRetro = args.beerModelFromRetro

        var reviews = rand(22000,100000)

        binding.apply {
            Glide.with(this@Beer_Fragment)
                .load(modelRetro.image_url)
                .into(beerImage)


            title.setText(modelRetro.name)
            tagline.setText(modelRetro.tagline)
            amount.setText(modelRetro.amount.toString())
            noOfReviews.setText(reviews.toString())
        }



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
                             modelRetro.description,modelRetro.food_pairing,modelRetro.brewers_tips,modelRetro.amount,true,modelRetro.isFavorite))
                         Log.d("favorite","Added to cart into Room db as favorite ")
                     }

                     override fun onLoadCleared(placeholder: Drawable?) {
                         //
                     }

                 })


            Toast.makeText(it.context,"Added to cart!!",Toast.LENGTH_SHORT).show()
             findNavController().navigate(com.example.let_me_have_one.R.id.action_beer_Fragment_to_add_to_Cart_Fragment)
        }

        binding.buyNow.setOnClickListener{

            //Navigate to the payment page using nav controller and then dismiss
          //  dismiss()
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
                .show()
            false
      })

    }
    fun rand(start: Int, end: Int): Int {

        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return Random(System.nanoTime()).nextInt(end - start + 1) + start

    }

}