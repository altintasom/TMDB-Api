package com.altintasomer.tmdbapi.domain.model

data class FilmDetail(
    val name: String,
    val first_air_date: String,
    val vote_average: Double,
    val vote_count: Int,
    val poster_path: String,
    val last_air_date: String,
    val overview: String,
    val original_language: String
)
