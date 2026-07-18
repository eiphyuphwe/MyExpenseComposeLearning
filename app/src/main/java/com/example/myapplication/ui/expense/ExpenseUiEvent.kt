package com.example.myapplication.ui.expense

sealed class ExpenseUiEvent {
    data object SaveSuccess : ExpenseUiEvent()
    data class SaveError(
        val message: String
    ) : ExpenseUiEvent()

    object ClearForm : ExpenseUiEvent()
}

