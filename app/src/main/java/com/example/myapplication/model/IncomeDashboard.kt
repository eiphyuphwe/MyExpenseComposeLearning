package com.example.myapplication.model

data class IncomeDashboard(
    val totalIncomeReceived: Double?,
    val taxSetAside: Double,
    val netAvailable: Double,
    val recentTransaction: List<IncomeTransaction>
)
