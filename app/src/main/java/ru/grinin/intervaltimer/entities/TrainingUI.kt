package ru.grinin.intervaltimer.entities

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.grinin.domain.entities.TimerDomain

@Serializable
@Parcelize
data class TimerUI(
    val timerId: Int,
    val title: String,
    val totalTime: Int,
    val intervals: List<IntervalUI>
): Parcelable

fun TimerDomain.toUI() = TimerUI(timerId, title, totalTime, intervals.map { it.toUI() })

val TimerNavType = object : NavType<TimerUI>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, TimerUI::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): TimerUI = Json.decodeFromString(value)

    override fun put(bundle: Bundle, key: String, value: TimerUI) =
        bundle.putParcelable(key, value)

    override fun serializeAsValue(value: TimerUI): String = Json.encodeToString(value)

    override val name: String = "timerValue"
}