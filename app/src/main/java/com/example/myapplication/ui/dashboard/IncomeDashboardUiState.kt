package com.example.myapplication.ui.dashboard

import com.example.myapplication.model.IncomeDashboard

sealed class IncomeDashboardUiState {
    data object Loading : IncomeDashboardUiState()
    data class Success(val data: IncomeDashboard): IncomeDashboardUiState()
    data class Error(val message: String, val isNetworkError: Boolean): IncomeDashboardUiState()
}