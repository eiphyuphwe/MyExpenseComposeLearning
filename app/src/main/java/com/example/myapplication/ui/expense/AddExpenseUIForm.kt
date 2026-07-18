package com.example.myapplication.ui.expense

data class AddExpenseUIForm(
    val title: String = "",
    val titleError: String? = null,

    val category: String = "",
    val categoryError: String? = null,

    val amount: String = "",
    val amountError: String? = null,

    val date: String = "Select date",

    val isSaving: Boolean = false
)