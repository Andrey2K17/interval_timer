package ru.grinin.data.repositories

import kotlinx.coroutines.flow.Flow
import ru.grinin.data.api.TrainingApi
import ru.grinin.data.mappers.remote.toDomain
import ru.grinin.data.network.remoteCall
import ru.grinin.domain.entities.NetworkResult
import ru.grinin.domain.entities.TimerDomain
import ru.grinin.domain.repositories.TrainingRepository

class TrainingRepositoryImpl(
    private val api: TrainingApi
) : TrainingRepository {
    override fun getTraining(id: String): Flow<NetworkResult<TimerDomain>> = remoteCall(
        { api.getTraining(id) },
        { it.toDomain() }
    )
}