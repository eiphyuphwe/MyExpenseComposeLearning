package com.example.myapplication.domain

import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.model.IncomeDashboard

 const val taxPercentage: Double = 0.2
class GetIncomeDashboardUseCase(val repository: IncomeDashboardRepository) {

    suspend fun getIncomeDashBoard(): IncomeDashboard {
        val transactionList = repository.getIncomeTransaction()
        val totalIncome  = transactionList.sumOf { it.amount }
        val totalTaxAside = totalIncome * taxPercentage
        val netAvailable = totalIncome - totalTaxAside
        return IncomeDashboard(totalIncome, totalTaxAside, netAvailable, transactionList)
    }
}