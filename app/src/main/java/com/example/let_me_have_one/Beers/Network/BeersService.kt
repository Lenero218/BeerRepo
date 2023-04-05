package com.example.let_me_have_one.Beers.Network

import com.example.let_me_have_one.Beers.Network.models.BeerDTO

import retrofit2.http.GET
import retrofit2.http.Query

interface BeersService {


    @GET("beers")
    suspend fun search(
        @Query("beer_name")query : String
    ) : List<BeerDTO>

    @GET("beers")
    suspend fun get() : List<BeerDTO>

    @GET("beers")
    suspend fun searchPage(
        @Query("page")page : Int
    ) : List<BeerDTO>

    //Get data for food

    @GET("beers")
    suspend fun searchBeerForFood(
        @Query("food") food : String
    ) : List<BeerDTO>

    @GET("beers")
    suspend fun searchAccToAlcoholABV(

        @Query("abv_gt") low : Int,
        @Query("abv_it") high : Int

    ) : List<BeerDTO>



}