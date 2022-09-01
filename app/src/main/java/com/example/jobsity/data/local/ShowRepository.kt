package com.example.jobsity.data.local

import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.main_screen.view_model.DetailsState
import com.example.jobsity.main_screen.view_model.ShowState
import kotlinx.coroutines.flow.Flow

interface ShowRepository {

    suspend fun getShows(): Flow<ShowState>

    suspend fun getShowWithEpisodes(id: Int): Flow<DetailsState>

    suspend fun searchShowName(query: String): Flow<List<ShowItem>>

    suspend fun favoriteShow(id: Int): Flow<DetailsState>

    suspend fun getAllFavoriteShows(): Flow<List<ShowItem>>
}