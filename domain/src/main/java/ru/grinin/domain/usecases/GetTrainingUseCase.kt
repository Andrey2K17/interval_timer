package ru.grinin.domain.usecases

import ru.grinin.domain.repositories.TrainingRepository

class GetTrainingUseCase(private val trainingRepository: TrainingRepository) {
    operator fun invoke(id: String) = trainingRepository.getTraining(id)
}