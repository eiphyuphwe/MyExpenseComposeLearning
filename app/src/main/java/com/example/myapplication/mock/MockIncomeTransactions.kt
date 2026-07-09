package com.example.myapplication.mock

import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.model.IncomeTransactionStatus

object MockIncomeTransactions {

    var incomeTransactions = listOf(
        IncomeTransaction(
            transactionId = "TXN-1042",
            clientName = "Acme Media Ltd",
            amount = 1250.00,
            date = "2 Jul 2026",
            status = IncomeTransactionStatus.PAID
        ),
        IncomeTransaction(
            transactionId = "TXN-1041",
            clientName = "Bluewave Design",
            amount = 860.00,
            date = "28 Jun 2026",
            status = IncomeTransactionStatus.PAID
        ),
        IncomeTransaction(
            transactionId = "TXN-1039",
            clientName = "Kowhai Studio",
            amount = 430.50,
            date = "24 Jun 2026",
            status = IncomeTransactionStatus.PENDING
        ),
        IncomeTransaction(
            transactionId = "TXN-1035",
            clientName = "Northline Ltd",
            amount = 2100.00,
            date = "18 Jun 2026",
            status = IncomeTransactionStatus.PAID
        ),
        IncomeTransaction(
            transactionId = "TXN-1032",
            clientName = "Pixel Works",
            amount = 520.00,
            date = "14 Jun 2026",
            status = IncomeTransactionStatus.PENDING
        )
    )
}