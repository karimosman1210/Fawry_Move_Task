package com.fawry.movieapp.di

import android.content.Context
import androidx.room.Room
import com.fawry.movieapp.BuildConfig
import com.fawry.movieapp.data.api.AuthInterceptor
import com.fawry.movieapp.data.api.MoviesApi
import com.fawry.movieapp.data.db.AppDatabase
import com.fawry.movieapp.data.repo.MoviesRepo
import com.fawry.movieapp.utils.AppUtils
import com.fawry.movieapp.utils.NetworkHelper
import com.fawry.movieapp.utils.PrefManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideBaseUrl() = AppUtils.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()

        okHttpBuilder.connectTimeout(1, TimeUnit.MINUTES)
        okHttpBuilder.readTimeout(1, TimeUnit.MINUTES)
        okHttpBuilder.writeTimeout(1, TimeUnit.MINUTES)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }

        okHttpBuilder.addInterceptor(authInterceptor)

        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoviesApi(retrofit: Retrofit): MoviesApi {
        return retrofit.create(MoviesApi::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, AppUtils.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideMoviesRepo(appDatabase: AppDatabase, moviesApi: MoviesApi): MoviesRepo {
        return MoviesRepo(appDatabase, moviesApi)
    }


    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext appContext: Context): NetworkHelper {
        return NetworkHelper(appContext)
    }

    @Provides
    @Singleton
    fun providePrefManager(@ApplicationContext appContext: Context): PrefManager {
        return PrefManager(appContext)
    }
}