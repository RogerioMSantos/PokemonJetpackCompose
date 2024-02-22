package com.example.app.ui

//import com.example.app.data.remote.ClientApplication
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import com.example.app.entities.Pokemon
import com.example.app.service.PokemonListService
import com.example.app.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class PokemonListActivity : ComponentActivity() {

    private var mService: PokemonListService? = null
    private val mBound = MutableLiveData(false)

    private var searching = false

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as PokemonListService.PokemonListBinder
            mService = binder.getService()
            mBound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound.value = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, PokemonListService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unbindService(connection)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val state = mBound.observeAsState(false)
            var loading by remember {
                mutableStateOf(true)
            }
            AppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    if (loading) {
                        Loading(state) {
                            loading = it
                        }
                    } else {
                        Column {
                            PokemonSearch(mService)
                            Button(onClick = {
                                mService?.getPokemonTeam()
                            }) {

                            }
                            PokemonsList(mService)

                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PokemonSearch(mService: PokemonListService?){
        var text by remember {
            mutableStateOf("")
        }
        Box (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            contentAlignment = Alignment.CenterStart
            ){
            Row(horizontalArrangement = Arrangement.SpaceAround,
                    modifier = Modifier.fillMaxWidth()) {
                TextField(
                    modifier = Modifier.fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(50.dp),
                    value = text,
                    onValueChange = { text = it },
                    maxLines = 1,
                    placeholder = {
                            Text(text = "Digite o nome ou o id do pokemon")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent)
                )
                Button(onClick = {
                    if(!searching) {
                        searching = true
                        if(text == "")
                            searching = false
                        else
                            runBlocking { mService?.findPokemon(text) }
                    }
                    else{
                        if(text == ""){
                            searching = false
                            mService?.resetList()
                            runBlocking { mService?.getPokemons() }
                        }
                        else
                            runBlocking { mService?.findPokemon(text) }
                    }

                }) {
                    Text(text = "Buscar")
                }
            }

        }
    }

    @Composable
    fun PokemonsList(mService: PokemonListService?) {
        if(!searching) {
            runBlocking {
                mService!!.getPokemons()
            }
        }
        val livePokemons = mService!!.livePokemons
        val pokemons by livePokemons.observeAsState(initial = emptyList())

        LazyColumn {
            itemsIndexed(pokemons) { index, pokemon ->
                PokemonListCard(pokemon = pokemon!!)
                if (index == pokemons.lastIndex && !searching) {
                    runBlocking {
                        mService.getPokemons()
                    }
                }
            }
        }
        if(pokemons.isEmpty() && searching){
            PokemonNotFound()
        }
    }

    @Composable
    private fun PokemonNotFound() {
        Box(modifier = Modifier.fillMaxSize()){
            Text(text = "Nenhum pokemon com esse nome ou id encontrado")
        }
    }

    @Composable
    fun PokemonListCard(pokemon: Pokemon) {
        Card(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .background(MaterialTheme.colorScheme.background)
                .padding(10.dp)
                .clickable {
                    val intent = Intent(this@PokemonListActivity, PokemonActivity::class.java)
                    intent.putExtra("pokemon", pokemon.name);
                    startActivity(intent)
                }
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.size(90.dp),
                        contentAlignment = Alignment.CenterStart) {
                        Text(
                            text = "#${pokemon.id}",
                            fontSize = 35.sp,
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
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(end = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {

                        Box(
//                        modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            Text(text = pokemon.name.replaceFirstChar { it.uppercaseChar() })
                        }
                        Box(modifier = Modifier.clickable {
                            mService?.addPokemonToTeam(pokemon)
                        }){
                            Text(text = "+")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun Loading(state: State<Boolean>, loadingState: (Boolean) -> Unit) {
        var progress by remember { mutableFloatStateOf(0.1f) }
        var loading by remember {
            mutableStateOf(false)
        }
        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = progress
            )
        }
        LaunchedEffect(key1 = progress) {
            if (!loading) {
                loading = true

                scope.async {
                    loadProgress { newProgress ->
                        progress = newProgress
                    }
                }.await()
            }
            if (progress >= 1f && state.value) {
                loadingState(false)
            }
        }
    }

    suspend fun loadProgress(updateProgress: (Float) -> Unit) {
        for (i in 1..100) {
            updateProgress(i.toFloat() / 100)
            delay(1)
        }
    }


}
