package com.altintasomer.tmdbapi.domain.model

data class Film(
    val id: Int,
    val name: String?,
    val poster_path: String?,
    val vote_average: Double?,
    val vote_count: Int?
)
