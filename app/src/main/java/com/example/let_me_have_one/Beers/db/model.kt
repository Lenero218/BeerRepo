package com.example.let_me_have_one.Beers.db

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "cached_table"
)
data class model(

    @PrimaryKey(autoGenerate = true)
    var id : Int? = null,

    var image: Bitmap? = null,
    var name: String? = null,
    var tagLine: String? = null,
    var abv: Double? = null,
    var description: String? = null,
    var food_pairing: List<String>? = null,
    var brewers_tips: String? = null,

    @ColumnInfo(name="amount", defaultValue = "0")
    var amount : Int,

    @ColumnInfo(name="addToCart",defaultValue="false")
    var addToCart:Boolean,

    @ColumnInfo(name="favorite", defaultValue = "false")
    var favorite : Boolean,

    @ColumnInfo(name="rating",defaultValue = "0.0")
    var rating : Double,

    @ColumnInfo(name = "currentOffer")
    var currentOffer:Int?=null,

    @ColumnInfo(name="finalAmount",defaultValue="0")
    var finalAmount:Int,

    @ColumnInfo(name="no_of_reviews",defaultValue="0")
    var no_of_reviews:Int

) : java.io.Serializable