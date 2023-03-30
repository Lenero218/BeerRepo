package com.example.let_me_have_one.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.let_me_have_one.Network.models.BeerModel
import com.example.let_me_have_one.db.model

interface BeerRepository {

suspend fun search (query : String) : List<BeerModel>

suspend fun get() : List<BeerModel>

suspend fun insertIntoRoom(beer : model)

fun getFromDb() : LiveData<List<model>>

fun isEmpty() : LiveData<Int>

fun getBeerWithName(name : String) : LiveData<List<model>>


}