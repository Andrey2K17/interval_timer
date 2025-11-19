package ru.grinin.intervaltimer.screens.load

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

import kotlinx.serialization.Serializable
import ru.grinin.intervaltimer.screens.training.navigateToTraining

@Serializable
object LoadRoute


fun NavGraphBuilder.loadGraph(navController: NavController) {
    composable<LoadRoute> {
        LoadRoute(
            navToTraining = navController::navigateToTraining,
        )
    }
}