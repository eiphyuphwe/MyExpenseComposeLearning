package com.example.myapplication.di

import com.example.myapplication.data.remote.IncomeTaxApiService
import com.example.myapplication.data.remote.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://6a4f1e52e785c9ef536d5e19.mockapi.io/api/v1/"


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideIncomeDashboardApi(): IncomeTaxApiService {
        return RetrofitClient.api
    }

}