package com.example.jobsity.main_screen.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.local.ShowRepository
import com.example.jobsity.data.local.entities.ShowItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowViewModel
@Inject constructor(
    private val showRepository: ShowRepository
) : ViewModel() {

    private val _showState = MutableStateFlow<ShowState>(ShowState.Loading)
    val showState = _showState.asStateFlow()

    private val _searchList = MutableLiveData<List<ShowItem>>()
    val searchList: LiveData<List<ShowItem>> = _searchList

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState = _detailsState.asStateFlow()

    private val _favoriteList = MutableLiveData<List<ShowItem>>()
    val favoriteList: LiveData<List<ShowItem>> = _favoriteList

    init {
        viewModelScope.launch {
            showRepository.getShows()
                .onStart { emit(ShowState.Loading) }
                .catch { _showState.value = ShowState.Error(it) }
                .collect {
                    _showState.value = it
                }
        }
    }

    fun searchShowName(query: String) {
        viewModelScope.launch {
            showRepository.searchShowName(query).collect {
                _searchList.value = it
            }
        }
    }

    fun getShowWithEpisodes(id: Int) {
        viewModelScope.launch {
            showRepository.getShowWithEpisodes(id)
                .onStart { emit(DetailsState.Loading) }
                .catch {
                    _detailsState.value = DetailsState.Error(it)
                }
                .collect {
                    _detailsState.value = it
                }
        }
    }

    fun setFavoriteShow(id: Int) {
        viewModelScope.launch {
            showRepository.favoriteShow(id)
                .collect {
                    _detailsState.value = it
                }
        }
    }

    fun getAllFavorites() {
        viewModelScope.launch {
            showRepository.getAllFavoriteShows()
                .collect {
                    _favoriteList.value = it
                }
        }
    }
}

sealed interface ShowState {
    object Loading : ShowState
    class Error(val throwable: Throwable) : ShowState
    class ShowsLoaded(val list: List<ShowItem>) : ShowState
}

sealed interface DetailsState {
    object Loading : DetailsState
    class Error(val throwable: Throwable) : DetailsState
    class ShowWithEpisodes(val list: List<ItemDelegate>) : DetailsState
    class UpdateShow(val list: List<ItemDelegate>) : DetailsState
}