package com.arctouch.codechallenge.ui.home

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : BaseActivity() {
    private val viewModel by viewModel<HomeViewModel>()

    private val adapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    private var recyclerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        viewModel.apiInstance = api
        setupInterface()

        viewModel.fetch(1)

        if (recyclerState != null) {
            recyclerView.layoutManager?.onRestoreInstanceState(recyclerState)
        } else {
            viewModel.mMovies.observe(this, Observer { movies ->
                movies?.let {
                    updateInterface(movies)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.reset()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putParcelable("llmState", recyclerView.layoutManager?.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        recyclerState = savedInstanceState?.getParcelable("llmState")
    }

    private fun setupInterface(){
        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition = llm.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == adapter.itemCount - 1 && !viewModel.isLoading) {
                    viewModel.fetch(viewModel.currentPage + 1)
                }
            }
        })
    }

    private fun updateInterface(movies: List<Movie>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE

        movies.forEach {
            adapter.insertMovies(it)
        }

    }
}
