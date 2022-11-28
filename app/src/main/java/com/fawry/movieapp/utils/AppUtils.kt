package com.fawry.movieapp.utils

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.fawry.movieapp.R

class AppUtils {

    companion object {

        const val BASE_URL = "https://api.themoviedb.org/3/"
        const val BASE_POSTER_URL = "https://image.tmdb.org/t/p/w500"
        const val BASE_BACKDROP_URL = "https://image.tmdb.org/t/p/w780"

        const val API_KEY = "c50f5aa4e7c95a2a553d29b81aad6dd0"

        const val DATABASE_NAME = "movie_database"

        fun Fragment.createProgressDialog(): AlertDialog {
            val builder = AlertDialog.Builder(requireActivity(), R.style.WrapContentDialogTheme)
            val dialogView = layoutInflater.inflate(R.layout.dialog_loading, null)
            builder.setView(dialogView)
            builder.setCancelable(false)
            return builder.create()
        }
    }
}