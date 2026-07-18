package com.example.myapplication.data.repository

import com.example.myapplication.data.mapper.toDomain
import com.example.myapplication.data.remote.IncomeTaxApiService
import com.example.myapplication.model.IncomeTransaction
import javax.inject.Inject

class IncomeDashboardRepositoryImpl @Inject constructor(private val apiService: IncomeTaxApiService) :
    IncomeDashboardRepository {
    override suspend fun getIncomeTransactions(): List<IncomeTransaction> {
        val response = apiService.getIncomeRecentTransactions()
        return response.map {
            it.toDomain()
        }
    }
}