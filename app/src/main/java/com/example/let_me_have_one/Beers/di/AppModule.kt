package com.example.let_me_have_one.Beers.di

import android.content.Context
import androidx.room.Room
import com.example.let_me_have_one.Beers.db.database
import com.example.let_me_have_one.Beers.other.Constants.CACHED_DATABASE_NAME
import com.example.let_me_have_one.Beers.presentation.BaseApplication

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesApplication(@ApplicationContext app : Context) : BaseApplication {
        return app as BaseApplication
    }

    @Singleton
    @Provides
    fun providesRunningDatabase(
        @ApplicationContext app : Context
    ) = Room.databaseBuilder(
        app,
        database::class.java,
        CACHED_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideRunDao(
        db : database
    ) = db.getBeerDao()


}