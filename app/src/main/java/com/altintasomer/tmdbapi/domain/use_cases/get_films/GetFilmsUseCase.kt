package com.altintasomer.tmdbapi.domain.use_cases.get_films

import android.util.Log
import com.altintasomer.tmdbapi.common.ResourceState
import com.altintasomer.tmdbapi.data.romete.dto.FilmDto
import com.altintasomer.tmdbapi.data.romete.dto.toFilm
import com.altintasomer.tmdbapi.data.romete.dto.toPopularFilm
import com.altintasomer.tmdbapi.domain.model.PopularFilm
import com.altintasomer.tmdbapi.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

private const val TAG = "GetFilmsUseCase"
class GetFilmsUseCase @Inject constructor(private val repository: Repository){

     fun getFilms(page: String): Flow<ResourceState<PopularFilm>> = flow {
        try {
            emit(ResourceState.Loading<PopularFilm>())
            val response = repository.getFilms(page = page)
            emit(ResourceState.Success<PopularFilm>(response?.toPopularFilm()))

        }catch (e: HttpException){
            emit(ResourceState.Error<PopularFilm>(e.localizedMessage ?: "An unexpected error occurred!"))
        }catch (e: IOException){
            emit(ResourceState.Error<PopularFilm>("Couldn't reach server. Check your internet connection."))
        }
    }

    fun getSearchFilms(query: String,page: String): Flow<ResourceState<PopularFilm>> = flow {

        try {
            emit(ResourceState.Loading())
            val response = repository.searchFilms(query, page)
            emit(ResourceState.Success(response?.toPopularFilm()))

        }catch (e: HttpException){
            emit(ResourceState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }catch (e: IOException){
            emit(ResourceState.Error("Couldn't reach server. Check your internet connection."))
        }

    }
}