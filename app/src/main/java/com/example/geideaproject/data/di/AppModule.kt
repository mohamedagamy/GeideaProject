package com.example.geideaproject.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.geideaproject.data.api.ApiService
import com.example.geideaproject.data.room.AppDatabase
import com.jakewharton.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import com.example.tempo.data.repo.AppRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePicasso(@ApplicationContext appContext: Context, okHttp3Downloader: OkHttp3Downloader): Picasso {
        return Picasso.Builder(appContext).downloader(okHttp3Downloader).build()
    }

    @Provides
    @Singleton
    fun provideGlide(@ApplicationContext appContext: Context): RequestManager {
        return Glide.with(appContext)
    }

    @Provides
    @Singleton
    fun okHttp3Downloader(okHttpClient: OkHttpClient): OkHttp3Downloader {
        return OkHttp3Downloader(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideUsersApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAppRepository(apiService: ApiService): AppRepository {
        return AppRepository(apiService)
    }

/*    @Provides
    @Singleton
    fun provideRoomDB(appContext: Context): AppDatabase {
        val db = Room.databaseBuilder(
            appContext,
            AppDatabase::class.java, "db-geidea-users"
        ).build()
        return db
    }*/

}