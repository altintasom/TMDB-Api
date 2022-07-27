package com.altintasomer.tmdbapi.data.romete

import com.altintasomer.tmdbapi.common.Utils.Companion.API_KEY
import com.altintasomer.tmdbapi.data.romete.dto.FilmDetailDto
import com.altintasomer.tmdbapi.data.romete.dto.PopularFilmDto
import com.altintasomer.tmdbapi.common.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("search/tv")
    suspend fun searchFilms(@Query("api_key") apiKey : String? = API_KEY,
                            @Query("query") query : String?, @Query("language") language : String? = Utils.LANGUAGE,
                            @Query("page") page : String? = "1",
                            @Query("include_adult") include_adult : Boolean? = false) : PopularFilmDto?

    @GET("tv/{tv_id}")
    suspend fun getFilmDetail(@Path("tv_id") tv_id : String,
                              @Query("api_key") apiKey : String? = API_KEY) : FilmDetailDto?

    @GET("tv/popular")
    suspend fun getFilms(@Query("api_key") apiKey : String? = API_KEY,
                         @Query("language") language : String? = Utils.LANGUAGE,
                         @Query("page") page : String? = "1") :PopularFilmDto?
}