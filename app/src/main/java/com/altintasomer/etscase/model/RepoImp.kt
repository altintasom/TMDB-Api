package com.altintasomer.etscase.model

import com.altintasomer.etscase.di.TMDBApi
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import retrofit2.Response
import javax.inject.Inject

class RepoImp @Inject constructor(private val api : TMDBApi ) : Repo{
    override suspend fun searchFilms(query: String?,page :String?): Response<PopularFilm> = api.searchFilms(query = query,page = page)
    override suspend fun getFilmDetail(tv_id: String): Response<FilmDetail> = api.getFilmDetail(tv_id = tv_id)
    override suspend fun getFilms(page: String?): Response<PopularFilm> = api.getFilms(page = page)


}