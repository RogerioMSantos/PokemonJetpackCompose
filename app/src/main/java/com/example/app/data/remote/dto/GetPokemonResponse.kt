package com.example.app.data.remote.dto

import kotlinx.serialization.Serializable


@Serializable
data class GetPokemonResponse (
    val name: String,
    val id: Int,
    val sprites: Sprites,
    val types: MutableList<Types>

){
    @Serializable
    data class Sprites (
        val front_default: String,
        val front_female: String
    )

    @Serializable
    data class Types (
        val name: String
    )
}