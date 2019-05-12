package com.arctouch.codechallenge.data

import com.arctouch.codechallenge.model.Genre

object Cache {

    var genres = listOf<Genre>()

    fun cacheGenres(genre: Genre) {
        this.genres += genre
    }
}
