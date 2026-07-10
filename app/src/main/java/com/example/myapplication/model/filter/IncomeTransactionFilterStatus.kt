package com.example.myapplication.model.filter

enum class IncomeTransactionFilterStatus {
    PAID,
    PENDING,
    ALL
}

fun IncomeTransactionFilterStatus.displayName(): String {
    return when (this) {
        IncomeTransactionFilterStatus.ALL -> "All"
        IncomeTransactionFilterStatus.PAID -> "Paid"
        IncomeTransactionFilterStatus.PENDING -> "Pending"
    }
}