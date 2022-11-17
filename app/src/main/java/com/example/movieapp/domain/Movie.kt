package com.example.movieapp.domain



data class Movie(
    val id:Int,
    val title:String,
    val image:String,
    val voteAverage:String,
    var favourite:Boolean
)