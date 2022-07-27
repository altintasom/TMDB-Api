package com.altintasomer.tmdbapi.data.romete.dto

import com.altintasomer.tmdbapi.domain.model.PopularFilm

private const val TAG = "PopularFilmDto"
data class PopularFilmDto(
    val page: Int,
    val results: List<FilmDto>?,
    val total_pages: Int,
    val total_results: Int
)

fun PopularFilmDto.toPopularFilm(): PopularFilm{
    return PopularFilm(page = page,films = results?.map { it.toFilm() },total_pages = total_pages)
}