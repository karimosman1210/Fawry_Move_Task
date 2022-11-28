package com.fawry.movieapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Inject

/* SharedPreference to save localization select */
class PrefManager @Inject constructor(context: Context) {

    companion object {
        private const val TAG = "PrfMangr"
        private const val prefName = "geidea"
        private const val timeSaved = "LanguageCode"
        const val ar = "ar"
        const val en = "en"
    }

    private val pref = context.getSharedPreferences(prefName, MODE_PRIVATE)

    fun getTime(): String? {
        return pref.getString(timeSaved, "")
    }

    fun setTime(time: String) {
        pref.edit().putString(timeSaved, time).apply()
    }
}