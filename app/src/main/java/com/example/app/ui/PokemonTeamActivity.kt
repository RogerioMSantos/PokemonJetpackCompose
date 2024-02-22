package com.example.app.ui

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import coil.compose.AsyncImage
import com.example.app.entities.Pokemon
import com.example.app.entities.Team
import com.example.app.service.PokemonListService
import com.example.app.service.PokemonTeamService
import com.example.app.ui.theme.AppTheme
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
                            Box(modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopEnd) {
                                Button(onClick = { openDialog = true }) {
                                    Icon(imageVector = Icons.Filled.Add,
                                        contentDescription = "Cria um novo time")
                                }

                            }
                            if(state.value) {
                                mService?.getTeams()
                                TeamList(mService){
                                    openDialog = true
                                }
                                if (openDialog){
                                    CreateTeam{
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
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateTeam(closeDialog: ()->Unit){
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
    private fun TeamList(mService: PokemonTeamService?,openDialog:() ->Unit) {
        val liveTeam = mService!!.liveTeams
        val teams by liveTeam.observeAsState(initial = emptyList())

        LazyColumn {
            itemsIndexed(teams) { index, team ->
                TeamItemCard(team)
            }
        }
        if (teams.isEmpty()) {
            TeamNotFound(openDialog)
        }
    }

    private @Composable
    fun TeamItemCard(team: Team) {
        Card(modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxWidth()
        ) {
            Box{
                Text(text = team.name)
            }
        }
    }

    @Composable
    private fun TeamNotFound(openDialog:() ->Unit) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Nenhum time foi criado ainda! CLique aqui para criar um!")
            Button(onClick = {
                openDialog()

            } ) {
                Text(text = "Criar time")
            }
        }
    }
}

