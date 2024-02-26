package com.example.pokedex.data.remote.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonListResponse (
    val count: Int,
    var next: String,
    @JsonUnwrapped
    val results: MutableList< Pokemon?>

) {
    @Serializable
    data class Pokemon(var name: String, val url: String)
}
