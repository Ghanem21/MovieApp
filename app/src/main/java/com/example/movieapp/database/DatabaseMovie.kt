package com.example.movieapp.database


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.domain.Movie
import com.squareup.moshi.Json

@Entity
data class DatabaseMovie(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title:String,
    @Json(name = "poster_path")
    val image:String,
    @Json(name = "vote_average")
    val voteAverage:String,
    var favourite : Boolean
)

fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map {
       Movie(
            id = it.id,
            title = it.title,
            image = it.image,
            voteAverage = it.voteAverage,
            favourite = it.favourite
        )
    }
}