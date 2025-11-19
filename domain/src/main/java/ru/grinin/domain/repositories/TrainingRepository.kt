package ru.grinin.domain.repositories

import kotlinx.coroutines.flow.Flow
import ru.grinin.domain.entities.NetworkResult
import ru.grinin.domain.entities.TrainingDomain

interface TrainingRepository {
    fun getTraining(id: String): Flow<NetworkResult<TrainingDomain>>
}