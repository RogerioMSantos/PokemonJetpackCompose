package com.example.app.data.repository

import com.example.app.data.remote.ApiService
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.entities.Pokemon
import com.example.app.repository.PokemonRepository
import com.fasterxml.jackson.core.type.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.util.stream.Collectors

class PokemonRepositoryImpl (val api: ApiService): PokemonRepository{

    override suspend fun getPokemons(offSet: GetPokemonListRequest): List<Pokemon>{
        val pokemonsDTO = api.getPokemons(offSet)
        return jacksonObjectMapper().convertValue(pokemonsDTO.results)
    }

    override suspend fun getPokemon(pokemon:Any): Pokemon{
        val pokemonDTO = api.getPokemon(pokemon)
        return jacksonObjectMapper().convertValue(pokemonDTO)
    }
}