package com.example.myapplication.data.mapper

import com.example.myapplication.data.model.IncomeTransactionResponse
import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.model.IncomeTransactionStatus

fun IncomeTransactionResponse.toDomain(): IncomeTransaction {
    return IncomeTransaction(
        id = id.orEmpty(),
        transactionId = transactionId.orEmpty(),
        clientName = clientName.orEmpty(),
        amount = amount ?: 0.0,
        date = date.orEmpty(),
        status = status.toIncomeTransactionStatus()
    )
}

private fun String?.toIncomeTransactionStatus(): IncomeTransactionStatus {
    return when (this?.uppercase()) {
        "PAID" -> IncomeTransactionStatus.PAID
        "PENDING" -> IncomeTransactionStatus.PENDING
        else -> IncomeTransactionStatus.PENDING
    }
}