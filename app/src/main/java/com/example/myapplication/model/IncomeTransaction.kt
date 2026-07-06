package com.example.myapplication.model

data class IncomeTransaction(
    val transactionId: String,
    val clientName: String,
    val amount: Double,
    val date: String,
    val status: IncomeTransactionStatus
)
