package com.example.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonListResponse (
    val count: Int,
    var next: String,
    val results: MutableList<Pokemon?>

) {
    @Serializable
    data class Pokemon(var name: String, val url: String) {
        var id: Int = 0
        var sprite: String = ""

        init {
            val regex = """/pokemon/(\d+).*""".toRegex()
            this.id = regex.find(url)?.groupValues?.get(1)!!.toInt()
            this.sprite = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${this.id}.png"
        }
    }
}
