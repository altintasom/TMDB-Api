package com.altintasomer.etscase.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.altintasomer.etscase.model.RepoImp
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.utils.Event
import com.altintasomer.etscase.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "DetailViewModel"

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repoImp: RepoImp
) : ViewModel() {

    private val _filmDetail =
        MutableStateFlow<Event<Resource<FilmDetail>>>(Event(Resource.loading()))
    val filmDetail get() = _filmDetail

    fun getFilmDetail(id: String) {

        viewModelScope.launch {

            _filmDetail.value = Event(Resource.loading())

            repoImp.getFilmDetailWithFlow(tv_id = id)
                .catch { e ->
                    filmDetail.value = Event(Resource.error(e.localizedMessage))
                }
                .collect {
                    _filmDetail.value = Event(Resource.success(it))
                }
        }
    }
}