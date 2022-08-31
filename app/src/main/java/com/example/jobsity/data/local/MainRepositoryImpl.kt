package com.example.jobsity.data.local

import com.example.jobsity.data.TVMazeService
import com.example.jobsity.main_screen.view_model.MainState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

class MainRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val tvMazeService: TVMazeService
) : MainRepository {

    override suspend fun getShows(): Flow<MainState> {
        return flow {
            val service = tvMazeService.getShows(1)

            if (service.isSuccessful) {
                service.body()?.let {
                    emit(MainState.Loaded(it))
                }
            } else {
                emit(MainState.Error)
            }
        }.flowOn(dispatcher)
            .onStart { emit(MainState.Loading) }
            .catch { emit(MainState.Error) }
    }
}