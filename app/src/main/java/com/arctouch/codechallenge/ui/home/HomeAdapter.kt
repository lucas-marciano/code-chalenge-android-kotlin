package com.arctouch.codechallenge.ui.home

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.model.Movie
import com.arctouch.codechallenge.prefs
import com.arctouch.codechallenge.ui.detail.DetailsActivity
import com.arctouch.codechallenge.util.Constants
import com.arctouch.codechallenge.util.Constants.ID_MOVIE_SELECTED
import com.arctouch.codechallenge.util.Logger
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.arctouch.codechallenge.util.convertToPtBrDateFormat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.movie_item.view.*

class HomeAdapter(private val movies: List<Movie>) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    class ViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
        private val TAG = Logger.tag

        private val movieImageUrlBuilder = MovieImageUrlBuilder()

        fun bind(movie: Movie) {
            if (Logger.DEBUG) Log.d(TAG, "bind")

            itemView.titleTextView.text = movie.title
            itemView.genresTextView.text = movie.genres?.joinToString(separator = ", ") { it.name }
            itemView.releaseDateTextView.text = movie.releaseDate?.convertToPtBrDateFormat()

            Glide.with(itemView)
                    .load(movie.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                    .apply(RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(itemView.posterImageView)

            setupClickListener(movie)

        }

        private fun setupClickListener(movie: Movie) {
            itemView.setOnClickListener {
                if (Logger.DEBUG) Log.d(TAG, "setupClickListener")

                val intent = Intent(context, DetailsActivity::class.java)
                intent.putExtra(ID_MOVIE_SELECTED, movie.id.toLong())
                context.startActivity(intent)

//                prefs.idMovie = movie.id.toLong()
            }
        }
    }
}
