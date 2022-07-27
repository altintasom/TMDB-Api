package com.altintasomer.tmdbapi.data.romete.dto

import com.altintasomer.tmdbapi.domain.model.Film

data class FilmDto(
    val backdrop_path: String,
    val first_air_date: String,
    val genre_ids: List<Int>,
    val id: Int,
    val name: String,
    val origin_country: List<String>,
    val original_language: String,
    val original_name: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val vote_average: Double,
    val vote_count: Int
)

fun FilmDto.toFilm(): Film{
    return Film(id, name, poster_path, vote_average, vote_count)
}