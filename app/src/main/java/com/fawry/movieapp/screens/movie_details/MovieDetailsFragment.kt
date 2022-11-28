package com.fawry.movieapp.screens.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.fawry.movieapp.R
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.databinding.FragmentMovieDetailsBinding
import com.fawry.movieapp.utils.AppUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {

    private lateinit var viewBinding: FragmentMovieDetailsBinding

    val safeArgs: MovieDetailsFragmentArgs by navArgs()

    private lateinit var movie: Movie

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMovieDetailsBinding.inflate(inflater, container, false)

        movie = safeArgs.movie

        initViews()

        return viewBinding.root
    }

    private fun initViews() {
        viewBinding.icBack.setOnClickListener {
            findNavController().navigateUp()
        }

        Glide.with(this)
            .load(AppUtils.BASE_BACKDROP_URL + movie.backdropPath)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(viewBinding.itemImage)

        viewBinding.itemTitle.text = movie.title

        viewBinding.itemOverview.text = movie.overview
    }
}