package com.example.pokedex.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.remote.dto.GetPokemonListRequest
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.entities.Pokemon_
import com.example.pokedex.entities.Team
import com.example.pokedex.repository.PokemonRepository
import dagger.hilt.android.AndroidEntryPoint
import io.objectbox.BoxStore
import javax.inject.Inject

@AndroidEntryPoint
class PokemonTeamService: Service(){
    private val binder = PokemonTeamBinder()

    inner class PokemonTeamBinder : Binder(){
        fun getService(): PokemonTeamService = this@PokemonTeamService
    }

    override fun onBind(intent: Intent): IBinder = binder

    @Inject
    lateinit var pokemonRepository: PokemonRepository

    @Inject
    lateinit var objectBoxRepository: BoxStore

    val liveTeams = MutableLiveData<List<Team>>()

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

    fun getTeams(){
        val teamBox = objectBoxRepository.boxFor(Team::class.java)
        liveTeams.value = teamBox.all
    }

    fun createTeam(name:String){
        val teamBox = objectBoxRepository.boxFor(Team::class.java)
        teamBox.put(Team(name))
        getTeams()
    }

    fun addPokemonToTeam(team: Team,pokemon: Pokemon){
        val teamBox = objectBoxRepository.boxFor(Team::class.java)

        if(team.pokemons.size >= 6){
            Log.e("addPokemon","O time ja possui 6 pokemons, não é possivel adicionar mais")
            return
        }
        team.pokemons.add(pokemon)
        teamBox.put(team)
    }

    fun getPokemonTeam(id: Long):List<Pokemon>{
        val teamBox = objectBoxRepository.boxFor(Team::class.java)
        val team = teamBox.get(id)
        return team.pokemons
    }

    fun findOnTeam(pokemon: Pokemon,id: Long): Boolean {

        val pokemons = getPokemonTeam(id)
        for (pkm in pokemons){
            if(pkm.id == pokemon.id) return true
        }
        return false
    }

    fun removePokemonTeam(pokemon: Pokemon) {
        val pokemonBox = objectBoxRepository.boxFor(Pokemon::class.java)
        pokemonBox.query(Pokemon_.id.equal(pokemon.id)).build().remove()
    }

    fun removeTeam(team: Team){
        val teamBox = objectBoxRepository.boxFor(Team::class.java)
        teamBox.remove(team)
        getTeams()
    }

    fun clearTeam(team: Team) {
        val teamBox = objectBoxRepository.boxFor(Team::class.java)
        team.pokemons.clear()
        teamBox.put(team)

    }
}