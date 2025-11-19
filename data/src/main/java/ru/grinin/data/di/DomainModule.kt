package ru.grinin.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.grinin.domain.repositories.TrainingRepository
import ru.grinin.domain.usecases.GetTrainingUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetTrainingUseCase(
        repository: TrainingRepository
    ): GetTrainingUseCase {
        return GetTrainingUseCase(repository)
    }
}