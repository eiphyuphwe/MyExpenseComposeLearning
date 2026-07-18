package com.example.myapplication.ui.expense

import android.content.res.Resources
import android.icu.text.CaseMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.AddExpenseUseCase
import com.example.myapplication.model.expense.Expense
import com.example.myapplication.utils.Resource
import com.example.myapplication.utils.toConvertExpenseCategory
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExpenseViewModel(val expenseUseCase: AddExpenseUseCase) : ViewModel() {
    private var _expenseUIState = MutableStateFlow<ExpenseUIState>(ExpenseUIState())
    val expenseUIState = _expenseUIState.asStateFlow() //laoding
    private var _expenseUIEvent = MutableSharedFlow<ExpenseUiEvent>() //for one time event
    val expenseUIEvent = _expenseUIEvent.asSharedFlow()

    private var _form = MutableStateFlow<AddExpenseUIForm>(AddExpenseUIForm())
    val form = _form.asStateFlow()

    init {
        observeExpense()
    }


    fun onTitleChange(title: String) {
        _form.update {
            it.copy(title = title)
        }
    }

    fun onCategoryChange(category: String) {
        _form.update {
            it.copy(category = category)
        }
    }

    fun onDateChange(date: String) {
        _form.update {
            it.copy(date = date)
        }
    }

    fun onAmountChange(amount: String) {
        _form.update {
            it.copy(amount = amount)
        }
    }

    fun saveExpense() {
        val formSate = _form.value
        if (formSate.isSaving) return
        viewModelScope.launch { //reset the error first
            _form.update {
                it.copy(
                    titleError = null,
                    categoryError = null,
                    amountError = null
                )
            }
            //checking started
            var hasError = false

// ✅ validation
            if (formSate.title.isBlank()) {
                _form.update { it.copy(titleError = "Title cannot be empty") }
                hasError = true
            }

            if (formSate.category.isBlank()) {
                _form.update { it.copy(categoryError = "Category cannot be empty") }
                hasError = true
            }

            val amountValue = formSate.amount.toDoubleOrNull()
            if (amountValue == null || amountValue <= 0) {
                _form.update { it.copy(amountError = "Enter a valid amount") }
                hasError = true
            }

            // ✅ stop here if invalid
            if (hasError) return@launch


            _form.update {
                it.copy(isSaving = true)
            }
            val result = expenseUseCase.addExpense(
                Expense(
                    id = 0,
                    title = formSate.title,
                    category = formSate.category.toConvertExpenseCategory(),
                    amount = formSate.amount.toDouble(),
                    date = System.currentTimeMillis()
                )
            )
            when (result) {
                is Resource.Success -> {
                    _expenseUIEvent.emit(ExpenseUiEvent.SaveSuccess)
                    _form.value = AddExpenseUIForm()
                }
                is Resource.Error -> {
                    _expenseUIEvent.emit(ExpenseUiEvent.SaveError(result.message))
                }

                else -> {

                }
            }
            _form.update {
                it.copy(isSaving = false)
            }
        }
    }

    private fun observeExpense() {
        viewModelScope.launch {
            expenseUseCase.getAllExpenses().collectLatest { expenses ->
                _expenseUIState.update {
                    it.copy(data = expenses)
                }
            }
        }
    }
}