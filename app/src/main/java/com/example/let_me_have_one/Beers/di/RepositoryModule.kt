package com.example.let_me_have_one.Beers.di

import com.example.let_me_have_one.Beers.Network.BeersService
import com.example.let_me_have_one.Beers.Network.models.BeerDtoMapper
import com.example.let_me_have_one.Beers.db.Dao
import com.example.let_me_have_one.Beers.repository.BeerRepository
import com.example.let_me_have_one.Beers.repository.Beer_Repository_Impl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideBeerRepository(
        beerDao: Dao,
        beersService: BeersService,
        beerDtoMapper: BeerDtoMapper
    ) : BeerRepository {
        return Beer_Repository_Impl(
                beerDao,beersService,beerDtoMapper
        )
    }

}