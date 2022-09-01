package com.example.jobsity.data.local

import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.TVMazeService
import com.example.jobsity.data.local.entities.EpisodeItem
import com.example.jobsity.details.delegates.SeasonDelegate
import com.example.jobsity.details.view_model.DetailsState
import com.example.jobsity.main_screen.view_model.MainState
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart

@ViewModelScoped
class MainRepositoryImpl(
    private val dispatcher: CoroutineDispatcher,
    private val tvMazeService: TVMazeService
) : MainRepository {

    override suspend fun getShows(): Flow<MainState> {
        return flow {
            val service = tvMazeService.getShows(1)

            if (service.isSuccessful) {
                service.body()?.let {
                    emit(MainState.ShowsLoaded(it))
                }
            } else {
                emit(MainState.Error(Throwable("IllegalState")))
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
                        embedded.episodes.groupBy { it.season }.forEach { season, episodes ->
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
                                    episode.summary,
                                    episode.ended
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
}
