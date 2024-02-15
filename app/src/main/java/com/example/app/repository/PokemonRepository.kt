package com.example.app.repository

import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.data.remote.dto.GetPokemonResponse
import com.example.app.entities.Pokemon


interface PokemonRepository {

    suspend fun getPokemons(offSet:GetPokemonListRequest): MutableList<Pokemon>

    suspend fun getPokemon(pokemon:Any): Pokemon
}