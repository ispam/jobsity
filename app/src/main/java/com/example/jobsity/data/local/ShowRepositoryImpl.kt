package com.example.jobsity.data.local

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.TVMazeService
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.details.delegates.SeasonDelegate
import com.example.jobsity.details.view_model.DetailsState
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
        return flow{
            val service = tvMazeService.getShowWithEpisodes(id)
            if (service.isSuccessful) {
                service.body()?.let {
                    val list = mutableListOf<ItemDelegate>()
                    list.add(it.copy(showSummary = true))
                    it.embedded?.let { embedded ->
                        embedded.episodes.groupBy { it.season }.forEach { (season, episodes) ->
                            list.add(SeasonDelegate.Model("Season $season"))
                            episodes.forEach { episode ->
                                list.add(EpisodeItem(
                                    episode.id,
                                    episode.name,
                                    episode.season,
                                    episode.number,
                                    episode.runtime,
                                    episode.rating,
                                    episode.image,
                                    episode.summary
                                ))
                            }
                        }
                    }

                    emit(DetailsState.ShowWithEpisodes(list))
                }
            } else {
                emit(DetailsState.Error(Throwable("Illegal State")))
            }
        }.flowOn(dispatcher)
    }

    override suspend fun searchShowName(query: String): Flow<List<ShowItem>> {
        return flow {
            emit(currentShows.filter{ it.name.contains(query, true) })
        }.flowOn(dispatcher)
    }
}
