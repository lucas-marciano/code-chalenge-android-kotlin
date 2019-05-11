package com.arctouch.codechallenge.ui.detail

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class DetailsViewModel : ViewModel() {

    private var TAG = Logger.tag
    lateinit var apiInstance: TmdbApi

    var movieId: Long = 0
    var mMovie = MutableLiveData<Movie>()

    @SuppressLint("CheckResult")
    fun requestMovie(){
        if (Logger.DEBUG) Log.d(TAG, "requestMovie")

        apiInstance.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mMovie.value = it
                }
    }
}
