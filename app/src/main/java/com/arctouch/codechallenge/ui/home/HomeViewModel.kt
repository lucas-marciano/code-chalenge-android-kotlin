package com.arctouch.codechallenge.ui.home

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel : ViewModel() {
    private var TAG = Logger.tag
    lateinit var apiInstance: TmdbApi
    var mMovies = MutableLiveData<List<Movie>>()
    var cacheMovies = MutableLiveData<List<Movie>>()
    var isLoading: Boolean = false
        private set
    var currentPage: Long = -1
        private set

    /**
     * Method to fetch the data, checking first the cache genres.
     * @param page [Long]
     */
    @SuppressLint("CheckResult")
    fun fetch(page: Long) {
        if (Logger.DEBUG) Log.d(TAG, "fetch")

        if (Cache.genres.isEmpty()) {
            apiInstance.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Cache.cacheGenres(it.genres)
                        requestMovies(page)
                    }
        } else {
            requestMovies(page)
        }
    }

    /**
     * Method to the request all movies in the page parameter.
     * @param page [Long]
     */
    @SuppressLint("CheckResult")
    private fun requestMovies(page: Long) {
        if (Logger.DEBUG) Log.d(TAG, "requestMovies")

        isLoading = true

        if (page <= currentPage) {
            mMovies = cacheMovies
        } else {
            currentPage = page
            apiInstance.upcomingMovies(TmdbApi.API_KEY, page)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { movies ->
                        mMovies.value = movies.results.map { movie ->
                            movie.copy(genres = Cache.genres.filter {
                                movie.genreIds?.contains(it.id) == true
                            })
                        }

                        cacheMovies = mMovies
                        isLoading = false
                    }
        }
    }

    /**
     * Clear the cache and reset the current page.
     */
    fun reset() {
        currentPage = -1
        cacheMovies = MutableLiveData()
        isLoading = false
    }
}