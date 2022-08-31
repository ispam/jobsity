package com.example.jobsity.data.local

import com.example.jobsity.main_screen.view_model.MainState
import kotlinx.coroutines.flow.Flow

interface MainRepository {

    suspend fun getShows(): Flow<MainState>
}