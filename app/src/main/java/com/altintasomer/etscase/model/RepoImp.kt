package com.altintasomer.etscase.model

import com.altintasomer.etscase.di.TMDBApi
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import retrofit2.Response
import javax.inject.Inject

class RepoImp @Inject constructor(private val api : TMDBApi ) : Repo{
    override suspend fun getFilms(query: String?): Response<PopularFilm> = api.getFilms(query = query)
    override suspend fun getFilmDetail(tv_id: String): Response<FilmDetail> = api.getFilmDetail(tv_id = tv_id)


}