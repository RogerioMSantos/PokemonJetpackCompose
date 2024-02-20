package com.example.app.data.remote

import com.example.app.data.remote.dto.GetEvolutionChainResponse
import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonResponse

interface ApiService {

    suspend fun getPokemons(offSet: GetPokemonListRequest = GetPokemonListRequest()):GetPokemonListResponse

    suspend fun getPokemon(pokemon: Any): GetPokemonResponse

    suspend fun getEvolutionChain(pokemon: Any): GetEvolutionChainResponse
}