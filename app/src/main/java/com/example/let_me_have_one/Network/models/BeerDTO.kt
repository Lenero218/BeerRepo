package com.example.let_me_have_one.Network.models

import com.google.gson.annotations.SerializedName

data class BeerDTO(

    @SerializedName("id")
    var pk : Int? = null,

    @SerializedName("image_url")
    var image_url: String? = null,

    @SerializedName("name")
    var name : String? = null,

    @SerializedName("tagline")
    var tagline : String? = null,

    @SerializedName("abv")
    var abv : Double? = null,

    @SerializedName("description")
    var description : String? = null,

    @SerializedName("food_pairing")
    var food_pairing : List<String>? = null,

    @SerializedName("brewers_tips")
    var brewers_tips : String? = null

)