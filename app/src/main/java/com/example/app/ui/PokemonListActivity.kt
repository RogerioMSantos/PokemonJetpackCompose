package com.example.app.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.app.service.PokemonListService
//import com.example.app.data.remote.ClientApplication
import com.example.app.entities.Pokemon
import com.example.app.repository.PokemonRepository
import com.example.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.lang.ref.WeakReference
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListActivity : ComponentActivity(){

    @Inject
    lateinit var pokemonRepository: PokemonRepository

    private var mService: PokemonListService? = null
    private var mBound: Boolean = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as PokemonListService.PokemonListBinder
            mService = binder.getService()
            mBound = true
            Toast.makeText(baseContext,"Bind feito? $mBound",Toast.LENGTH_SHORT).show()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(baseContext,"Fazendo bind",Toast.LENGTH_SHORT).show()
        val intent = Intent(this,PokemonListService::class.java)
        bindService(intent,connection, Context.BIND_AUTO_CREATE)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    while (!mBound){}
                    Greeting(name = "Bind feito")
//                    PokemonsList(mService,{mBound},pokemonRepository)
                }
            }
        }
    }

    @Composable
    fun PokemonsList(mService: PokemonListService?,checkBind : ()->Boolean, pokemonRepository: PokemonRepository){

        if(checkBind()) {

            runBlocking {

                mService!!.getPokemons()
            }
            val livePokemons = mService!!.livePokemons
            val pokemons by livePokemons.observeAsState(initial = emptyList())
            LazyColumn {
                itemsIndexed(pokemons) { index, pokemon ->
                    PokemonListCard(pokemon = pokemon!!)
                    if (index == pokemons.lastIndex) {
                        runBlocking {
                            mService.getPokemons()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PokemonListCard(pokemon: Pokemon){
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
        ) {
            Box (
                Modifier
                    .fillMaxSize()
                    .padding(5.dp)) {
                Row(modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box{
                        Text(
                            text = "#${pokemon.id}",
                            fontSize = 50.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFFE1D8C4)
                        )
                    }
                    Box {
                        AsyncImage(
                            model = pokemon.sprite,
                            contentDescription = "Imagem do pokemon ${pokemon.name}",
                            modifier = Modifier.size(100.dp)
                        )
                    }
                    Box (modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart){
                        Text(text = pokemon.name.replaceFirstChar { it.uppercaseChar() })
                    }
                }
            }
        }
    }
    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }
}


