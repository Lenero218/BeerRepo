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

    override suspend fun get(): List<BeerModel> {
        Log.d("check","Calling for data")
        val result = beersService.get()
        Log.d("check","Got result from service now mapping to Domain list and returning")
        return mapper.ToDomainList(result)
    }

    override suspend fun insertIntoRoom(beer: model) {
        beerDao.insertBeer(beer)
    }

     override fun getFromDb(): LiveData<List<model>> {
       return beerDao.getAllBeer()
    }

    override fun isEmpty(): LiveData<Int> {
        return beerDao.isEmpty()
    }

    override fun getBeerWithName(name: String): List<model> {
        return beerDao.getBeerWithName(name)
    }

    override suspend fun searchPage(page: Int): List<BeerModel> {
        Log.d("check","Calling the service class with page no, ${page}")
        val result = beersService.searchPage(page)
        Log.d("check","Got result from service class using Page no. ${page}")

        for(i in 0.. result.size-1){
            Log.d("check","Result got after calling service call after calling Page ${page} ${result[i].name}")
        }

        return mapper.ToDomainList(result)
    }


}