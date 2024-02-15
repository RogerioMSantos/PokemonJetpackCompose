package com.example.app.data.repository

import com.example.app.data.remote.ApiService
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.entities.Pokemon
import com.example.app.repository.PokemonRepository
import java.util.stream.Collectors

class PokemonRepositoryImpl (val api: ApiService): PokemonRepository{

    override suspend fun getPokemons(offSet: GetPokemonListRequest): MutableList<Pokemon>{
        val pokemonsDTO = api.getPokemons(offSet)
        val pokemons = pokemonsDTO.results
                        .stream()
                        .map {
                            Pokemon(
                                name = it?.name ?: "",
                                url = it?.url ?: ""
                            )
                        }.collect(Collectors.toList())
        return pokemons
    }

    override suspend fun getPokemon(pokemon:Any): Pokemon{
        val pokemonDTO = api.getPokemon(pokemon)
        val types = pokemonDTO.types
                    .stream()
                    .map { it.type?.name  }
                    .collect(Collectors.toList())
        if(pokemonDTO.sprites.front_female != null){
            return Pokemon(
                id = pokemonDTO.id,
                name = pokemonDTO.name,
                sprite = pokemonDTO.sprites.front_default,
                spriteFemale = pokemonDTO.sprites.front_female,
                types = types
            )
        }
        return Pokemon(
            id = pokemonDTO.id,
            name = pokemonDTO.name,
            sprite = pokemonDTO.sprites.front_default,
            types = types
        )
    }
}