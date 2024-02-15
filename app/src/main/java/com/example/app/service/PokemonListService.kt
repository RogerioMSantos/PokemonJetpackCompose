package com.example.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.lifecycle.MutableLiveData
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.entities.Pokemon
import com.example.app.repository.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
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

    val QTDPOKEMON = 50
    val livePokemons = MutableLiveData<List<Pokemon?>>()

    private var nextPage = 0

    suspend fun getPokemons(){
        val pokemons = livePokemons.value?.toMutableList() ?:mutableListOf()
        val request = GetPokemonListRequest(nextPage * QTDPOKEMON, QTDPOKEMON)
        pokemons.addAll(pokemonRepository.getPokemons(request))
        nextPage++
        livePokemons.value = pokemons
    }
}