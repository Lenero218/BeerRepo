package com.example.let_me_have_one.Beers.presentation.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.let_me_have_one.Beers.Network.models.BeerModel
import com.example.let_me_have_one.Beers.db.model
import com.example.let_me_have_one.Beers.repository.BeerRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class BeerListViewModel
@Inject
    constructor(
 @ApplicationContext private val context: Context,
private val Repository : BeerRepository
) : ViewModel() {

    private val _beers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val beers : LiveData<List<BeerModel>> get() = _beers
    val _query : MutableLiveData<String> = MutableLiveData()
    val query : LiveData<String> get() = _query
    val _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean> get() = _loading
    var page : Int = 0
    var cpage : Int = 1
    val syncCheck : MutableLiveData<Boolean> = MutableLiveData()

    var isFavorite = false


    val getBeerByName : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val getBeerForCart : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val NoOfItems = Repository.isEmpty()
    val getBeerForFavorite : MutableLiveData<List<model>> = MutableLiveData(emptyList())
    val getBeerForFood : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getLightBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getMediumBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())
    val getStrongBeers : MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())

    val getRecomendedBeers: MutableLiveData<List<BeerModel>> = MutableLiveData(emptyList())

    var isConnectedToInternet = false
    var isInitialized = false



    init{

         isConnectedToInternet = checkForInternet(context)

        if(isConnectedToInternet)
        {getFromRetrofit()}

    }



    fun get(cpage : Int){

        viewModelScope.launch(Dispatchers.IO) {


                MainScope().launch {
                    Repository.searchPage(cpage).also{

                        it.let{

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

                        }

                        _beers.value=it




                }

            }
        }

    }


    fun sync(){


        if(syncCheck.value == false)
            syncCheck.value = true
        else
            syncCheck.value = false
    }

    fun delete(name : String, fav : Boolean, cart : Boolean){

        viewModelScope.launch(Dispatchers.IO) {

            Repository.deleteBeer(name,fav,cart)

            MainScope().launch {
                sync()
            }

        }



    }


    fun getFromRetrofit(){
               viewModelScope.launch(Dispatchers.IO) {



                MainScope().launch {
                    _loading.value = true

                    Repository.searchPage(page).also{
                        it.let{





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

                if(page != 0)
                    cpage = page

                page = page + 1


            }


    }


    fun getFoodFromRetro(name : String){

        viewModelScope.launch(Dispatchers.IO) {

            MainScope().launch {
                _loading.value = true

                Repository.getAllBeerForFood(name).also {


                    for(i in 0..it.size-1){

                        it[i].rating = randRating(2,5)

                    }

                    it.let{
                        getBeerForFood.value = it
                    }


                }

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





    fun getBeerByNameForFavorite(name: String){

        viewModelScope.launch(Dispatchers.IO) {


                Repository.getAllBeerForFavorite(name).also{

                    MainScope().launch {
                        it.let{
                            getBeerForFavorite.value = it
                        }
                    }


                }


        }

    }

    fun getBeerByNameForCart(name: String){



        CoroutineScope(Dispatchers.IO).launch {

                    Repository.getBeerWithNameForCart(name).also{

                        MainScope().launch {
                                it.let{v->
                                    getBeerByName.value = v
                                }
                            }

                    }
            }

        }







    fun getAllBeerForCart(){

        CoroutineScope(Dispatchers.IO).launch {
            Repository.getAllBeerForCart().also{
                MainScope().launch {
                    it.let{v->



                        getBeerForCart.value = v
                    }
                }
            }
        }

    }

    fun getAllForFavorite(){

       CoroutineScope(Dispatchers.IO).launch{
            Repository.getAllBeerForFavorite().also{
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

                       it[i].rating = randRating(2,5)

                       //adding random offers
                       it[i].currentOffer = rand(20,45)



                       //adding random reviews
                       it[i].no_of_reviews=  randReviews(22000,100000)

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



        if(page > 2){

            _beers?.let{

                current = ArrayList(this._beers.value)
            }

        }


        recipes?.let{

            current?.addAll(recipes)

        }



        current?.let{

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


                Repository.getLightBeer(2,3).also {

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

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}