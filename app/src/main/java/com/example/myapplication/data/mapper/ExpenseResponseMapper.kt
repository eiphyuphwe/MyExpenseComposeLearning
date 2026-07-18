package com.example.myapplication.data.mapper

import com.example.myapplication.data.local.entity.ExpenseEntity
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.utils.toConvertExpenseCategory


fun ExpenseEntity.toDoMain() : Expense {
    return Expense(
        id = id,
        title = title,
        category = category.toConvertExpenseCategory(),   // or convert to enum if needed
        date = date,           // keep Long in domain ✅
        amount = amount
    )

}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        category = category.name,
        date = date,
        amount = amount
    )
}

