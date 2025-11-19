package ru.grinin.intervaltimer.screens.training

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.grinin.intervaltimer.entities.TrainingNavType
import ru.grinin.intervaltimer.entities.TrainingUI
import kotlin.reflect.typeOf

@Serializable
data class TrainingRoute(
    val training: TrainingUI
)

fun NavController.navigateToTraining(training: TrainingUI) {
    navigate(TrainingRoute(training))
}

fun NavGraphBuilder.trainingScreen(navController: NavController) {
    composable<TrainingRoute>(
        typeMap = mapOf(
            typeOf<TrainingUI>() to TrainingNavType
        )
    ) {
        TrainingRoute()
    }
}