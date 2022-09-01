package com.example.jobsity.details.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.common.delegate.ItemDelegate
import com.example.jobsity.data.local.ShowRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject constructor(
    private val showRepository: ShowRepository
) : ViewModel() {

    private val _detailsState = MutableStateFlow<DetailsState>(DetailsState.Loading)
    val detailsState = _detailsState.asStateFlow()

    fun getShowEpisodes(id: Int) {
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
}

sealed interface DetailsState {
    object Loading : DetailsState
    class Error(val throwable: Throwable) : DetailsState
    class ShowWithEpisodes(val list: List<ItemDelegate>) : DetailsState
}