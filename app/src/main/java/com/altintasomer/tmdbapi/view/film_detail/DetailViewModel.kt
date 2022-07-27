package com.altintasomer.tmdbapi.view.film_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.tmdbapi.common.ResourceState
import com.altintasomer.tmdbapi.common.Utils
import com.altintasomer.tmdbapi.domain.model.FilmDetail
import com.altintasomer.tmdbapi.domain.use_cases.get_film.GetFilmUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "DetailViewModel"

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getFilmUseCase: GetFilmUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<ResourceState<FilmDetail>>(ResourceState.Loading())
    val state: StateFlow<ResourceState<FilmDetail>> = _state
    init {
        val id = savedStateHandle.get<Int>(Utils.PARAM_FILM_ID)
        id?.let {
            getFilm(id = it.toString())
        }
    }

    private fun getFilm(id: String){
        getFilmUseCase(id = id).onEach { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.d(TAG, "getFilm: Loading")
                    _state.value = ResourceState.Loading()
                }
                is ResourceState.Success -> {
                    Log.d(TAG, "getFilm: success")
                    _state.value = ResourceState.Success(result.data)
                }

                is ResourceState.Error -> {
                    Log.e(TAG, "getFilm: error")
                    _state.value = ResourceState.Error(message = result.message)
                }
            }

        }.launchIn(viewModelScope)
    }

}