package com.example.let_me_have_one.Beers.di

import com.example.let_me_have_one.Beers.Network.BeersService
import com.example.let_me_have_one.Beers.Network.models.BeerDtoMapper

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesBeersDtoMapper() : BeerDtoMapper {
        return BeerDtoMapper()
    }

    @Singleton
    @Provides
    fun provideRetrofitService() : BeersService {
        return Retrofit.Builder()
            .baseUrl("https://api.punkapi.com/v2/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()//This will create a object of the connection to the api and then
            .create(BeersService::class.java)//This will help to connect the service to the services we have provided
    }
}