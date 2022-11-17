package com.example.movieapp.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("select * from databasemovie ORDER BY voteAverage DESC")
    fun getMovies(): LiveData<List<DatabaseMovie>>

    @Query("select * from databasemovie where favourite = 1")
    fun getFavouriteMovies(): LiveData<List<DatabaseMovie>>

    @Query("update databasemovie set favourite = :favourite where id = :id")
    fun setFavourite(id :Int,favourite:Boolean)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg movies: DatabaseMovie)
}

@Database(entities = [DatabaseMovie::class], version = 1)
abstract class MoviesDatabase : RoomDatabase() {
    abstract val movieDao: MovieDao
}

private lateinit var INSTANCE: MoviesDatabase

fun getDatabase(context: Context): MoviesDatabase {
    synchronized(MoviesDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                MoviesDatabase::class.java,
                "movies").build()
        }
    }
    return INSTANCE
}