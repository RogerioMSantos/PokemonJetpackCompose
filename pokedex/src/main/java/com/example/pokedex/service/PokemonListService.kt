package com.example.pokedex.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.repository.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
import io.objectbox.BoxStore
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListService : Service(){

    private val binder = PokemonListBinder()

    inner class PokemonListBinder : Binder(){
        fun getService(): PokemonListService = this@PokemonListService
    }

    override fun onBind(intent: Intent): IBinder = binder

    @Inject
    lateinit var pokemonRepository: PokemonRepository

    @Inject
    lateinit var objectBoxRepository: BoxStore

    val QTDPOKEMON = 50
    val livePokemons = MutableLiveData<List<Pokemon?>>()

    private var nextPage = 0

    suspend fun getPokemons(){
        val pokemons = livePokemons.value?.toMutableList() ?:mutableListOf()
        if(nextPage > 20 ) return
        if((nextPage - 1 )  * QTDPOKEMON > 1025){
            val request = GetPokemonListRequest(nextPage * QTDPOKEMON, 25)
            pokemons.addAll(pokemonRepository.getPokemons(request))
            nextPage++
            livePokemons.value = pokemons
        }
        else {
            val request = GetPokemonListRequest(nextPage * QTDPOKEMON, QTDPOKEMON)
            pokemons.addAll(pokemonRepository.getPokemons(request))
            nextPage++
            livePokemons.value = pokemons
        }
    }

    suspend fun findPokemon(pokemon: Any){
        nextPage = 0
        val request = pokemonRepository.getPokemon(pokemon)
        resetList()
        if(request.name != "")
            livePokemons.value = mutableListOf(request)

    }

    fun resetList(){
        livePokemons.value = mutableListOf()
    }
}