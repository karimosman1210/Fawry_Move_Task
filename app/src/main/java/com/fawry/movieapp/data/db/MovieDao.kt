package com.fawry.movieapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.data.model.MovieType

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie WHERE movieType=:movieType ")
    suspend fun getMoviesByType(movieType: MovieType): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movie")
    suspend fun deleteMovies()
}