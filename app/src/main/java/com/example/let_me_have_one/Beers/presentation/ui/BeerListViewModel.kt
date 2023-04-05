package com.example.let_me_have_one.Beers.presentation.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.repository.BeerRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class BeerListViewModel
@Inject
    constructor(
private val Repository : BeerRepository
) : ViewModel() {

    private val _beers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val beers : LiveData<List<BeerModel>> get() = _beers
    val _query : MutableLiveData<String> = MutableLiveData()
    val query : LiveData<String> get() = _query
    val _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading
    var page : Int = 1


    var resultFromRoom : LiveData<List<model>>? = null
//    var getBeerByName : LiveData<List<model>>? = null
    val getBeerByName : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val getBeerForCart : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val NoOfItems = Repository.isEmpty()
    val getBeerForFavorite : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val getBeerForFood : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getLightBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getMediumBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getStrongBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())

    val getRecomendedBeers: MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())


    init{
        getAllForFavorite(true)
        getAllBeerForCart(true)
    }



    fun delete(name : String, fav : Boolean, cart : Boolean){

        viewModelScope.launch(Dispatchers.IO) {

            Repository.deleteBeer(name,fav,cart)

        }

        getAllBeerForCart(true)

    }


    fun getFromRetrofit(){
               viewModelScope.launch(Dispatchers.IO) {

                Log.d("check","Calling the service get with page num ${page}")

                MainScope().launch {
                    _loading.value = true

                    Repository.searchPage(page).also{
                        it.let{
                         //  delay(2200)

                            Log.d("check","Append beer called")


                            for(i in 0..it.size-1){


                                it[i].apply{
                                    //adding random rating
                                    rating = randRating(2,5)

                                    //adding random offers
                                    currentOffer = rand(20,45)

                                    //adding random amount
                                    amount = rand(400,1000)

                                    //adding random reviews
                                    no_of_reviews=  randReviews(22000,100000)
                                }

                            }
                            appendBeers(it,page)
                        }
                    }
                    _loading.value = false
                }
                page = page + 1
                Log.d("check","Got the results ")

            }


    }


    fun getFoodFromRetro(name : String){

        viewModelScope.launch(Dispatchers.IO) {

            MainScope().launch {
                _loading.value = true
                Log.d("nakli","Called service class")
                Repository.getAllBeerForFood(name).also {


                    for(i in 0..it.size-1){

                        it[i].rating = randRating(2,5)

                    }

                    it.let{
                        getBeerForFood.value = it
                    }

//                    it.let {
//                        getBeerForFood.value = v
//                    }
                }
                Log.d("nakli","Got the result from VM")
                _loading.value = false
            }

        }

    }

    private fun randRating(min: Int, max: Int): Double {
        val random = getRandomReview(min, max)
        val formattedNumber = "%.1f".format(random)
        return formattedNumber.toDouble()

    }

    private fun getRandomReview(min: Int, max: Int): Double {
        require(min < max) { "Invalid range [$min, $max]" }
        return min + Random.nextDouble() * (max - min)
    }

    fun getFromRoom(){

        _loading.value = true
          resultFromRoom = Repository.getFromDb()
        _loading.value = false


    }



    fun getBeerByName(name: String){
        Log.d("adapter","Entered in viewModel")


        CoroutineScope(Dispatchers.IO).launch {

                    Repository.getBeerWithName(name).also{

                        MainScope().launch {
                                it.let{v->
                                    getBeerByName.value = v
                                }
                            }

                    }
            }

        }







    fun getAllBeerForCart(bool : Boolean){

        CoroutineScope(Dispatchers.IO).launch {
            Repository.getAllBeerForCart(bool).also{
                MainScope().launch {
                    it.let{v->
                        getBeerForCart.value = v
                    }
                }
            }
        }

    }

    fun getAllForFavorite(bool : Boolean){

       CoroutineScope(Dispatchers.IO).launch{
            Repository.getAllBeerForFavorite(bool).also{
                MainScope().launch {
                    it.let{v->
                        getBeerForFavorite.value = v

                    }
                }
            }
       }

    }


    fun randReviews(start: Int, end: Int): Int {

        require(!(start > end || end - start + 1 > Int.MAX_VALUE)) { "Illegal Argument" }
        return Random(System.nanoTime()).nextInt(end - start + 1) + start

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



    private fun appendBeers(recipes: List<BeerModel>, page:Int){
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


    fun getRecomendedBeers(min : Int, max : Int){

        Log.d("recCheck", "Entered into viewModel and calling repository with min ${min} and max ${max}")

        viewModelScope.launch {

            MainScope().launch {

                _loading.value = true

                Repository.getLightBeer(min,max).also{

                    for(i in 0..it.size-1){
                        //rating
                        it[i].rating = randRating(2,5)
                        //amount
                        it[i].amount = rand(400,1000)

                        //Get Offer
                        it[i].currentOffer = rand(20,45)
                    }

                    it.let{
                        Log.d("recCheck","Setting the observer with list of size ${it.size}")
                        getRecomendedBeers.value = it
                    }

                }
                _loading.value = false
            }

        }


    }


    fun getLightBeers(){

        viewModelScope.launch {

            MainScope().launch {


                Repository.getLightBeer(2,5).also {

                    for(i in 0..it.size-1){

                        it[i].rating = randRating(2,5)

                    }

                    it.let {
                        getLightBeers.value = it
                    }
                }


            }
        }

    }

    fun getMediumBeers(){

        viewModelScope.launch {

            MainScope().launch {





                Repository.getLightBeer(3,5).also {

                    for(i in 0..it.size-1){

                        it[i].rating = randRating(2,5)

                    }

                    it.let {
                        getMediumBeers.value = it
                    }
                }


            }
        }

    }

    fun getStrongBeers(){

        viewModelScope.launch {

            MainScope().launch {


                Repository.getLightBeer(5,7).also {

                    for(i in 0..it.size-1){

                        it[i].rating = randRating(2,5)

                    }


                    it.let {
                        getStrongBeers.value = it
                    }
                }


            }
        }

    }




}