package com.example.let_me_have_one.presentation.ui.beerList

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.let_me_have_one.Network.models.BeerModel
import com.example.let_me_have_one.db.model
import com.example.let_me_have_one.repository.BeerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject


@HiltViewModel
class BeerListViewModel
@Inject
    constructor(
private val Repository :BeerRepository
) : ViewModel() {

    private val _beers : MutableLiveData<List<BeerModel>> = MutableLiveData()
    val beers : LiveData<List<BeerModel>> get() = _beers
    val _query : MutableLiveData<String> = MutableLiveData()
    val query : LiveData<String> get() = _query
    val _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading
    var page : Int = 1


    var resultFromRoom : LiveData<List<model>>? = null
//    var getBeerByName : LiveData<List<model>>? = null
    val getBeerByName : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val NoOfItems = Repository.isEmpty()




    fun getFromRetrofit(){

            _loading.value = true

            viewModelScope.launch(Dispatchers.Main) {

                Log.d("check","Calling the service get with page num ${page}")
                val result = Repository.searchPage(page)
                page = page + 1
                Log.d("check","Got the results ")
                delay(3000)
                _beers.value = result

            }
        _loading.value = false
    }

    fun getFromRoom(){

        _loading.value = true
          resultFromRoom = Repository.getFromDb()
        _loading.value = false


    }



    fun getBeerByName(name: String){
        Log.d("adapter","Entered in viewModel")
        _loading.value = true

        CoroutineScope(Dispatchers.IO).launch {
            Repository.getBeerWithName(name).also{ it->
                MainScope().launch {

                    it.let { v->
                        getBeerByName.value= v
                    }
                }

            }
        }
        _loading.value = false


        Log.d("adapter","Exit in viewModel")


    }

//    fun getSearchedPage(){
//        viewModelScope.launch {
//            _loading.value = true
//            val result = Repository.searchPage(page++)
//
//            _loading.value = false
//            _beers.value = result
//        }
//    }



    private fun appendRecipe(recipes: List<BeerModel>){
        val current = ArrayList(this._beers.value) //I can change only the mutable type data not the non mutable live data
        current.addAll(recipes)
        this._beers.value = current
    }


    //Inserting into the Room
    fun insertBeer(beer : model) = viewModelScope.launch(Dispatchers.IO) {
        Repository.insertIntoRoom(beer)
    }





}