package com.example.myapplication.domain

import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.model.IncomeDashboard
import com.example.myapplication.model.IncomeTransactionStatus
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

const val taxPercentage: Double = 0.2
class GetIncomeDashboardUseCase(val repository: IncomeDashboardRepository) {

    suspend fun getIncomeDashBoard(): IncomeDashboard {
        delay(3000.milliseconds)
        val transactionList = repository.getIncomeTransactions()
        val paidTransactions = transactionList.filter {
            it.status == IncomeTransactionStatus.PAID
        }
        val totalIncome = paidTransactions.sumOf { it.amount }
        val totalTaxAside = totalIncome * taxPercentage
        val netAvailable = totalIncome - totalTaxAside
        return IncomeDashboard(totalIncome, totalTaxAside, netAvailable, transactionList)
    }
}