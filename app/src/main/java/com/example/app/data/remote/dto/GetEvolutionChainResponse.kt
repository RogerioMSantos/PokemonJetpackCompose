package com.example.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetEvolutionChainResponse (
    val chain: Chain

){


    @Serializable
    data class Chain (
        @SerialName("evolves_to")
        var evolveTo: List<Chain>?,
        val species: Species
    )

    @Serializable
    data class Species(
        val name: String,
        val url: String
    )


}
