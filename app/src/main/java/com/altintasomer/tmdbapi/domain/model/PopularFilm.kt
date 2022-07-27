package com.altintasomer.tmdbapi.domain.model

data class PopularFilm(
    val page: Int,
    val films: List<Film>?,
    val total_pages: Int
)
