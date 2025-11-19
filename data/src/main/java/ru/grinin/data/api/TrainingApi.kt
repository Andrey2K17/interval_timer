package ru.grinin.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import ru.grinin.data.entities.TrainingRemote

interface TrainingApi {
    @GET("interval-timers/{id}")
    suspend fun getTraining(
        @Path("id") id: String,
    ): Response<TrainingRemote>
}