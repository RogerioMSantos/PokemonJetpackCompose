package com.example.app.entities

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class Team (
    val name: String = "",
    var pokemons: MutableList<Pokemon> = mutableListOf()
){
    @Id
    var id: Long = 0
}