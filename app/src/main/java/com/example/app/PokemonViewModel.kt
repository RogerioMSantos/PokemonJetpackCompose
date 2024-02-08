package com.example.app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.app.data.remote.GetServiceImpl
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonListResponse

class PokemonViewModel :ViewModel(){

    val QTDPOKEMON = 50
    val livePokemons = MutableLiveData<List<GetPokemonListResponse.Pokemon?>>()

    private var nextPage = 0

    suspend fun getPokemons(getServiceImpl: GetServiceImpl){
        val pokemons = livePokemons.value?.toMutableList() ?:mutableListOf()
        val request = GetPokemonListRequest(nextPage * QTDPOKEMON, QTDPOKEMON)
        pokemons.addAll(getServiceImpl.getPokemons(request).results)
        nextPage++
        livePokemons.value = pokemons
    }
}