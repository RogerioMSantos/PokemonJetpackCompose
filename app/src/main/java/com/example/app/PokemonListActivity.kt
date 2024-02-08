package com.example.app

import android.app.Activity
import android.os.Bundle
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import ch.qos.logback.core.net.server.Client
import coil.compose.AsyncImage
import com.example.app.data.remote.ClientApplication
import com.example.app.data.remote.GetService
import com.example.app.data.remote.GetServiceImpl
import com.example.app.data.remote.dto.GetPokemonListRequest
import com.example.app.data.remote.dto.GetPokemonListResponse
import com.example.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PokemonListActivity() : AppCompatActivity(){

    @Inject
    lateinit var clientApplication : ClientApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        val client = clientApplication.client
        super.onCreate(savedInstanceState)
        val viewModel = ViewModelProvider(this)[PokemonViewModel::class.java]
        val serviceImpl = GetServiceImpl(client)
        setContent {
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PokemonsList(viewModel = viewModel,serviceImpl)
                }
            }
        }
    }

    @Composable
    fun PokemonsList(viewModel:PokemonViewModel,serviceImpl: GetServiceImpl){
        runBlocking {
            viewModel.getPokemons(serviceImpl)
        }
        val livePokemons = viewModel.livePokemons
        val pokemons by livePokemons.observeAsState(initial = emptyList())
        LazyColumn{
            itemsIndexed(pokemons) {index,pokemon ->
                PokemonListCard(pokemon = pokemon!!)
                if(index == pokemons.lastIndex){
                    runBlocking {
                        viewModel.getPokemons(serviceImpl)
                    }

                }
            }

        }
    }

    @Composable
    fun PokemonListCard(pokemon: GetPokemonListResponse.Pokemon){
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
}
