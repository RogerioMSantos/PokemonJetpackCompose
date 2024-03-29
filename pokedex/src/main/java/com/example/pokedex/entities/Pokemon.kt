package com.example.pokedex.entities

import com.example.pokedex.ui.theme.type_color_bug
import com.example.pokedex.ui.theme.type_color_dark
import com.example.pokedex.ui.theme.type_color_dragon
import com.example.pokedex.ui.theme.type_color_electric
import com.example.pokedex.ui.theme.type_color_fairy
import com.example.pokedex.ui.theme.type_color_fight
import com.example.pokedex.ui.theme.type_color_fire
import com.example.pokedex.ui.theme.type_color_flying
import com.example.pokedex.ui.theme.type_color_ghost
import com.example.pokedex.ui.theme.type_color_grass
import com.example.pokedex.ui.theme.type_color_ground
import com.example.pokedex.ui.theme.type_color_ice
import com.example.pokedex.ui.theme.type_color_normal
import com.example.pokedex.ui.theme.type_color_poison
import com.example.pokedex.ui.theme.type_color_psychic
import com.example.pokedex.ui.theme.type_color_rock
import com.example.pokedex.ui.theme.type_color_steel
import com.example.pokedex.ui.theme.type_color_water
import com.fasterxml.jackson.annotation.JsonInclude
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Pokemon (val name: String = "",val url:String? = ""){
    @Id var primaryID: Long = 0
    var id: Int = 0
    lateinit var sprite: String

    var spriteFemale: String? = null
    lateinit var types: List<String?>
    @Transient
    lateinit var evolutions: EvolutionChain


    init {
        if(url != null && url !="") {
            var regex = """/pokemon/(\d+).*""".toRegex()
            if(!regex.containsMatchIn(url)){
                regex = """/pokemon-species/(\d+).*""".toRegex()
            }
            this.id = regex.find(url)?.groupValues?.get(1)!!.toInt()
            this.sprite =
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${this.id}.png"
        }
        else this.sprite = ""
    }

    fun getSpriteType(): List<String>{
        val sprites = mutableListOf<String>()
        types.forEach{

            sprites.add(sprite)
        }

        return sprites
    }

    companion object {
        val color = mapOf(
            "normal" to type_color_normal,
            "fire" to type_color_fire,
            "water" to type_color_water,
            "grass" to type_color_grass,
            "flying" to type_color_flying,
            "fighting" to type_color_fight,
            "poison" to type_color_poison,
            "electric" to type_color_electric,
            "ground" to type_color_ground,
            "rock" to type_color_rock,
            "psychic" to type_color_psychic,
            "ice" to type_color_ice,
            "bug" to type_color_bug,
            "ghost" to type_color_ghost,
            "steel" to type_color_steel,
            "dragon" to type_color_dragon,
            "dark" to type_color_dark,
            "fairy" to type_color_fairy
        )
        fun getSpriteType(type: String): String{
            return "https://raw.githubusercontent.com/duiker101/pokemon-type-svg-icons/5781623f147f1bf850f426cfe1874ba56a9b75ee/icons/${type}.svg"
        }
    }
}