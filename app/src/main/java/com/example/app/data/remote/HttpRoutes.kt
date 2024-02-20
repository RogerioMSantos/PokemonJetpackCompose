package com.example.app.data.remote

object HttpRoutes {
    private const val BASE_URL = "https://pokeapi.co/api/v2"
    const val LIST_POKEMON = "$BASE_URL/pokemon"
    const val POKEMON = "$BASE_URL/pokemon"
    const val EVOLUTION = "$BASE_URL/evolution-chain"
    const val SPECIE = "$BASE_URL/pokemon-species"
}