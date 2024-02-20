package com.example.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonSpeciesResponse (

    @SerialName("evolution_chain")
    val evolutionChain : EvolutionChain
) {
    @Serializable
    data class EvolutionChain (
       val  url: String
    )
}