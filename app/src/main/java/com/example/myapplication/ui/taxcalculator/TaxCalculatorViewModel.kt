package com.example.myapplication.ui.taxcalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.TaxCalculationUseCase
import com.example.myapplication.model.taxcalculation.TaxData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import javax.inject.Inject

@HiltViewModel
class TaxCalculatorViewModel @Inject constructor(
    private val taxCalculationUseCase: TaxCalculationUseCase
) : ViewModel() {
    private var _totalTax = MutableStateFlow<TaxData>(TaxData())
    val totalTax: StateFlow<TaxData> = _totalTax.asStateFlow()
    var income by mutableStateOf("")

    var selectedTaxData by mutableStateOf<TaxData?>(null)
        private set

    fun calculateTax() {
        val trimmedIncome = income.trim()
        val incomeDecimal = trimmedIncome.toBigDecimalOrNull() ?: return // Or handle error
        viewModelScope.launch {
            val totalTax = taxCalculationUseCase.calculateTax(incomeDecimal)
            val netIncome = taxCalculationUseCase.netIncomeTax(incomeDecimal, totalTax)
            _totalTax.update {
                TaxData(incomeDecimal, totalTax, netIncome)
            }
            income = ""
        }
    }

    fun onIncomeChange(newIncome: String) {
        income = newIncome
    }

    fun selectTaxData(taxData: TaxData?) {
        selectedTaxData = taxData
    }
}
