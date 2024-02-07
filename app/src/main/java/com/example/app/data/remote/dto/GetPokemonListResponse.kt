package com.example.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPokemonListResponse (
    val count: Int,
    var next: String,
    val results: MutableList<Pokemon?>

) {
    data class Pokemon(var name: String, val url: String) {
        var idx: Int = 0

        constructor(idx: Int) : this("", "") {
            this.idx = idx
        }
    }
}
