package com.example.movieapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.movieapp.R
import com.example.movieapp.database.getDatabase
import com.example.movieapp.databinding.FragmentFavouriteBinding
import com.example.movieapp.repo.MovieRepo

class FavouriteFragment : Fragment() {
    lateinit var binding: FragmentFavouriteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_favourite, container, false)
        val adapter = MovieAdapter()
        val database = getDatabase(requireContext())
        val movieRepo = MovieRepo(database)

        movieRepo.favouriteMovies.observe(viewLifecycleOwner){
            movies ->
            adapter.movies = movies
        }
        binding.recyclerview.adapter = adapter
        return binding.root
    }
}