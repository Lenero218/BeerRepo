package com.example.let_me_have_one.Beers.Network.models

import com.example.let_me_have_one.Beers.util.DomainMapper


class BeerDtoMapper : DomainMapper<BeerDTO, BeerModel> {
    override fun mapToDomainModel(model: BeerDTO): BeerModel {
        return BeerModel(

                pk = model.pk,
                image_url = model.image_url,
                name = model.name,
                tagline = model.tagline,
                abv = model.abv,
                description = model.description,
                food_pairing = model.food_pairing,
                brewers_tips = model.brewers_tips
        )
    }

    fun ToDomainList(initial: List<BeerDTO>): List<BeerModel> {
        return initial.map { mapToDomainModel(it) }
    }
}