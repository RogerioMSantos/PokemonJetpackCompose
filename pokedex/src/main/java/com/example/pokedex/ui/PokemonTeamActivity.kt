package com.example.pokedex.ui

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import com.example.pokedex.entities.Pokemon
import com.example.pokedex.entities.Team
import com.example.pokedex.service.PokemonTeamService
import com.example.pokedex.ui.theme.AppTheme
import io.objectbox.relation.ToMany
import kotlinx.coroutines.runBlocking

class PokemonTeamActivity : ComponentActivity() {

    private var mService: PokemonTeamService? = null
    private val mBound = MutableLiveData(false)


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {

            val binder = service as PokemonTeamService.PokemonTeamBinder
            mService = binder.getService()
            mBound.value = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound.value = false
        }
    }


    override fun onStart() {
        super.onStart()
        Intent(this, PokemonTeamService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = mBound.observeAsState(false)
            AppTheme {
                var openDialog by remember {
                    mutableStateOf(false)
                }


                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {
                            NavigationBarActivity.CreateNavigationBar(this)
                        }
                    ) {
                        Column {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd
                            ) {
                                Button(onClick = { openDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Filled.Add,
                                        contentDescription = "Cria um novo time"
                                    )
                                }

                            }
                            if (state.value) {
                                mService?.getTeams()
                                TeamList(mService) {
                                    openDialog = true
                                }
                                if (openDialog) {
                                    CreateTeam(mService) {
                                        openDialog = false
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
    private fun TeamList(mService: PokemonTeamService?, openDialog: () -> Unit) {
        val liveTeam = mService!!.liveTeams
        val teams by liveTeam.observeAsState(initial = emptyList())
        if (teams.isEmpty()) {
            TeamNotFound(openDialog)
        }
        LazyColumn (contentPadding = PaddingValues(bottom = 70.dp)){
            itemsIndexed(teams) { _, team ->
                var openAddPokemon by remember {
                    mutableStateOf(false)
                }
                TeamItemCard(team){
                    openAddPokemon = true
                }
                if (openAddPokemon) {

                    AddPokemonOnTeam(mService,team) {
                        openAddPokemon = false

                    }
                }
            }
        }

    }

     @Composable
    private fun TeamItemCard(team: Team,openAddPokemon: ()->Unit) {
        Card(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth()
                    .size(height = 50.dp, width = 10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(text = team.name)
                    Row {
                        IconButton(
                            onClick = {
                            openAddPokemon()
                            },
                            modifier = Modifier
                                .size(20.dp)

                        ){
                            Icon(imageVector = Icons.Filled.Add,
                                contentDescription = "Adiciona um pokemon ao time"
                            )

                        }
                        IconButton(
                            onClick = {
                                mService?.removeTeam(team)
                            },
                            modifier = Modifier
                                .size(20.dp)

                        ){
                            Icon(imageVector = Icons.Filled.Clear,
                                contentDescription = "Exclui um time"
                            )

                        }

                    }
                }
            }
            TeamPokemonItem(team.pokemons)
        }
    }

    @Composable
    private fun TeamPokemonItem(pokemons: ToMany<Pokemon>) {
        Row(horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()) {
            for (i in 0..6) {
                if (pokemons.size > i) {
                    AsyncImage(
                        model = pokemons[i].sprite,
                        contentDescription = "Imagem do pokemon ${pokemons[i].name}",
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun TeamNotFound( openDialog: () -> Unit) {
        Box(contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()) {
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()) {
                Text(text = "Nenhum time foi criado ainda! CLique aqui para criar um!")
                Button(onClick = {
                    openDialog()

                }) {
                    Text(text = "Criar time")
                }
            }
        }
    }
}

@Composable
private fun CreateTeam(mService: PokemonTeamService?,closeDialog: () -> Unit) {
    var teamName by remember {
        mutableStateOf("")
    }
    AlertDialog(
        title = {
            Text(text = "Criar time")
        },
        text = {
            TextField(value = teamName,
                onValueChange = {
                    teamName = it
                })
        },
        onDismissRequest = {
            teamName = ""
            closeDialog()
        },
        confirmButton = {
            Button(
                onClick = {
                    mService?.createTeam(teamName)
                    closeDialog()
                }
            ) {
                Text("Criar")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    teamName = ""
                    closeDialog()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun AddPokemonOnTeam(mService: PokemonTeamService?,team: Team,closeDialog: () -> Unit){
    val addPokemons = mutableListOf<Pokemon>()
    team.pokemons.forEach{
        pokemon -> addPokemons.add(pokemon)
    }
    val livePokemons = mService!!.livePokemons
    val pokemons by livePokemons.observeAsState(initial = emptyList())
    runBlocking {
        mService.getPokemons()
    }

    AlertDialog(
        title = {
                Text(text = "Escolha os Pokemons")
        },
        modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 16.dp),
        text = {
            LazyColumn{
                itemsIndexed(pokemons) { index, pokemon ->
                    var checkStatus by remember {
                        mutableStateOf(team.pokemons.contains(pokemon!!))
                    }
                    Row {
                        Checkbox(
                            checked = checkStatus,
                            onCheckedChange = { checked ->
                                if(!checked){
                                    addPokemons.remove(pokemon)
                                    checkStatus = !checkStatus
                                }
                                else{
                                    if(addPokemons.size < 6 ) {
                                        addPokemons.add(pokemon!!)
                                        checkStatus = !checkStatus
                                    }
                                }

                            }
                        )
                        PokemonListCard(pokemon = pokemon!!)
                    }
                    if (index == pokemons.lastIndex) {
                        runBlocking {
                            mService.getPokemons()
                        }
                    }
                }
            }

        },
        dismissButton = {
            Button(
                onClick = {
                    addPokemons.clear()
                    closeDialog()
                }
            ) {
                Text("Cancelar") }
        },
        onDismissRequest = {
            closeDialog()
            addPokemons.clear()
        },
        confirmButton = {
            Button(
                onClick = {
                    closeDialog()
                    mService.clearTeam(team)
                    addPokemons.forEach{
                        pokemon -> mService.addPokemonToTeam(team, pokemon )
                    }
                }
            ) {
                Text("Confirmar") }
        }
    )
}

@Composable
fun PokemonListCard(pokemon: Pokemon) {
    Box{
       Row {
           Box {
               AsyncImage(
                   model = pokemon.sprite,
                   contentDescription = "Imagem do pokemon ${pokemon.name}",
                   modifier = Modifier.size(100.dp)
               )
           }
           Box(
               contentAlignment = Alignment.CenterStart
           ) {
               Text(text = pokemon.name.replaceFirstChar { it.uppercaseChar() })
           }
       }
    }
}

