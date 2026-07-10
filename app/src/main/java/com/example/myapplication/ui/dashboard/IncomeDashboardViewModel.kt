package com.example.myapplication.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.NetworkMonitor
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import com.example.myapplication.model.IncomeDashboard
import com.example.myapplication.model.filter.IncomeTransactionFilterStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class IncomeDashboardViewModel(
    private val getIncomeDashboardUseCase: GetIncomeDashboardUseCase,
    private val networkMonitor: NetworkMonitor
) :
    ViewModel() {

    private val _incomeDashboardUiState = MutableStateFlow<IncomeDashboardUiState>(
        IncomeDashboardUiState.Loading
    )
    val incomeDashboardUiState = _incomeDashboardUiState.asStateFlow()
    private var originalDashBoard: IncomeDashboard? = null

    private var loadJob: Job? = null

    init {
        loadIncomeDashboardData()
    }

    fun loadIncomeDashboardData() {
        // Cancel previous request if user retries quickly
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _incomeDashboardUiState.value = IncomeDashboardUiState.Loading

            if (!networkMonitor.isConnected()) {
                _incomeDashboardUiState.value = IncomeDashboardUiState.NoInternet()
                return@launch
            }
            try {
                originalDashBoard = getIncomeDashboardUseCase.getIncomeDashBoard()
                originalDashBoard?.let {
                    _incomeDashboardUiState.value = IncomeDashboardUiState.Success(it)
                }
            } catch (e: IOException) {
                _incomeDashboardUiState.value = IncomeDashboardUiState.Error(
                    "No Internet connection", true
                )
            } catch (e1: Exception) {
                _incomeDashboardUiState.value = IncomeDashboardUiState.Error(
                    e1.message ?: "Something wrong", false
                )
            }
        }
    }

    fun filterIncomeTransactionsByStatus(status: IncomeTransactionFilterStatus) {
        originalDashBoard?.let {
            val filteredDashboard =
                getIncomeDashboardUseCase.filterIncomeTransactionsByStatus(
                    filter = status,
                    transactions = it.recentTransaction
                )

            _incomeDashboardUiState.value = IncomeDashboardUiState.Success(
                data = filteredDashboard,
                status
            )
        }
    }
}