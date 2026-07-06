package com.example.myapplication.data.repository

import com.example.myapplication.model.IncomeTransaction

interface IncomeDashboardRepository {
    suspend fun getIncomeTransaction(): List<IncomeTransaction>
}