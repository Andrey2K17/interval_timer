package ru.grinin.intervaltimer.screens.training

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import ru.grinin.intervaltimer.entities.TimerNavType
import ru.grinin.intervaltimer.entities.TimerUI
import kotlin.reflect.typeOf

@Serializable
data class TrainingRoute(
    val timer: TimerUI
)

fun NavController.navigateToTraining(timer: TimerUI) {
    navigate(TrainingRoute(timer))
}

fun NavGraphBuilder.trainingScreen() {
    composable<TrainingRoute>(
        typeMap = mapOf(
            typeOf<TimerUI>() to TimerNavType
        )
    ) {
        TrainingRoute()
    }
}