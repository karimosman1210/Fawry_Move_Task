package com.fawry.movieapp.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "movie")
@Parcelize
data class Movie(
    @PrimaryKey @SerializedName("id") val id: Int,
    @ColumnInfo(name = "poster_path") @SerializedName("poster_path") val posterPath: String?,
    @ColumnInfo(name = "overview") @SerializedName("overview") val overview: String?,
    @ColumnInfo(name = "release_date") @SerializedName("release_date") val releaseDate: String?,
    @ColumnInfo(name = "original_title") @SerializedName("original_title") val originalTitle: String?,
    @ColumnInfo(name = "original_language") @SerializedName("original_language") val originalLanguage: String?,
    @ColumnInfo(name = "title") @SerializedName("title") val title: String?,
    @ColumnInfo(name = "backdrop_path") @SerializedName("backdrop_path") val backdropPath: String?,
    @ColumnInfo(name = "popularity") @SerializedName("popularity") val popularity: Double?,
    @ColumnInfo(name = "vote_count") @SerializedName("vote_count") val voteCount: Int?,
    @ColumnInfo(name = "video") @SerializedName("video") val video: Boolean?,
    @ColumnInfo(name = "vote_average") @SerializedName("vote_average") val voteAverage: Double?,
    var movieType: MovieType = MovieType.NONE,
) : Parcelable