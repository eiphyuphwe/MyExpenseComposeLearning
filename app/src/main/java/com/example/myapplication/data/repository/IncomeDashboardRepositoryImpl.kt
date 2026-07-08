package com.example.myapplication.data.repository

import com.example.myapplication.model.IncomeTransaction

class IncomeDashboardRepositoryImpl(val transactionList: List<IncomeTransaction>): IncomeDashboardRepository {
    override suspend fun getIncomeTransactions(): List<IncomeTransaction> = transactionList

}