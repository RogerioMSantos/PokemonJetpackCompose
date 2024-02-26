package com.example.pokedex.data.remote

import com.example.pokedex.data.remote.dto.GetEvolutionChainResponse
import com.example.pokedex.data.remote.dto.GetPokemonListResponse
import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.data.remote.dto.GetPokemonResponse

interface ApiService {

    suspend fun getPokemons(offSet: GetPokemonListRequest = GetPokemonListRequest()):GetPokemonListResponse

    suspend fun getPokemon(pokemon: Any): GetPokemonResponse

    suspend fun getEvolutionChain(pokemon: Any): GetEvolutionChainResponse
}