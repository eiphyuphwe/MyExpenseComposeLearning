package com.example.myapplication.model.expense

data class Expense(
     val id: Long,
     val title:String, val category: ExpenseCategory,
     val date: Long,
     val amount: Double
)

enum class ExpenseCategory {
    FOOD,
    TRANSPORT,
    OFFICE,
    OTHERS
}