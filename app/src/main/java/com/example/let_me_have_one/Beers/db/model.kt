package com.example.let_me_have_one.Beers.db

import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "cached_table"
)
data class model(

    @PrimaryKey
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
    var favorite : Boolean

) : java.io.Serializable