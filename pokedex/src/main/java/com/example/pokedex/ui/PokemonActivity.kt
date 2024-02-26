package com.example.pokedex.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.service.PokemonService
import com.example.pokedex.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking


class PokemonActivity : ComponentActivity() {

    private var mService: PokemonService? = null
    private val mBound = MutableLiveData(false)


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as PokemonService.PokemonBinder
            mService = binder.getService()
            mBound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, PokemonService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = mBound.observeAsState(false)
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (state.value) {
                        val extras = intent.extras
                        if (extras != null) {
                            val id = extras.getString("pokemon")
                            var pokemon: Pokemon
                            runBlocking {
                                pokemon = mService?.getPokemon(id!!)!!

                            }
                            ShowPokemon(pokemon)
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ShowEvolutions(evolutions: List<Pokemon>,isEnd: Boolean) {
        val lazyListState = rememberLazyGridState()
        val newList = evolutions.toMutableList()
        val limite = if (evolutions.size % 3 > 0 && isEnd) 3 - evolutions.size % 3
                    else 0
        val qttItems = evolutions.size + limite
        if(isEnd) {
            for (i in 1..limite) {

                newList.add(evolutions.size - evolutions.size % 3, Pokemon())
            }
        }
        LazyVerticalGrid(
            state = lazyListState,
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.End,

        ) {
            items(qttItems) { index ->
                if(newList[index].sprite.isNotEmpty()) {
                    Card(modifier = Modifier
                        .clickable {
                            val intent = Intent(this@PokemonActivity, PokemonActivity::class.java)
                            intent.putExtra("pokemon", newList[index].name)
                            startActivity(intent)
                        }
                        .background(MaterialTheme.colorScheme.background)
                        .size(80.dp),
                        shape = RoundedCornerShape(100.dp)) {
                        Box(
                            contentAlignment = Alignment.CenterEnd,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            AsyncImage(
                                model = newList[index].sprite,
                                contentDescription = "Imagem do pokemon ${newList[index].name}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .wrapContentHeight(),
                                alignment = Alignment.Center,
                            )
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun ShowPokemon(pokemon: Pokemon) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AsyncImage(
                        model = pokemon.sprite,
                        contentDescription = "Imagem do pokemon ${pokemon.name}",
                        modifier = Modifier
                            .size(250.dp)
                            .fillMaxWidth(),
                        alignment = Alignment.TopCenter
                    )
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Text(text = pokemon.name.replaceFirstChar { it.uppercaseChar() })
                }
                Spacer(modifier = Modifier.size(50.dp))
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,

                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {

                                Text(text = "Tipos")
                                LazyRow {
                                    items(pokemon.types) { type ->
                                        SvgImageSample(type!!)
                                    }
                                }
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            if (pokemon.evolutions.evolveFrom.isNotEmpty()
                                || pokemon.evolutions.evolveTo.isNotEmpty()
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = "Evolui")
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                                            if (pokemon.evolutions.evolveFrom.isNotEmpty()) {
                                                Column {
                                                    Text(text = "De:")
                                                    ShowEvolutions(evolutions = pokemon.evolutions.evolveFrom,false)
                                                }
                                            }
                                        }
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.CenterEnd
                                        ) {
                                            if (pokemon.evolutions.evolveTo.isNotEmpty()) {
                                                Column (horizontalAlignment = Alignment.End){
                                                    Text(text = "Para:")
                                                    ShowEvolutions(evolutions = pokemon.evolutions.evolveTo,true)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SvgImageSample(type: String) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .decoderFactory(SvgDecoder.Factory())
            .data(Pokemon.getSpriteType(type))
            .size(100)
            .build()
    )
    Image(
        painter = painter,
        contentDescription = null,
        colorFilter = ColorFilter.tint(Pokemon.color[type]!!)
    )
}