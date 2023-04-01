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
import kotlin.random.Random


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

            viewModelScope.launch(Dispatchers.IO) {

                Log.d("check","Calling the service get with page num ${page}")
                 Repository.searchPage(page).also {
                     MainScope().launch {
                         it.let{
                             Log.d("check","Append beer called")

                             for( i in 0..it.size-1){
                                 it[i].amount = rand(400,1000)
                             }

                             appendBeers(it,page)
                         }
                     }
                 }



                page = page + 1
                Log.d("check","Got the results ")
                delay(1000)




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

    fun searchWithQuery(name : String){
        CoroutineScope(Dispatchers.IO).launch {
            Repository.search(name).also {it->
               MainScope().launch {

                   for( i in 0..it.size-1){
                       it[i].amount = rand(400,1000)
                   }

                   it.let{v->
                       _beers.value = v

                   }
               }
            }
        }
    }



    private fun appendBeers(recipes: List<BeerModel>,page:Int){
        var current : ArrayList<BeerModel>? = ArrayList(emptyList())

        Log.d("check","Entered into append Beers with recipe size: ${recipes.size}")

        if(page > 2){

            _beers?.let{
                Log.d("check","Added paginated list")
                current = ArrayList(this._beers.value)
            }

        }
         //I can change only the mutable type data not the non mutable live data

        recipes?.let{
            Log.d("check","Trying to append Beers with recipe size: ${recipes.size}")
            current?.addAll(recipes)
            Log.d("check","Merged list with previous list")
            Log.d("check","After adding the current list size : ${current?.size}")
        }

        Log.d("check","List size: ${current?.size}")

        current?.let{
            Log.d("check","Adding into current recipe")
            MainScope().launch {
                _beers.value = it
            }


        }


    }


    //Inserting into the Room
    fun insertBeer(beer : model) = viewModelScope.launch(Dispatchers.IO) {
        Repository.insertIntoRoom(beer)
    }

    fun rand(start: Int, end: Int): Int {

        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return Random(System.nanoTime()).nextInt(end - start + 1) + start

    }



}