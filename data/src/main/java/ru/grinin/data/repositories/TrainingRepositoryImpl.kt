package ru.grinin.data.repositories

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.grinin.data.api.TrainingApi
import ru.grinin.data.mappers.remote.toDomain
import ru.grinin.data.network.remoteCall
import ru.grinin.domain.entities.IntervalDomain
import ru.grinin.domain.entities.NetworkResult
import ru.grinin.domain.entities.TrainingDomain
import ru.grinin.domain.repositories.TrainingRepository

class TrainingRepositoryImpl(
    private val api: TrainingApi
) : TrainingRepository {
    override fun getTraining(id: String): Flow<NetworkResult<TrainingDomain>> = remoteCall(
        { api.getTraining(id) },
        { it.toDomain() }
    )
}

class FakeTrainingRepository : TrainingRepository {
    override fun getTraining(id: String): Flow<NetworkResult<TrainingDomain>> {
        return flow {
            emit(NetworkResult.Loading())
            delay(1000L)
            emit(NetworkResult.Success(fakeData))
        }
    }
}

private val fakeData = TrainingDomain(
    id = 1,
    intervals = listOf(IntervalDomain(10), IntervalDomain(20))
)