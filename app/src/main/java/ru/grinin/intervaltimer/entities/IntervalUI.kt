package ru.grinin.intervaltimer.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ru.grinin.domain.entities.IntervalDomain

@Serializable
@Parcelize
data class IntervalUI(val duration: Int): Parcelable

fun IntervalDomain.toUI() = IntervalUI(duration)