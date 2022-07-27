package com.altintasomer.tmdbapi.view.film_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.tmdbapi.common.ResourceState
import com.altintasomer.tmdbapi.domain.model.Film
import com.altintasomer.tmdbapi.domain.use_cases.get_films.GetFilmsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val getFilmsUseCase: GetFilmsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<ResourceState<List<Film>>>(ResourceState.Loading())
    val state: StateFlow<ResourceState<List<Film>>> = _state

    //Searching text
    var searchQuery = ""

    private var oldSearchResultList = mutableListOf<Film>()
    private val oldResultList = mutableListOf<Film>()

    //Response of the total pages from api
    private var _totalPages = 0
    val TOTAL_PAGES get() = _totalPages

    //checking  if it's the last page for pagination
    private var _isLastPage = false
    val IS_LAST_PAGES get() = _isLastPage
    private var _isLoading = false
    val isLoading get() = _isLoading

    //page number for pagination
    var currentPage = 1

    //checking request from searching
    var fromSearching = false

    //checking first searching from pagination and refresh adapter (if it is equal to true, adapter refresh)
    var fromSearchingFirst = false

    init {
       getFilmList()
    }

    fun clearSearchList() {
        currentPage = 1
        oldSearchResultList.clear()
    }

     fun getFilmList(){
        fromSearching = false

        getFilmsUseCase.getFilms(page = currentPage.toString()).onEach { result ->

            when(result){
                is ResourceState.Loading -> {
                    _state.value = ResourceState.Loading()
                }

                is ResourceState.Success -> {
                    result.data?.total_pages?.let { rTotalPages ->
                        _totalPages = rTotalPages
                        _isLastPage = currentPage == TOTAL_PAGES
                    }
                    val resultList = result.data?.films
                    resultList?.let {
                        oldResultList.addAll(it)
                    }
                    _state.value = ResourceState.Success( oldResultList)
                }

                is ResourceState.Error -> {
                    Log.e(TAG, "getFilmList: error")
                    _state.value = ResourceState.Error(result.message ?: "An unexpected error occurred")
                }
            }

        }.launchIn(viewModelScope)

    }

    fun searchGetFilms(query: String){
        fromSearching = true
        getFilmsUseCase.getSearchFilms(query = query,page = currentPage.toString()).onEach { result ->
            when(result){
                is ResourceState.Loading -> {
                    Log.d(TAG, "searchGetFilms: loading...")
                    _state.value = ResourceState.Loading()
                }
                is ResourceState.Success -> {
                    Log.d(TAG, "searchGetFilms: success")
                    _state.value = ResourceState.Success(result.data?.films)
                }

                is ResourceState.Error -> {
                    Log.e(TAG, "searchGetFilms: error")
                    _state.value = ResourceState.Error( result.message ?: "An unexpected error occurred")
                }
            }

        }.launchIn(viewModelScope)
    }

}