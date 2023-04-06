package com.example.let_me_have_one.Beers.repository

import androidx.lifecycle.LiveData
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model


interface BeerRepository {

suspend fun search (query : String) : List<BeerModel>

suspend fun get(page : Int) : List<BeerModel>

suspend fun insertIntoRoom(beer : model)



fun isEmpty() : LiveData<Int>

fun getBeerWithNameForCart(name : String) :List<model>

suspend fun searchPage(page:Int) : List<BeerModel>

suspend fun getAllBeerForCart() : List<model>

suspend fun getAllBeerForFavorite() : List<model>

suspend fun getAllBeerForFood(name:String) : List<BeerModel>

suspend fun getAllBeerForFavorite(name : String) : List<model>

suspend fun getLightBeer(min:Int, max : Int) : List<BeerModel>

suspend fun deleteBeer(name: String,fav:Boolean, card : Boolean)


}