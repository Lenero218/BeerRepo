package com.example.let_me_have_one.repository

import androidx.lifecycle.LiveData
import com.example.let_me_have_one.Network.BeersService
import com.example.let_me_have_one.Network.models.BeerDtoMapper
import com.example.let_me_have_one.Network.models.BeerModel
import com.example.let_me_have_one.db.Dao
import com.example.let_me_have_one.db.model
import javax.inject.Inject

class Beer_Repository_Impl @Inject constructor(
    private val beerDao :Dao,
    private val beersService: BeersService,
    private val mapper : BeerDtoMapper

) : BeerRepository {
    override suspend fun search(query: String): List<BeerModel> {
        val result = beersService.search(query)
        return mapper.ToDomainList(result)
    }

    override suspend fun get(): List<BeerModel> {
        val result = beersService.get()
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


}