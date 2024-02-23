package com.example.pokedex.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.repository.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PokemonService: Service(){

    private val binder = PokemonBinder()

    inner class PokemonBinder : Binder(){
        fun getService(): PokemonService = this@PokemonService
    }
    override fun onBind(intent: Intent?): IBinder = binder
    @Inject
    lateinit var pokemonRepository: PokemonRepository



    suspend fun getPokemon(id: String): Pokemon {
        var pokemon = pokemonRepository.getPokemon(id)
        pokemon.evolutions = pokemonRepository.getEvolutions(pokemon.name)
        return pokemon
    }
}
