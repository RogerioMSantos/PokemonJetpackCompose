package com.example.pokedex.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToMany

@Entity
data class Team (
    val name: String = ""

){
    @Id
    var id: Long = 0
    lateinit var pokemons: ToMany<Pokemon>
}