package com.altintasomer.etscase.model

import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import retrofit2.Response

interface Repo {

    suspend fun searchFilms(query: String?,page : String?) : Response<PopularFilm>
    suspend fun getFilmDetail(tv_id : String) : Response<FilmDetail>
    suspend fun getFilms(page: String?) : Response<PopularFilm>
}