package com.example.let_me_have_one.util

interface DomainMapper<T, DomainModel> {

    fun mapToDomainModel(model : T) : DomainModel
}