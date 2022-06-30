package com.altintasomer.etscase.model

import com.altintasomer.etscase.di.TMDBApi
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepoImp @Inject constructor(private val api: TMDBApi) : Repo {

    override suspend fun searchFilmsWithFlow(query: String?, page: String?): Flow<PopularFilm?> {
        return flow {
           emit( api.searchFilms(query = query, page = page).body())
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getFilmDetailWithFlow(tv_id: String): Flow<FilmDetail?> {
        return flow {
            emit(api.getFilmDetail(tv_id).body())
        }.flowOn(Dispatchers.IO)
    }


    override suspend fun getFilmsWithFlow(page: String?): Flow<PopularFilm?> {
        return flow {
            emit(api.getFilms(page = page).body())
        }.flowOn(Dispatchers.IO)

    }

}