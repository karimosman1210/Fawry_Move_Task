package com.fawry.movieapp.data.repo

import com.fawry.movieapp.data.api.MoviesApi
import com.fawry.movieapp.data.db.AppDatabase
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.data.model.MovieType
import com.fawry.movieapp.data.model.Result
import javax.inject.Inject

class MoviesRepo @Inject constructor(
    private val appDatabase: AppDatabase,
    private val api: MoviesApi,
) {

    suspend fun getPopularMovies(): Result<List<Movie>> {
        try {
            val movieList = appDatabase.movieDao().getMoviesByType(MovieType.POPULAR)

            if (movieList.isEmpty()) {
                val response = api.getPopularMovies()
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    val movies = responseBody.movies

                    movies.map {
                        it.movieType = MovieType.POPULAR
                    }

                    appDatabase.movieDao().insertMovies(movies)

                    return Result.complete(
                        data = movies,
                        message = "Complete.",
                    )
                } else {
                    return Result.error(
                        message = "Error: ${response.message()}",
                    )
                }
            } else {
                return Result.complete(
                    data = movieList,
                    message = "Complete.",
                )
            }
        } catch (exception: Exception) {
            return Result.error(
                message = "Error: ${exception.message}",
            )
        }
    }

    suspend fun getUpcomingMovies(): Result<List<Movie>> {
        try {
            val movieList = appDatabase.movieDao().getMoviesByType(MovieType.UPCOMING)

            if (movieList.isEmpty()) {
                val response = api.getUpcomingMovies()
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    val movies = responseBody.movies

                    movies.map {
                        it.movieType = MovieType.UPCOMING
                    }

                    appDatabase.movieDao().insertMovies(movies)

                    return Result.complete(
                        data = movies,
                        message = "Complete.",
                    )
                } else {
                    return Result.error(
                        message = "Error: ${response.message()}",
                    )
                }
            } else {
                return Result.complete(
                    data = movieList,
                    message = "Complete.",
                )
            }
        } catch (exception: Exception) {
            return Result.error(
                message = "Error: ${exception.message}",
            )
        }
    }

    suspend fun getTopRatedMovies(): Result<List<Movie>> {
        try {
            val movieList = appDatabase.movieDao().getMoviesByType(MovieType.TOP_RATED)

            if (movieList.isEmpty()) {
                val response = api.getTopRatedMovies()
                val responseBody = response.body()

                if (response.isSuccessful && responseBody != null) {
                    val movies = responseBody.movies

                    movies.map {
                        it.movieType = MovieType.TOP_RATED
                    }

                    appDatabase.movieDao().insertMovies(movies)

                    return Result.complete(
                        data = movies,
                        message = "Complete.",
                    )
                } else {
                    return Result.error(
                        message = "Error: ${response.message()}",
                    )
                }
            } else {
                return Result.complete(
                    data = movieList,
                    message = "Complete.",
                )
            }
        } catch (exception: Exception) {
            return Result.error(
                message = "Error: ${exception.message}",
            )
        }
    }
}