package com.example.pokedex.data.repository

import com.example.pokedex.data.remote.ApiService
import com.example.pokedex.data.remote.dto.GetEvolutionChainResponse
import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.entities.EvolutionChain
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.repository.PokemonRepository
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

class PokemonRepositoryImpl (val api: ApiService): PokemonRepository{

    override suspend fun getPokemons(offSet: GetPokemonListRequest): List<Pokemon>{
        val pokemonsDTO = api.getPokemons(offSet)
        return jacksonObjectMapper().convertValue(pokemonsDTO.results)
    }

    override suspend fun getPokemon(pokemon:Any): Pokemon{
        val pokemonDTO = api.getPokemon(pokemon)
        return jacksonObjectMapper().convertValue(pokemonDTO)
    }

    override suspend fun getEvolutions(pokemon: String): EvolutionChain {
        val evolutionChain = api.getEvolutionChain(pokemon)

        return getEvolutionsList(evolutionChain,pokemon)

    }

    private fun getEvolutionsList(
        evolutionChain: GetEvolutionChainResponse,
        name: String
    ): EvolutionChain {
        val evolveFrom = mutableListOf<Pokemon>()
        val evolveTo = mutableListOf<Pokemon>()
        var iterator = evolutionChain.chain
        while (iterator.evolveTo != null && iterator.species.name != name) {
            val pokemon = iterator.species
            evolveFrom.add(Pokemon(pokemon.name, pokemon.url))
            if (iterator.evolveTo!!.size > 1) {
                for (i in iterator.evolveTo!!) {
                    if (findPokemonChain(i, name)) {
                        iterator = i
                        break
                    }
                }
            }
            else iterator = iterator.evolveTo!![0]
        }

        if (iterator.evolveTo != null && !(iterator.evolveTo!!.isEmpty()))
            for (i in iterator.evolveTo!!)
                evolveTo.addAll(findEvolverToList(i))
        return EvolutionChain(evolveFrom = evolveFrom, evolveTo = evolveTo)
    }

    private fun findEvolverToList(iterator: GetEvolutionChainResponse.Chain): List<Pokemon> {

        val pokemon = iterator.species
        val evolveTo = mutableListOf(Pokemon(pokemon.name,pokemon.url))
        if(iterator.evolveTo == null) return  evolveTo

        for (i in iterator.evolveTo!!){
            evolveTo.addAll(findEvolverToList(i))
        }
        return evolveTo
    }

    private fun findPokemonChain(chain: GetEvolutionChainResponse.Chain, pokemon: String): Boolean{
        if(chain.species.name == pokemon) return true
        if(chain.evolveTo == null) return false
        var find = false
        for(i in chain.evolveTo!!){
            if(findPokemonChain(i,pokemon)){
                find = true
                break
            }
        }
        return find
    }
}