package com.example.app.data.remote.dto

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonGetter
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonUnwrapped
import kotlinx.serialization.Serializable
import java.util.stream.Collectors





@Serializable
data class GetPokemonResponse (
    val name: String,
    val id: Int,
    @JsonUnwrapped
    val sprites: Sprites,
    val types: List<Type>

){
    @Serializable
    data class Sprites (
        @JsonProperty("sprite")
        val front_default: String,
        @JsonProperty("spriteFemale")
        val front_female: String?
    )

    @Serializable
    data class Type (
        val type: Species?
    )

    @Serializable
    data class Species (
        val name: String
    )

    @JsonGetter("types")
    fun getType(): List<String>? {
        return types.stream()
            .map { (type) -> type!!.name }
            .collect(Collectors.toList())
    }
}