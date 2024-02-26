package com.example.pokedex.repository

import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.entities.EvolutionChain
import com.example.pokedex.entities.Pokemon


interface PokemonRepository {

    suspend fun getPokemons(offSet:GetPokemonListRequest): List<Pokemon>

    suspend fun getPokemon(pokemon:Any): Pokemon

    suspend fun getEvolutions(pokemon:String): EvolutionChain
}