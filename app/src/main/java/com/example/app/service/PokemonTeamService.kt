package com.example.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.app.entities.Pokemon
import com.example.app.entities.Pokemon_
import com.example.app.entities.Team
import com.example.app.entities.Team_
import com.example.app.repository.PokemonRepository
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
}