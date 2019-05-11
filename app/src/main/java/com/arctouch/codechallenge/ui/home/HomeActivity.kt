package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        viewModel.apiInstance = api

    }

    override fun onResume() {
        super.onResume()
        viewModel.requestMovies()

        viewModel.mMovies.observe(this, Observer { movies ->
            movies?.let {
                updateInterface(movies)
            }
        })
    }

    private fun updateInterface(movies: List<Movie>) {
        recyclerView.adapter = HomeAdapter(movies)
        progressBar.visibility = View.GONE
    }
}
