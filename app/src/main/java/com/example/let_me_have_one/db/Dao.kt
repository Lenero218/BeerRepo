package com.example.let_me_have_one.db

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@androidx.room.Dao
interface Dao  {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBeer(beer : model)

    @Query("Select * From cached_table ORDER BY id ASC")
    fun getAllBeer() : LiveData<List<model>>
    
    @Query("Select count(*) from cached_table")
    fun isEmpty() : LiveData<Int>

    @Query("Select * From cached_table WHERE name LIKE  :name ")
    fun getBeerWithName(name : String) : List<model>

}