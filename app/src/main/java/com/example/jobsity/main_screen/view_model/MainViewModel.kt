package com.example.jobsity.main_screen.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jobsity.data.local.MainRepository
import com.example.jobsity.data.local.entities.ShowItem
import com.example.jobsity.utils.handleErrors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _mainState = MutableStateFlow<MainState>(MainState.Loading)
    val mainState = _mainState.asStateFlow()

    init {
        viewModelScope.launch {
            mainRepository.getShows()
                .onStart { emit(MainState.Loading) }
                .handleErrors { _mainState.value = MainState.Error(it) }
                .collect {
                _mainState.value = it
            }
        }
    }

}

sealed interface MainState {
    object Loading: MainState
    class Error(val throwable: Throwable): MainState
    class ShowsLoaded(val list: List<ShowItem>): MainState
}