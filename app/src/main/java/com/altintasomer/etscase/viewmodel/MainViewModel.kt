package com.altintasomer.etscase.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.etscase.R
import com.altintasomer.etscase.model.RepoImp
import com.altintasomer.etscase.model.network.Result
import com.altintasomer.etscase.utils.Event
import com.altintasomer.etscase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repoImp: RepoImp
) : ViewModel() {

    private val _resultsFlow =
        MutableStateFlow<Event<Resource<List<Result>>>>(Event(Resource.success(arrayListOf())))
    val resultsFlow: StateFlow<Event<Resource<List<Result>>>> = _resultsFlow

    //Searching text
    var searchQuery = ""

    private var oldSearchResultList = mutableListOf<Result>()
    private val oldResultList = mutableListOf<Result>()

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
        getFilms()
    }

    fun searchFilms(searchQuery: String?) {
        fromSearching = true

        _resultsFlow.value = Event(Resource.loading())
        viewModelScope.launch {

            repoImp.searchFilmsWithFlow(query = searchQuery, page = currentPage.toString())
                .catch { e ->
                    _resultsFlow.value = Event(Resource.error(e.localizedMessage))
                }
                .collect {
                    _resultsFlow.value = Event(Resource.success(it?.results))
                }
        }
    }


    fun getFilms() {
        fromSearching = false
        viewModelScope.launch {

            _resultsFlow.value = Event(Resource.loading())
            _isLoading = true

            repoImp.getFilmsWithFlow(currentPage.toString())
                .catch { e ->
                    _resultsFlow.value = Event(Resource.error(e.localizedMessage))
                }
                .collect {
                    it?.total_pages?.let { rTotalPages ->
                        _totalPages = rTotalPages
                        _isLastPage = currentPage == TOTAL_PAGES
                    }
                    val resultList = it?.results

                    resultList?.let {
                        oldResultList.addAll(it)
                    }

                    _isLoading = false
                    _resultsFlow.value = Event(Resource.success(oldResultList))

                }

        }
    }

    fun clearSearchList() {
        currentPage = 1
        oldSearchResultList.clear()
    }

    fun <T> Flow<T>.handleError(): Flow<T> = flow {
        try {
            collect { value -> emit(value) }
        } catch (e: Throwable) {

        }
    }

}