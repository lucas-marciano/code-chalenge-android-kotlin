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

    @SuppressLint("CheckResult")
    fun requestMovies() {
        if (Logger.DEBUG) Log.d(TAG, "requestMovies")

        apiInstance.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { movies ->
                    mMovies.value = movies.results.map { movie ->
                        movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                    }
                }
    }
}