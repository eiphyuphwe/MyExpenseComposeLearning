package com.example.myapplication.data.repository.expense

import com.example.myapplication.data.local.entity.ExpenseEntity
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    suspend fun addExpense(expense: Expense): Resource<Unit>
    fun getExpenses(): Flow<List<Expense>>
}