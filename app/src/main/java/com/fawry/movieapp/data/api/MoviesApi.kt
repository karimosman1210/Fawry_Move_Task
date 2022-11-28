package com.fawry.movieapp.data.api

import com.fawry.movieapp.data.model.ApiResponse
import retrofit2.Response
import retrofit2.http.GET

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(): Response<ApiResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(): Response<ApiResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(): Response<ApiResponse>
}