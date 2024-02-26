package com.example.pokedex.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.content.ContextCompat.startActivity

class NavigationBarActivity {

    data class BottomNavigationItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val navTo: Class<out ComponentActivity>
    )
    companion object {
        private fun createNavBarItems(): List<BottomNavigationItem> {
            return listOf(
                BottomNavigationItem(
                    title = "Favoritos",
                    selectedIcon = Icons.Filled.Star,
                    unselectedIcon = Icons.Outlined.Star,
                    navTo = MainActivity::class.java
                ),
                BottomNavigationItem(
                    title = "Lista",
                    selectedIcon = Icons.Filled.List,
                    unselectedIcon = Icons.Outlined.List,
                    navTo = PokemonListActivity::class.java
                ),
                BottomNavigationItem(
                    title = "Time",
                    selectedIcon = Icons.Filled.AddCircle,
                    unselectedIcon = Icons.Outlined.AddCircle,
                    navTo = PokemonTeamActivity::class.java
                )
            )
        }

        @Composable
        fun CreateNavigationBar(context: Context) {
            var initalIndex =   if(context::class.java == PokemonListActivity::class.java) 1
                                else 2
            val items = createNavBarItems()
            var selectedItem by rememberSaveable {
                mutableIntStateOf(initalIndex)
            }
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            Intent(context,item.navTo).also {
                                startActivity(context,it, Bundle())
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selectedItem == index)
                                    item.selectedIcon
                                else
                                    item.unselectedIcon,
                                contentDescription = item.title
                            )
                        })
                }
            }
        }
    }
}