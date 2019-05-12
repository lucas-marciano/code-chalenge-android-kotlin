package com.arctouch.codechallenge.ui.detail

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import io.reactivex.Observable

class DetailsViewModel : ViewModel() {
    private var TAG = Logger.tag

    var movieId: Long = 0

    fun requestMovie(): Observable<Movie>? {
        if (Logger.DEBUG) Log.d(TAG, "requestMovies")

        return TmdbApi.movieById(movieId).doOnNext { movie ->
            movie.copy(genres = Cache.genres.filter {
                movie.genreIds?.contains(it.id) == true
            })
        }.doOnComplete {
            if (Logger.DEBUG) Log.d(TAG, "doOnComplete")
        }
    }
}
