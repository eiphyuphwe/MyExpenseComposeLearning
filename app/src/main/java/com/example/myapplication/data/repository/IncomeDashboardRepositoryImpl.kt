package com.example.myapplication.data.repository

import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.mock.MockIncomeTransactions

class IncomeDashboardRepositoryImpl(val transactionList: List<IncomeTransaction>): IncomeDashboardRepository {
    override suspend fun getIncomeTransaction(): List<IncomeTransaction> = transactionList

}