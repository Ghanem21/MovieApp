package com.example.movieapp.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentSplashScreenBinding
import com.example.movieapp.viewmodel.MovieViewModel
import com.google.firebase.auth.FirebaseAuth

class SplashScreenFragment : Fragment() {
    private lateinit var binding:FragmentSplashScreenBinding
    private lateinit var auth:FirebaseAuth

    private val viewModel: MovieViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        ViewModelProvider(
            requireActivity(),
            MovieViewModel.Factory(activity.application)
        )[MovieViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_splash_screen, container, false)
        auth = FirebaseAuth.getInstance()
        auth.signOut()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fader()
    }
    private fun fader(){
        val animator = ObjectAnimator.ofFloat(binding.appNameText,View.ALPHA,0f)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.duration = 2000
        animator.disableViewDuringAnimation()
        animator.start()
    }

    private fun ObjectAnimator.disableViewDuringAnimation() {
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                if(auth.currentUser != null && auth.currentUser!!.isEmailVerified)
                    findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment2)
                else {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                }
            }
        })
    }
}