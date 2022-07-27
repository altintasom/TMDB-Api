package com.altintasomer.tmdbapi.domain.use_cases.get_film

import com.altintasomer.tmdbapi.common.ResourceState
import com.altintasomer.tmdbapi.data.romete.dto.toFilmDetail
import com.altintasomer.tmdbapi.domain.model.FilmDetail
import com.altintasomer.tmdbapi.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFilmUseCase @Inject constructor(private val repository: Repository) {

    operator fun invoke(id: String): Flow<ResourceState<FilmDetail>> = flow {

        try {
            emit(ResourceState.Loading<FilmDetail>())

            val film = repository.getFilmDetail(tv_id = id)?.toFilmDetail()

            emit(ResourceState.Success<FilmDetail>(film))

        }catch (e: HttpException){
            emit(ResourceState.Error<FilmDetail>(e.localizedMessage ?: "AN unexpected error occurred!"))
        }catch (e: IOException){
            emit(ResourceState.Error<FilmDetail>("Couldn't reach server. Check your internet connection."))
        }

    }

}