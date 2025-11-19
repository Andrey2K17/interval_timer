package ru.grinin.intervaltimer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import ru.grinin.intervaltimer.screens.load.LoadRoute
import ru.grinin.intervaltimer.screens.load.loadGraph
import ru.grinin.intervaltimer.screens.training.trainingScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = LoadRoute,
    ) {
        loadGraph(navController)
        trainingScreen(navController)
    }
}