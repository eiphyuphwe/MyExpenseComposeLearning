package com.example.myapplication.di

import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.data.repository.IncomeDashboardRepositoryImpl
import com.example.myapplication.data.repository.expense.ExpenseRepository
import com.example.myapplication.data.repository.expense.ExpenseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindIncomeDashboardRepository(
        implementation: IncomeDashboardRepositoryImpl
    ): IncomeDashboardRepository

    @Binds
    @Singleton
    abstract fun bindExpenseRepository(
        implementation: ExpenseRepositoryImpl
    ): ExpenseRepository
}