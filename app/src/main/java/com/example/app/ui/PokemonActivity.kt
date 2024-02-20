package com.example.app.ui

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.app.entities.EvolutionChain
import com.example.app.entities.Pokemon
import com.example.app.service.PokemonService
import com.example.app.ui.theme.AppTheme
import kotlinx.coroutines.runBlocking


class PokemonActivity: ComponentActivity() {

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
        setContent{
            val state = mBound.observeAsState(false)
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ){
                    if(state.value) {
                        val extras = intent.extras
                        if(extras != null) {
                            val id = extras.getString("pokemon")
                            var pokemon : Pokemon
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
    fun ShowEvolutions(evolutions : List<Pokemon>){
        Row {
            for (i in evolutions) {
                Card(modifier = Modifier
                    .clickable {
                        val intent = Intent(this@PokemonActivity, PokemonActivity::class.java)
                        intent.putExtra("pokemon", i.name)
                        startActivity(intent)
                    }
                    .background(MaterialTheme.colorScheme.background)
                    .size(80.dp),
                    shape = RoundedCornerShape(100.dp)) {
                    Box (modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center){

                        AsyncImage(
                            model = i.sprite,
                            contentDescription = "Imagem do pokemon ${i.name}",
                            modifier = Modifier
                                .size(70.dp)
                                .fillMaxWidth(),
                            alignment = Alignment.TopCenter,
                        )
                    }
                }
            }
        }
    }
    @Composable
    fun ShowPokemon(pokemon: Pokemon){
        Box(
            Modifier
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                Box(contentAlignment = Alignment.TopCenter,
                    modifier = Modifier.fillMaxWidth()) {
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
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Tipos")
                        LazyRow {
                            items(pokemon.types) { type ->
                                SvgImageSample(type!!)
                            }
                        }
                        if(pokemon.evolutions.evolveFrom.isNotEmpty()
                            || pokemon.evolutions.evolveTo.isNotEmpty()){
                            Text(text = "Evolui")
                            Box(modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopCenter) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if(pokemon.evolutions.evolveFrom.isNotEmpty()) {
                                        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                                            Column {
                                                Text(text = "De:")
                                                ShowEvolutions(evolutions = pokemon.evolutions.evolveFrom)
                                            }
                                        }
                                    }
                                    if(pokemon.evolutions.evolveTo.isNotEmpty()) {
                                        Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                                            Column {
                                                Text(text = "Para:")
                                                ShowEvolutions(evolutions = pokemon.evolutions.evolveTo)
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