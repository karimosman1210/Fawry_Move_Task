package com.fawry.movieapp.screens.home.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.data.model.Result
import com.fawry.movieapp.data.repo.MoviesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val repo: MoviesRepo
) : ViewModel() {

    private val _popularMovies =
        MutableLiveData<Result<List<Movie>>>(Result.loading("Loading..."))
    val popularMovies: LiveData<Result<List<Movie>>> = _popularMovies

    private val _upcomingMovies =
        MutableLiveData<Result<List<Movie>>>(Result.loading("Loading..."))
    val upcomingMovies: LiveData<Result<List<Movie>>> = _upcomingMovies

    private val _topRatedMovies =
        MutableLiveData<Result<List<Movie>>>(Result.loading("Loading..."))
    val topRatedMovies: LiveData<Result<List<Movie>>> = _topRatedMovies

    fun getMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            launch {
                _popularMovies.postValue(
                    Result.loading("Loading...")
                )

                _popularMovies.postValue(
                    repo.getPopularMovies()
                )
            }

            launch {
                _upcomingMovies.postValue(
                    Result.loading("Loading...")
                )

                _upcomingMovies.postValue(
                    repo.getUpcomingMovies()
                )
            }

            launch {
                _topRatedMovies.postValue(
                    Result.loading("Loading...")
                )

                _topRatedMovies.postValue(
                    repo.getTopRatedMovies()
                )
            }
        }
    }
}