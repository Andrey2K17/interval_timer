package ru.grinin.intervaltimer.entities

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.grinin.domain.entities.TrainingDomain

@Serializable
@Parcelize
data class TrainingUI(
    val id: Int,
    val intervals: List<IntervalUI>
): Parcelable

fun TrainingDomain.toUI() = TrainingUI(id, intervals.map { it.toUI() })

val TrainingNavType = object : NavType<TrainingUI>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, TrainingUI::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): TrainingUI = Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: TrainingUI) =
        bundle.putParcelable(key, value)

    override fun serializeAsValue(value: TrainingUI): String = Json.encodeToString(value)

    override val name: String = "trainingValue"
}