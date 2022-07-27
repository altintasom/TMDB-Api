package com.altintasomer.tmdbapi.data.repository

import com.altintasomer.tmdbapi.domain.repository.Repository
import com.altintasomer.tmdbapi.data.romete.TMDBApi
import com.altintasomer.tmdbapi.data.romete.dto.FilmDetailDto
import com.altintasomer.tmdbapi.data.romete.dto.PopularFilmDto
import retrofit2.Response
import javax.inject.Inject

class RepositoryImp @Inject constructor(private val api: TMDBApi) : Repository {

    override suspend fun searchFilms(query: String?, page: String?): PopularFilmDto? {
        return api.searchFilms(query = query)
    }

    override suspend fun getFilmDetail(tv_id: String): FilmDetailDto? {
        return api.getFilmDetail(tv_id)
    }


    override suspend fun getFilms(page: String?): PopularFilmDto? {
        return api.getFilms(page = page)

    }

}