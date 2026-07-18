package com.example.myapplication.di

import com.example.myapplication.core.NetworkMonitor
import com.example.myapplication.core.NetworkMonitorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkMonitorModule {

    @Binds
    @Singleton
    abstract fun bindNetworkMonitor(
        implementation: NetworkMonitorImpl
    ): NetworkMonitor
}