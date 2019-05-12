package com.arctouch.codechallenge.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.convertToPtBrDateFormat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.details_fragment.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class DetailsFragment : Fragment() {
    private var TAG = Logger.tag
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
        loadData()

    }

    @SuppressLint("CheckResult")
    private fun loadData() {
        if (Logger.DEBUG) Log.d(TAG, "loadData")

        viewModel.requestMovie()
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { movie ->
                    mountInterface(movie)
                }
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
