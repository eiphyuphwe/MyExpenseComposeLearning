package com.example.myapplication.ui.expense

import com.example.myapplication.model.expense.Expense

/*
sealed class ExpenseUIState {
    object Loading : ExpenseUIState()
    data class Success(val data: List<Expense>) : ExpenseUIState()
    data class Error(val message: String) : ExpenseUIState()
}*/

data class ExpenseUIState(
    val isLoading: Boolean = true,
    val data: List<Expense>  = emptyList(),
    val loadError: String? = null,
)
