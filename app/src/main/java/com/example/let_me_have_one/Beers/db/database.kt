package com.example.let_me_have_one.Beers.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [model::class], version = 10,
    autoMigrations = [
        AutoMigration(from = 9, to = 10)
    ]
    )

@TypeConverters(Converters::class)
abstract class database : RoomDatabase(){

    abstract fun getBeerDao() : Dao


}