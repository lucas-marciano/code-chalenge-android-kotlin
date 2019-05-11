package com.arctouch.codechallenge.ui.detail

import android.os.Bundle
import android.widget.Toast
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.util.Constants.ID_MOVIE_SELECTED
import org.koin.android.viewmodel.ext.android.viewModel

class DetailsActivity : BaseActivity() {
    private val viewModel by viewModel<DetailsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.details_activity)

        viewModel.apiInstance = api

        val extra = intent.getLongExtra(ID_MOVIE_SELECTED, -1)

        if (extra > 0) {
            viewModel.movieId = extra
        } else {
            Toast.makeText(this, getString(R.string.message_error_negative_movie_id),
                    Toast.LENGTH_LONG).show()
            finish()
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, DetailsFragment.newInstance())
                    .commitNow()
        }

    }
}
