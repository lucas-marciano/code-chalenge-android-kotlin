package com.arctouch.codechallenge.util

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    val PREFS_FILENAME = "com.arctouch.codechallenge.util.prefs"
    val ID_MOVIE_PREF = "id_movie_selected"

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var idMovie: Long
        get() = prefs.getLong(ID_MOVIE_PREF, -1)
        set(value) = prefs.edit().putLong(ID_MOVIE_PREF, value).apply()
}