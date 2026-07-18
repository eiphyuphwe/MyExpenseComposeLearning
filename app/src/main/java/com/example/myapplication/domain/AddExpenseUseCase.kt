package com.example.myapplication.domain

import com.example.myapplication.data.repository.expense.ExpenseRepository
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.flow.Flow

class AddExpenseUseCase(private val expenseRepository: ExpenseRepository) {

    suspend fun addExpense(expense: Expense): Resource<Unit> {
        return expenseRepository.addExpense(expense)
    }

    fun getAllExpenses(): Flow<List<Expense>> = expenseRepository.getExpenses()
}