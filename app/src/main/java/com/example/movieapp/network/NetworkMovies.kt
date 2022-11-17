package com.example.movieapp.network

import com.example.movieapp.database.DatabaseMovie
import com.example.movieapp.domain.Movie
import com.squareup.moshi.Json


data class NetworkMovieContainer(
    @Json(name = "results")
    val movies: List<NetworkMovie>)



data class NetworkMovie(
    val id: Int,
    val title:String,
    @Json(name = "poster_path")
    val image:String,
    @Json(name = "vote_average")
    val voteAverage:String,
    var favourite:Boolean = false
)

fun NetworkMovieContainer.asDomainModel(): List<Movie> {
    return movies.map {
        Movie(
            id = it.id,
            title = it.title,
            image = it.image,
            voteAverage = it.voteAverage,
            favourite = it.favourite
            )
    }
}

fun NetworkMovieContainer.asDatabaseModel(): Array<DatabaseMovie> {
    return movies.map {
        DatabaseMovie(
            id = it.id,
            title = it.title,
            image = it.image,
            voteAverage = it.voteAverage,
            favourite = it.favourite,
        )
    }.toTypedArray()
}