package com.example.movieapp

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.movieapp.domain.Movie

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val url = "https://image.tmdb.org/t/p/original$imgUrl"

        Glide.with(imgView.rootView.context)
            .load(url)
            .placeholder(R.drawable.loading_animation)
            .error(R.drawable.ic_broken_image)
            .into(imgView)
    }
}

@BindingAdapter("setFavouriteState")
fun setFavouriteState(imgView: ImageView, favourite: Boolean) {
    if (favourite) {
        imgView.setImageResource(R.drawable.fill_heart)
    } else {
        imgView.setImageResource(R.drawable.heart)
    }
}