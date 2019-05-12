package com.arctouch.codechallenge.ui.home

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import io.reactivex.Observable

class HomeViewModel : ViewModel() {
    private var TAG = Logger.tag

    var cacheMovies = mutableListOf<Movie>()

    var isLoading: Boolean = false
        private set
    var currentPage: Long = -1
        private set

    /**
     * Method to the request all movies in the page parameter.
     * @param page [Long]
     */
    @SuppressLint("CheckResult")
    fun requestMovies(page: Long): Observable<Movie>? {
        if (Logger.DEBUG) Log.d(TAG, "requestMovies")

        isLoading = true

        if (Cache.genres.isEmpty()) {
            TmdbApi.genresMovies().doOnNext { genres ->
                Cache.cacheGenres(genres)
            }
        }

        return if (page <= currentPage) {
            Observable.fromIterable(cacheMovies)
        } else {
            currentPage = page

            TmdbApi.getAllMovies(page).doOnNext { movie ->
                movie.copy(genres = Cache.genres.filter {
                    movie.genreIds?.contains(it.id) == true
                })
                cacheMovies.add(movie)
            }.doOnComplete {
                isLoading = false
            }
        }
    }

    /**
     * Clear the cache and reset the current page.
     */
    fun reset() {
        currentPage = -1
        cacheMovies.clear()
        isLoading = false
    }
}