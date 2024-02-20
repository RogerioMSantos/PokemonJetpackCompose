package com.example.app.repository

import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.data.remote.dto.GetPokemonResponse
import com.example.app.entities.EvolutionChain
import com.example.app.entities.Pokemon


interface PokemonRepository {

    suspend fun getPokemons(offSet:GetPokemonListRequest): List<Pokemon>

    suspend fun getPokemon(pokemon:Any): Pokemon

    suspend fun getEvolutions(pokemon:String): EvolutionChain
}