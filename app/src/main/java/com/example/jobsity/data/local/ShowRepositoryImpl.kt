package com.example.jobsity.data.local

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.TVMazeService
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.details.delegates.SeasonDelegate
import com.example.jobsity.main_screen.view_model.DetailsState
import com.example.jobsity.main_screen.view_model.ShowState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

@ViewModelScoped
class ShowRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val tvMazeService: TVMazeService
) : ShowRepository {

    private var currentShows = mutableListOf<ShowItem>()
    private var showWithEpisodes = mutableListOf<ItemDelegate>()

    override suspend fun getShows(): Flow<ShowState> {
        return flow {
            val service = tvMazeService.getShows(1)

            if (service.isSuccessful) {
                service.body()?.let {
                    currentShows.addAll(it)
                    emit(ShowState.ShowsLoaded(it))
                }
            } else {
                emit(ShowState.Error(Throwable("IllegalState")))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getShowWithEpisodes(id: Int): Flow<DetailsState> {
        return flow {
            val service = tvMazeService.getShowWithEpisodes(id)
            if (service.isSuccessful) {
                service.body()?.let {
                    showWithEpisodes.clear()
                    showWithEpisodes.add(it.copy(showSummary = true))
                    it.embedded?.let { embedded ->
                        embedded.episodes.groupBy { it.season }.forEach { (season, episodes) ->
                            showWithEpisodes.add(SeasonDelegate.Model("Season $season"))
                            episodes.forEach { episode ->
                                showWithEpisodes.add(
                                    EpisodeItem(
                                        episode.id,
                                        episode.name,
                                        episode.season,
                                        episode.number,
                                        episode.runtime,
                                        episode.rating,
                                        episode.image,
                                        episode.summary
                                    )
                                )
                            }
                        }
                    }

                    emit(DetailsState.ShowWithEpisodes(showWithEpisodes))
                }
            } else {
                emit(DetailsState.Error(Throwable("Illegal State")))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun searchShowName(query: String): Flow<List<ShowItem>> {
        return flow {
            emit(currentShows.filter { it.name.contains(query, true) })
        }.flowOn(dispatcher)
    }

    override suspend fun favoriteShow(id: Int): Flow<DetailsState> {
        return flow {
            val showToUpdate = (showWithEpisodes.first() as ShowItem)
            showToUpdate?.let {
                it.copy(isFavorite = !it.isFavorite).apply {
                    val interimShow = this
                    showWithEpisodes[0] = interimShow
                    currentShows.find { it.id == id }?.apply {
                        val index = currentShows.indexOf(this)
                        if (index >= 0) {
                            currentShows[index] = interimShow
                        }
                    }
                    emit(DetailsState.UpdateShow(showWithEpisodes))
                }
            }
        }.flowOn(dispatcher)
    }

    override suspend fun getAllFavoriteShows(): Flow<List<ShowItem>> {
        return flow {
            emit(currentShows.filter { it.isFavorite }.map { it.copy(showSummary = false) })
        }.flowOn(dispatcher)
    }
}
