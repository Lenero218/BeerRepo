package com.example.let_me_have_one.presentation.ui.beers


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide

import com.example.let_me_have_one.databinding.FragmentBeerBinding
import com.example.let_me_have_one.db.model


class Beer_Fragment : Fragment() {

    lateinit var binding : FragmentBeerBinding
    val args :Beer_FragmentArgs by navArgs()
    //val argsFromRetrofit : Beer_FragmentArgs by navArgs()




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

        binding.apply {
            Glide.with(this@Beer_Fragment)
                .load(modelRetro.image_url)
                .into(beerImage)


            title.setText(modelRetro.name)
            tagline.setText(modelRetro.tagline)
        }
//        val modelRetrofit = argsFromRetrofit

//        binding.apply {
//            beerImage.setImageBitmap(modelRoom.image)
//           // description.setText(model.description)
//            title.setText(modelRoom.name)
//            tagline.setText(modelRoom.tagLine)
//        }


         binding.addToCart.setOnClickListener{
            //Write functionality to Add to the cart and then implement the dismiss
            //dismiss()


             findNavController().navigate(com.example.let_me_have_one.R.id.action_beer_Fragment_to_add_to_Cart_Fragment)
        }

        binding.buyNow.setOnClickListener{

            //Navigate to the payment page using nav controller and then dismiss
          //  dismiss()
            findNavController().navigate(com.example.let_me_have_one.R.id.action_beer_Fragment_to_payment_Fragment)
        }

        binding.description.setOnClickListener( {
            AlertDialog.Builder(activity)
                .setMessage(
                    modelRetro.description
                )
                .setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener { dialog, which -> dialog.cancel() }).show()
            false
      })

    }


}