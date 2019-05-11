package com.arctouch.codechallenge.ui.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseFragment
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.convertToPtBrDateFormat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.details_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DetailsFragment : BaseFragment() {

    private val viewModel by sharedViewModel<DetailsViewModel>()

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private val movieImageUrlBuilder = MovieImageUrlBuilder()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.requestMovie()
    }

    override fun onResume() {
        super.onResume()
        viewModel.mMovie.observe(this, Observer { movie ->
            movie?.let {
                mountInterface(it)
            }
        })
    }

    /**
     * Build the interface
     */
    private fun mountInterface(movie: Movie) {

        Glide.with(ivPoster)
                .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(ivPoster)

        Glide.with(ivBackdrop)
                .load(movie.backdropPath?.let { movieImageUrlBuilder.buildBackdropUrl(it) })
                .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                .into(ivBackdrop)

        tvName.text = movie.title
        tvGenres.text = movie.genres?.joinToString(separator = ", ") { it.name }
        tvReleaseDate.text = movie.releaseDate?.convertToPtBrDateFormat()

        if (movie.overview != null || movie.overview.toString().isNotEmpty()) {
            tvOverview.text = movie.overview
        }
    }

}
