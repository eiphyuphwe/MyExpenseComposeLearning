package com.example.myapplication.domain

import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.model.IncomeDashboard
import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.model.IncomeTransactionStatus
import com.example.myapplication.model.filter.IncomeTransactionFilterStatus
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

const val taxPercentage: Double = 0.2

class GetIncomeDashboardUseCase(private val repository: IncomeDashboardRepository) {

    suspend fun getIncomeDashBoard(): IncomeDashboard {
        delay(2000.milliseconds)
        val transactionList = repository.getIncomeTransactions()
        return createDashboard(transactionList)
    }

    fun getTotalIncome(transList: List<IncomeTransaction>): Double {
        return transList.filter {
            it.status == IncomeTransactionStatus.PAID
        }.sumOf { it.amount }
    }

    fun getTaxAside(totalIncome: Double): Double = totalIncome * taxPercentage

    fun getNetAvailable(totalIncome: Double, totalTaxAside: Double): Double =
        totalIncome - totalTaxAside

    fun filterIncomeTransactionsByStatus(
        filter: IncomeTransactionFilterStatus,
        transactions: List<IncomeTransaction>
    ): IncomeDashboard {

        val filteredTransactions =
            when (filter) {

                IncomeTransactionFilterStatus.ALL ->
                    transactions

                IncomeTransactionFilterStatus.PAID ->
                    transactions.filter {
                        it.status == IncomeTransactionStatus.PAID
                    }

                IncomeTransactionFilterStatus.PENDING ->
                    transactions.filter {
                        it.status == IncomeTransactionStatus.PENDING
                    }
            }

        return createDashboard(filteredTransactions)
    }

    private fun createDashboard(
        transactions: List<IncomeTransaction>
    ): IncomeDashboard {

        val totalIncome = getTotalIncome(transactions)
        val taxAside = getTaxAside(totalIncome)
        val netAvailable = getNetAvailable(
            totalIncome,
            taxAside
        )

        return IncomeDashboard(
            totalIncomeReceived = totalIncome,
            taxSetAside = taxAside,
            netAvailable = netAvailable,
            recentTransaction = transactions
        )
    }
}