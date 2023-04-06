package com.example.let_me_have_one.Beers.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@androidx.room.Dao
interface Dao  {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBeer(beer : model)

//    @Query("Select DISTINCT * From cached_table ORDER BY id ASC")
//    fun getAllBeer() : LiveData<List<model>>
    
    @Query("Select count(*) from cached_table")
    fun isEmpty() : LiveData<Int>

    @Query("Select DISTINCT * From cached_table WHERE name LIKE  :name AND currentOffer > 0 ")
    fun getBeerWithName(name : String) : List<model>

    @Query("Select * from cached_table WHERE currentOffer > 0  ")
    fun getAllBeerForCart() : List<model>


    @Query("Select DISTINCT * From cached_table WHERE name LIKE :name AND currentOffer == 0")
    fun getBeerWithNameForFavorite(name : String) : List<model>

    @Query("Select  * from cached_table WHERE currentOffer = 0")
    fun getAllBeerForFavorite() : List<model>

    @Query("DELETE from cached_table WHERE name = :name ")
    fun deleteBeer(name : String)





}