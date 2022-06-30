package com.altintasomer.etscase.model

import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface Repo {
    //with flow
    suspend fun searchFilmsWithFlow(query: String?,page : String?) : Flow<PopularFilm?>
    suspend fun getFilmDetailWithFlow(tv_id : String) : Flow<FilmDetail?>
    suspend fun getFilmsWithFlow(page: String?) : Flow<PopularFilm?>
}