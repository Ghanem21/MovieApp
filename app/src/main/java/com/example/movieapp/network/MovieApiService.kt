package com.example.movieapp

import com.example.movieapp.network.NetworkMovieContainer
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

//base url of website
private const val BASE_URL =
    "https://api.themoviedb.org/3/movie/"

//moshi build which we will use to convert json to object kotlin
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

//retrofit build which use to get json response from the base url
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface MovieApiService {
    @GET("top_rated?api_key=cd6455be1815794f771dc7127ba72451")
    suspend fun getTopRatedMovie(): NetworkMovieContainer
}

//object to make singleton from the retrofit
object MovieApi {
    val retrofitService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }
}
