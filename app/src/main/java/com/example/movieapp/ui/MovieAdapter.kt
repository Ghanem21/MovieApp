package com.example.movieapp.ui

import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movieapp.R
import com.example.movieapp.database.getDatabase
import com.example.movieapp.databinding.MovieCardBinding
import com.example.movieapp.domain.Movie
import com.example.movieapp.repo.MovieRepo
import com.example.movieapp.setFavouriteState
import kotlinx.coroutines.*


class MovieAdapter() :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    var movies: List<Movie> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    class MovieViewHolder(private var binding: MovieCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val database = getDatabase(binding.root.context)
        private val movieRepo = MovieRepo(database)
        private val coroutineScope = CoroutineScope(Dispatchers.IO)
        fun onBind(movie: Movie) {
            binding.movie = movie

            binding.favoriteIcon.setOnClickListener {
                coroutineScope.launch {
                   movieRepo.setFavourite(movie.id, !movie.favourite)
                    withContext(Dispatchers.Main){
                        setFavouriteState(binding.favoriteIcon,!movie.favourite)
                    }
                }

            }
            Log.d("TAG", "onBind: ${movie.title} ${movie.favourite}" )
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        return MovieAdapter.MovieViewHolder(
            MovieCardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.onBind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
