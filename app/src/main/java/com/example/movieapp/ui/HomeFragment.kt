package com.example.movieapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.second_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        binding.bottomNavigation.menu.getItem(0).isChecked = true

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            navController.navigate(item.itemId)
            return@setOnItemSelectedListener true
        }

        setupWithNavController(binding.bottomNavigation,navController)
        return binding.root
    }
}