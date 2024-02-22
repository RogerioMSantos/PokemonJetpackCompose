package com.example.app.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.vector.ImageVector
import com.example.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.*

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent{
            AppTheme {

                Surface {

                    listPokemon()
                }
            }
        }

    }



    fun listPokemon(){
        Intent(this@MainActivity,PokemonListActivity::class.java).also {
            startActivity(it)
        }
    }
}
