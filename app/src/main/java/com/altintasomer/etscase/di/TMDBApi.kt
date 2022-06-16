package com.altintasomer.etscase.di

import com.altintasomer.etscase.di.Utils.Companion.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBApi {

    @GET("/tv/popular")
    suspend fun getFilms(@Query("apiKey") apiKey : String? = API_KEY)
}