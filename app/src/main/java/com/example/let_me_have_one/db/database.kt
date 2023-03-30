package com.example.let_me_have_one.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [model::class], version = 1)

@TypeConverters(Converters::class)
abstract class database : RoomDatabase(){

    abstract fun getBeerDao() : Dao


}