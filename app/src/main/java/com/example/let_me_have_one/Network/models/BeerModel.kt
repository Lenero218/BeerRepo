package com.example.let_me_have_one.Network.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class BeerModel(

    val pk : Int? = null,


    val image_url: String? = null,


    val name : String? = null,


    val tagline : String? = null,


    val abv : Double? = null,


    val description : String? = null,


    val food_pairing : List<String>? = null,

    var isFavorite : Boolean = false,

    val brewers_tips : String? = null,

    var amount : Int = 0
) : java.io.Serializable