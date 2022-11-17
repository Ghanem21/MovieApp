package com.example.movieapp.repo


import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.movieapp.MovieApi
import com.example.movieapp.database.MoviesDatabase
import com.example.movieapp.database.asDomainModel
import com.example.movieapp.domain.Movie
import com.example.movieapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepo(private val database: MoviesDatabase) {

    /**
     * A playlist of videos that can be shown on the screen.
     */
    val movies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getMovies()) {
            it.asDomainModel()
        }

    val favouriteMovies: LiveData<List<Movie>> =
        Transformations.map(database.movieDao.getFavouriteMovies()) {
            it.asDomainModel()
        }

    /**
     * Refresh the videos stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the videos for use, observe [movies]
     */
    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val topRatedRespond = MovieApi.retrofitService.getTopRatedMovie()
            database.movieDao.insertAll(*topRatedRespond.asDatabaseModel())
        }
    }

    suspend fun setFavourite(id:Int,favourite:Boolean) {
        withContext(Dispatchers.IO) {
            database.movieDao.setFavourite(id, favourite )
        }
    }
}