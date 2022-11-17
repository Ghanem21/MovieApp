package com.example.movieapp.ui

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movieapp.MovieApi
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieListBinding
import com.example.movieapp.domain.Movie
import com.example.movieapp.viewmodel.MovieViewModel
import kotlinx.coroutines.*

class MovieListFragment : Fragment() {

    private lateinit var binding: FragmentMovieListBinding


    private val viewModel: MovieViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, MovieViewModel.Factory(activity.application)).get(MovieViewModel::class.java)

    }
    private var viewModelAdapter: MovieAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            movies?.apply {
                viewModelAdapter?.movies = movies
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModelAdapter = MovieAdapter()
        binding.recyclerview.adapter = viewModelAdapter

        return binding.root
    }
}