package com.example.app.data.remote

import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonResponse

interface GetService {

    suspend fun getPokemons(offSet: GetPokemonListRequest):GetPokemonListResponse
    suspend fun getPokemon(pokemon: Any): GetPokemonResponse
}