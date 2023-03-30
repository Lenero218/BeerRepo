package com.example.let_me_have_one.di

import com.example.let_me_have_one.Network.BeersService
import com.example.let_me_have_one.Network.models.BeerDtoMapper
import com.example.let_me_have_one.db.Dao
import com.example.let_me_have_one.repository.BeerRepository
import com.example.let_me_have_one.repository.Beer_Repository_Impl
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
    ) : BeerRepository{
        return Beer_Repository_Impl(
                beerDao,beersService,beerDtoMapper
        )
    }

}