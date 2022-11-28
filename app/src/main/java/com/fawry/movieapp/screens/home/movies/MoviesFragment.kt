package com.fawry.movieapp.screens.home.movies

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.fawry.movieapp.R
import com.fawry.movieapp.adapter.MoviesAdapter
import com.fawry.movieapp.data.db.AppDatabase
import com.fawry.movieapp.data.model.Movie
import com.fawry.movieapp.data.model.Status
import com.fawry.movieapp.data.worker.MoviesWorker
import com.fawry.movieapp.databinding.FragmentMoviesBinding
import com.fawry.movieapp.utils.AppUtils.Companion.createProgressDialog
import com.fawry.movieapp.utils.HorizontalListItemMargin
import com.fawry.movieapp.utils.NetworkHelper
import com.fawry.movieapp.utils.PrefManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@AndroidEntryPoint
class MoviesFragment : Fragment(),
    MoviesAdapter.OnMovieClickListener {

    private val TAG = "MoviesFrag"

    private lateinit var viewBinding: FragmentMoviesBinding

    private lateinit var progressDialog: AlertDialog

    @Inject
    lateinit var prefManager: PrefManager

    @Inject
    lateinit var networkHelper: NetworkHelper

    @Inject
    lateinit var appDb: AppDatabase

    private val moviesViewModel: MoviesViewModel by activityViewModels()

    private lateinit var popularMovies: MutableList<Movie>
    private lateinit var popularMoviesAdapter: MoviesAdapter

    private lateinit var upcomingMovies: MutableList<Movie>
    private lateinit var upcomingMoviesAdapter: MoviesAdapter

    private lateinit var topRatedMovies: MutableList<Movie>
    private lateinit var topRatedMoviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentMoviesBinding.inflate(inflater, container, false)
        initViews()

        return viewBinding.root
    }


    // using work manger way one .
    private fun initWorker() {
        val workRequest = PeriodicWorkRequest.Builder(
            MoviesWorker::class.java,
            4,
            TimeUnit.HOURS,
            PeriodicWorkRequest.MIN_PERIODIC_FLEX_MILLIS,
            TimeUnit.MILLISECONDS,
        )
            .addTag("refresh_dp_periodic")
            .build()

        WorkManager.getInstance(requireActivity()).enqueueUniquePeriodicWork(
            "refresh_dp_periodic",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest,
        )
    }

    // for Quick test do work immediate
    private fun onTimeWork() {
        val request = OneTimeWorkRequestBuilder<MoviesWorker>().build()
        WorkManager.getInstance(requireContext().applicationContext).enqueue(request)
    }

    private fun initViews() {
        progressDialog = createProgressDialog()

        popularMovies = mutableListOf()
        upcomingMovies = mutableListOf()
        topRatedMovies = mutableListOf()

        // adapter init

        popularMoviesAdapter = MoviesAdapter(
            requireActivity(),
            mutableListOf(),
            this
        )
        upcomingMoviesAdapter = MoviesAdapter(
            requireActivity(),
            mutableListOf(),
            this
        )
        topRatedMoviesAdapter = MoviesAdapter(
            requireActivity(),
            mutableListOf(),
            this
        )

        val movieItemMargin =
            resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._16sdp)

        //set decoration from every recycler

        viewBinding.rvPopularMovies.addItemDecoration(
            HorizontalListItemMargin(
                movieItemMargin
            )
        )
        viewBinding.rvPopularMovies.layoutManager =
            GridLayoutManager(
                requireActivity(),
                1,
                RecyclerView.HORIZONTAL,
                false
            )
        viewBinding.rvPopularMovies.adapter = popularMoviesAdapter


        viewBinding.rvUpcomingMovies.addItemDecoration(
            HorizontalListItemMargin(
                movieItemMargin
            )
        )
        viewBinding.rvUpcomingMovies.layoutManager =
            GridLayoutManager(
                requireActivity(),
                1,
                RecyclerView.HORIZONTAL,
                false
            )
        viewBinding.rvUpcomingMovies.adapter = upcomingMoviesAdapter


        viewBinding.rvTopRatedMovies.addItemDecoration(
            HorizontalListItemMargin(
                movieItemMargin
            )
        )
        viewBinding.rvTopRatedMovies.layoutManager =
            GridLayoutManager(
                requireActivity(),
                1,
                RecyclerView.HORIZONTAL,
                false
            )
        viewBinding.rvTopRatedMovies.adapter = topRatedMoviesAdapter

        // if i need using work manger to handle the cached list deleted  (way num one)
        //        initWorker()

        // if i need normal time different to handle the cached list deleted  (way num two)

        runBlocking {
            calcTwoTime()
        }

        moviesViewModel.getMovies()

        getPopularMovies()
        getUpcomingMovies()
        getTopRatedMovies()


    }


    private fun getPopularMovies() {
        moviesViewModel.popularMovies.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    Log.d(TAG, "getPopularMovies: LOADING...")
                    Log.d(TAG, "getPopularMovies: ${it.message}")
                }
                Status.COMPLETE -> {
                    progressDialog.dismiss()
                    Log.d(TAG, "getPopularMovies: COMPLETE")
                    Log.d(TAG, "getPopularMovies: ${it.message}")

                    it.data?.let { response ->
                        if (response.isNotEmpty()) {
                            popularMovies.addAll(response)
                            popularMoviesAdapter.addItems(response)
                            viewBinding.tvPopularMovies.visibility = View.VISIBLE
                            viewBinding.rvPopularMovies.visibility = View.VISIBLE
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_movies_msg),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    } ?: run {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_msg),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e(TAG, "getPopularMovies: ERROR")
                    Log.e(TAG, "getPopularMovies: ${it.message}")

                    val msg = if (!networkHelper.isNetworkAvailable()) {
                        getString(R.string.check_internet_msg)
                    } else {
                        getString(R.string.error_msg)
                    }
                    Toast.makeText(
                        requireActivity(),
                        msg,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    private fun getUpcomingMovies() {
        moviesViewModel.upcomingMovies.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    Log.d(TAG, "getUpcomingMovies: LOADING...")
                    Log.d(TAG, "getUpcomingMovies: ${it.message}")
                }
                Status.COMPLETE -> {

                    progressDialog.dismiss()
                    Log.d(TAG, "getUpcomingMovies: COMPLETE")
                    Log.d(TAG, "getUpcomingMovies: ${it.message}")

                    it.data?.let { response ->
                        if (response.isNotEmpty()) {
                            upcomingMovies.addAll(response)
                            upcomingMoviesAdapter.addItems(response)
                            viewBinding.tvUpcomingMovies.visibility = View.VISIBLE
                            viewBinding.rvUpcomingMovies.visibility = View.VISIBLE

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_movies_msg),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    } ?: run {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_msg),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e(TAG, "getUpcomingMovies: ERROR")
                    Log.e(TAG, "getUpcomingMovies: ${it.message}")

                    val msg = if (!networkHelper.isNetworkAvailable()) {
                        getString(R.string.check_internet_msg)
                    } else {
                        getString(R.string.error_msg)
                    }
                    Toast.makeText(
                        requireActivity(),
                        msg,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    private fun getTopRatedMovies() {
        moviesViewModel.topRatedMovies.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    progressDialog.show()
                    Log.d(TAG, "getTopRatedMovies: LOADING...")
                    Log.d(TAG, "getTopRatedMovies: ${it.message}")
                }
                Status.COMPLETE -> {
                    progressDialog.dismiss()
                    Log.d(TAG, "getTopRatedMovies: COMPLETE")
                    Log.d(TAG, "getTopRatedMovies: ${it.message}")

                    it.data?.let { response ->
                        if (response.isNotEmpty()) {
                            topRatedMovies.addAll(response)
                            topRatedMoviesAdapter.addItems(response)
                            viewBinding.tvTopRatedMovies.visibility = View.VISIBLE
                            viewBinding.rvTopRatedMovies.visibility = View.VISIBLE

                        } else {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.no_movies_msg),
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    } ?: run {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_msg),
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                Status.ERROR -> {
                    progressDialog.dismiss()
                    Log.e(TAG, "getTopRatedMovies: ERROR")
                    Log.e(TAG, "getTopRatedMovies: ${it.message}")

                    val msg = if (!networkHelper.isNetworkAvailable()) {
                        getString(R.string.check_internet_msg)
                    } else {
                        getString(R.string.error_msg)
                    }
                    Toast.makeText(
                        requireActivity(),
                        msg,
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
        }
    }

    override fun onMovieClick(movie: Movie) {
        findNavController().navigate(MoviesFragmentDirections.actionOpenMovieDetails(movie))
    }

    private suspend fun calcTwoTime() {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateAndTime: String = sdf.format(Date())
        if (prefManager.getTime() == null || prefManager.getTime().equals("")) {
            prefManager.setTime(currentDateAndTime)
            Log.d("timeOn ", "calcTwoTime: ")
        } else {
            val currentDate: Date = sdf.parse(currentDateAndTime) as Date
            val oldDate: Date = sdf.parse(prefManager.getTime().toString()) as Date
            val diff = currentDate.time - oldDate.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            val hours = minutes / 60

            if (hours >= 4) {
                appDb.movieDao().deleteMovies()
                prefManager.setTime("")
            }
        }
    }
}