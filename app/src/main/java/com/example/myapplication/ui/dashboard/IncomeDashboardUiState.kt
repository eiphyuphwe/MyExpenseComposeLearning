package com.example.myapplication.ui.dashboard

import com.example.myapplication.model.IncomeDashboard
import com.example.myapplication.model.filter.IncomeTransactionFilterStatus

sealed class IncomeDashboardUiState {
    data object Loading : IncomeDashboardUiState()

    data class NoInternet(
        val message: String = "No internet connection"
    ) : IncomeDashboardUiState()

    data class Success(val data: IncomeDashboard,
        val incomeTransactionFilterStatus: IncomeTransactionFilterStatus =  IncomeTransactionFilterStatus.ALL): IncomeDashboardUiState()
    data class Error(val message: String, val isNetworkError: Boolean): IncomeDashboardUiState()
}