package com.example.app.entities

import com.example.app.data.remote.dto.GetPokemonResponse
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonUnwrapped
import com.github.pozo.KotlinBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Pokemon (val name: String,val url:String?){
    var id: Int = 0
    lateinit var sprite: String

    lateinit var spriteFemale: String
    lateinit var types: List<String?>
    lateinit var evolutions: List<Pokemon>

    constructor(id: Int, name: String, sprite: String, spriteFemale: String, types: List<String?>) : this(name,""){
        this.id = id
        this.sprite = sprite
        this.types = types
        this.spriteFemale = spriteFemale
    }

    constructor(id: Int, name: String, sprite: String, types: List<String?>) : this(name,""){
        this.id = id
        this.sprite = sprite
        this.types = types
    }


    init {
        if(url != null && url !="") {
            val regex = """/pokemon/(\d+).*""".toRegex()
            this.id = regex.find(url!!)?.groupValues?.get(1)!!.toInt()
            this.sprite =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${this.id}.png"
        }
    }
    data class Type (
        val type: GetPokemonResponse.Species?
    )

}