package com.altintasomer.tmdbapi.domain.repository

import com.altintasomer.tmdbapi.data.romete.dto.FilmDetailDto
import com.altintasomer.tmdbapi.data.romete.dto.PopularFilmDto
import retrofit2.Response

interface Repository {
    //with flow
    suspend fun searchFilms(query: String?,page : String?) : PopularFilmDto?
    suspend fun getFilmDetail(tv_id : String) : FilmDetailDto?
    suspend fun getFilms(page: String?) : PopularFilmDto?
}