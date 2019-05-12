package com.arctouch.codechallenge.api

import com.arctouch.codechallenge.BuildConfig
import com.arctouch.codechallenge.api.endpoints.Tmdb
import com.arctouch.codechallenge.model.Genre
import com.arctouch.codechallenge.model.Movie
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object TmdbApi {
    private val api: Tmdb by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        httpClient.addInterceptor{chain ->
            val origin = chain.request()
            val originalHttpUrl = origin.url()

            val url = originalHttpUrl.newBuilder()
                    // if the api were sent at post, it could be inserted
                    // as an interceptor at this point
                    .build()

            val requestBuilder = origin.newBuilder().url(url)
            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.URL)
                .client(OkHttpClient.Builder().build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()

        retrofit.create<Tmdb>(Tmdb::class.java)
    }

    /**
     * Get movies genres
     */
    fun genresMovies(): Observable<Genre>{
        return api.genres()
                .flatMapIterable { response ->
                    response.genres
                }
    }

    /**
     * Get all movies, paging the response.
     * @param page [Long]
     */
    fun getAllMovies(page: Long): Observable<Movie>{
        return api.upcomingMovies(BuildConfig.API_KEY, page)
                .flatMapIterable { response ->
                    response.results
                }
    }

    /**
     * Get movie by ID.
     * @param id [Long]
     */
    fun movieById(id: Long): Observable<Movie>{
        return api.movie(id)
    }
}
