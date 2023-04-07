package com.example.let_me_have_one.Beers.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.let_me_have_one.Beers.Network.BeersService
import com.example.let_me_have_one.Beers.Network.models.BeerDtoMapper
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.Dao
import com.example.let_me_have_one.Beers.db.model

import javax.inject.Inject

class Beer_Repository_Impl @Inject constructor(
    private val beerDao : Dao,
    private val beersService: BeersService,
    private val mapper : BeerDtoMapper

) : BeerRepository {
    override suspend fun search(query: String): List<BeerModel> {
        val result = beersService.search(query)
        return mapper.ToDomainList(result)
    }

    override suspend fun get(page : Int): List<BeerModel> {

        val result = beersService.searchPage(page)

        return mapper.ToDomainList(result)
    }

    override suspend fun insertIntoRoom(beer: model) {
        beerDao.insertBeer(beer)
    }



    override fun isEmpty(): LiveData<Int> {
        return beerDao.isEmpty()
    }

    override fun getBeerWithNameForCart(name: String): List<model> {
        return beerDao.getBeerWithName(name)
    }

    override suspend fun searchPage(page: Int): List<BeerModel> {

        val result = beersService.searchPage(page)


        return mapper.ToDomainList(result)
    }

    override suspend fun getAllBeerForCart(): List<model> {
        return beerDao.getAllBeerForCart()
    }

    override suspend fun getAllBeerForFavorite(): List<model> {
        return beerDao.getAllBeerForFavorite()
    }

    override suspend fun getAllBeerForFavorite(name: String): List<model> {
        return beerDao.getBeerWithNameForFavorite(name)
    }

    override suspend fun getAllBeerForFood(name: String): List<BeerModel> {

        val result = beersService.searchBeerForFood(name)

        return mapper.ToDomainList(result)
    }



    override suspend fun getLightBeer(min: Int, max : Int): List<BeerModel> {


        val result = beersService.searchAccToAlcoholABV(min,max)


        return mapper.ToDomainList(result)
    }

    override suspend fun deleteBeer(name: String) {
        beerDao.deleteBeer(name)
    }


}