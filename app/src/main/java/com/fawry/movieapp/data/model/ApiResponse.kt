package com.fawry.movieapp.data.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("results") val movies: List<Movie>,
)