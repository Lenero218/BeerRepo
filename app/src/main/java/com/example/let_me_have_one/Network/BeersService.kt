package com.example.let_me_have_one.Network

import com.example.let_me_have_one.Network.models.BeerDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface BeersService {


    @GET("beers?beer_name=")
    suspend fun search(
        @Query("query")query : String
    ) : List<BeerDTO>

    @GET("beers")
    suspend fun get() : List<BeerDTO>

    @GET("beers")
    suspend fun searchPage(
        @Query("page")page : Int
    ) : List<BeerDTO>
}