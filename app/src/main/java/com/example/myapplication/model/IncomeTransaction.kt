package com.example.myapplication.model

data class IncomeTransaction(
    val id: String = "",
    val transactionId: String,
    val clientName: String,
    val amount: Double,
    val date: String,
    val status: IncomeTransactionStatus?
)
