package com.euriconeto.markupcalculator.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.euriconeto.markupcalculator.R
import com.euriconeto.markupcalculator.ui.screen.MarkupListScreen
import com.euriconeto.markupcalculator.ui.screen.MarkupScreen
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

enum class RouterSet(val title: String, val icon: ImageVector) {
    MarkupScreen("Criar Markup", Icons.Filled.AddCircle),
    MarkupListScreen("Makrup Salvo", Icons.AutoMirrored.Filled.List)
}

@Serializable
data class MarkupRoute(val markupId: Int? = null)

@Serializable
object MarkupListRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarkupApp () {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                RouterSet.entries.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = {
                            Text(text = item.title)
                        },
                        selected = index == selectedItemIndex,
                        onClick = {
                            val route = when(item) {
                                RouterSet.MarkupScreen -> MarkupRoute()
                                RouterSet.MarkupListScreen -> MarkupListRoute
                            }
                            navController.navigate(route)
                            selectedItemIndex = index
                            scope.launch {
                                drawerState.close()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.app_name))
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            NavHostContainer(navController = navController, padding = innerPadding)
        }
    }
}

@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues,
) {
    NavHost(
        navController = navController,
        startDestination = MarkupRoute(),
        modifier = Modifier.padding(paddingValues = padding)
    ) {
        composable<MarkupRoute>{ backStackEntry ->
            val markup: MarkupRoute = backStackEntry.toRoute()
            MarkupScreen(
                markup = markup
            )
        }
        composable<MarkupListRoute>{
            MarkupListScreen(
                navController = navController
            )
        }
    }
}

