package com.example.app.data.remote.dto

import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.github.pozo.KotlinBuilder
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
