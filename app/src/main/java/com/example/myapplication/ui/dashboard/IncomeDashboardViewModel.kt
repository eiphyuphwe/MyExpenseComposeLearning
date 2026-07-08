package com.example.myapplication.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.core.NetworkMonitor
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class IncomeDashboardViewModel(private val getIncomeDashboardUseCase: GetIncomeDashboardUseCase,
    private val networkMonitor: NetworkMonitor) :
    ViewModel() {

    private val _incomeDashboardUiState = MutableStateFlow<IncomeDashboardUiState>(
        IncomeDashboardUiState.Loading)
    val incomeDashboardUiState = _incomeDashboardUiState.asStateFlow()

    init {
        loadIncomeDashboardData()
    }

    fun loadIncomeDashboardData() {
        viewModelScope.launch {
            _incomeDashboardUiState.value = IncomeDashboardUiState.Loading

            if(!networkMonitor.isConnected()) {
                _incomeDashboardUiState.value = IncomeDashboardUiState.NoInternet()
                return@launch
            }

               try {
                   _incomeDashboardUiState.value =  IncomeDashboardUiState.Success(getIncomeDashboardUseCase.getIncomeDashBoard())
               }catch (e: IOException) {
                   _incomeDashboardUiState.value = IncomeDashboardUiState.Error(
                       "No Internet connection", true
                   )
               }catch (e1: Exception) {
                   _incomeDashboardUiState.value = IncomeDashboardUiState.Error(
                       e1.message ?: "Something wrong", false
                   )
               }
        }
    }
}