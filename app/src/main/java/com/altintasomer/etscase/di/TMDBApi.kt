package com.altintasomer.etscase.di

import com.altintasomer.etscase.utils.Utils.Companion.API_KEY
import com.altintasomer.etscase.model.network.FilmDetail
import com.altintasomer.etscase.model.network.PopularFilm
import com.altintasomer.etscase.utils.Utils
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBApi {

    @GET("search/tv")
    suspend fun searchFilms(@Query("api_key") apiKey : String? = API_KEY, @Query("query") query : String?, @Query("language") language : String? = Utils.LANGUAGE, @Query("page") page : String? = "1", @Query("include_adult") include_adult : Boolean? = false) : Response<PopularFilm>

    @GET("tv/{tv_id}")
    suspend fun getFilmDetail(@Path("tv_id") tv_id : String,@Query("api_key") apiKey : String? = API_KEY) : Response<FilmDetail>

    @GET("tv/popular")
    suspend fun getFilms(@Query("api_key") apiKey : String? = API_KEY, @Query("language") language : String? = Utils.LANGUAGE, @Query("page") page : String? = "1") : Response<PopularFilm>
}