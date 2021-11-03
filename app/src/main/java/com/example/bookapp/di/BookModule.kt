package com.example.bookapp.di

import com.example.bookapp.network.BookService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

const val BASE_URL = "https://www.googleapis.com"

@Module
@InstallIn(SingletonComponent::class)
object BookModule {

    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit) : BookService = retrofit.create(BookService::class.java)

}