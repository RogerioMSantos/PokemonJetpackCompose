package com.example.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonListRequest(
    val offSet: Int = 0,
    val limit: Int = 20
)