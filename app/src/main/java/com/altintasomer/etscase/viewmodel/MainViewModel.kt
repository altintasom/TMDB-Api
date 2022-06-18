package com.altintasomer.etscase.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.altintasomer.etscase.R
import com.altintasomer.etscase.model.RepoImp
import com.altintasomer.etscase.model.network.Result
import com.altintasomer.etscase.utils.Event
import com.altintasomer.etscase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repoImp: RepoImp
) : ViewModel() {

    private val _results = MutableLiveData<Event<Resource<List<Result>>>>()
    val results get() = _results
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

        viewModelScope.launch(Dispatchers.IO) {
            _results.postValue(Event(Resource.loading()))
            _isLoading = true
            val response = try {
                repoImp.searchFilms(searchQuery, currentPage.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                _results.postValue(Event(Resource.error(context.getString(R.string.error_connection))))
                return@launch
            }


            if (response.isSuccessful) {
                val resultList = response.body()?.results
                response.body()?.total_pages?.let {
                    _totalPages = it
                }
                _isLastPage = currentPage == TOTAL_PAGES
                resultList?.let {
                    oldSearchResultList.addAll(it)
                    _results.postValue(Event(Resource.success(oldSearchResultList)))
                }
                _isLoading = false

            } else {
                _isLoading = false
                _results.postValue(Event(Resource.error(context.getString(R.string.error))))
            }

        }
    }


    fun getFilms() {
        fromSearching = false
        viewModelScope.launch(Dispatchers.IO) {
            _results.postValue(Event(Resource.loading()))
            _isLoading = true
            val response = try {
                repoImp.getFilms(currentPage.toString())
            } catch (e: Exception) {
                e.printStackTrace()
                return@launch
            }

            if (response.isSuccessful) {

                val resultList = response.body()?.results
                response.body()?.total_pages?.let {
                    _totalPages = it
                }

                Log.d(TAG, "getFilm: ${TOTAL_PAGES}")
                _isLastPage = currentPage == TOTAL_PAGES
                resultList?.let {
                    oldResultList.addAll(it)

                }
                _isLoading = false
                _results.postValue(Event(Resource.success(oldResultList)))

            } else {
                _isLoading = false
                _results.postValue(Event(Resource.error(context.getString(R.string.error))))
            }

        }
    }

    fun clearSearchList(){
        currentPage = 1
        oldSearchResultList.clear()
    }


}