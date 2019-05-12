package com.arctouch.codechallenge.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.util.Logger
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel

class HomeActivity : AppCompatActivity() {
    private var TAG = Logger.tag
    private val viewModel by viewModel<HomeViewModel>()

    private val adapter: HomeAdapter by lazy {
        HomeAdapter()
    }

    private var recyclerState: Parcelable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Logger.DEBUG) Log.d(TAG, "onCreate")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)
        setupInterface()
        loadData()
    }

    override fun onDestroy() {
        if (Logger.DEBUG) Log.d(TAG, "onDestroy")

        super.onDestroy()
        viewModel.reset()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        if (Logger.DEBUG) Log.d(TAG, "onSaveInstanceState")

        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putParcelable("llmState", recyclerView.layoutManager?.onSaveInstanceState())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (Logger.DEBUG) Log.d(TAG, "onRestoreInstanceState")

        super.onRestoreInstanceState(savedInstanceState)
        recyclerState = savedInstanceState?.getParcelable("llmState")
    }

    private fun setupInterface() {
        if (Logger.DEBUG) Log.d(TAG, "setupInterface")

        val llm = LinearLayoutManager(this)
        recyclerView.layoutManager = llm
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastVisibleItemPosition = llm.findLastVisibleItemPosition()
                if (lastVisibleItemPosition == adapter.itemCount - 1 && !viewModel.isLoading) {
                    loadData(viewModel.currentPage + 1)
                }
            }
        })
    }

    @SuppressLint("CheckResult")
    private fun loadData(page: Long = 1) {
        if (Logger.DEBUG) Log.d(TAG, "loadData")

        viewModel.requestMovies(page)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ movie ->
                    adapter.insertMovies(movie)
                    updateInterface()
                }, { exception ->
                    if (Logger.DEBUG) Log.e(TAG, exception.message)
                }, {
                    if (recyclerState != null) {
                        recyclerView.layoutManager?.onRestoreInstanceState(recyclerState)
                        recyclerState = null
                    }
                })
    }

    private fun updateInterface() {
        if (Logger.DEBUG) Log.d(TAG, "updateInterface")

        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }
}
