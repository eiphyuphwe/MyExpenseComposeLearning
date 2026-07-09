package com.example.myapplication.data.remote

import com.example.myapplication.data.model.IncomeTransactionResponse
import retrofit2.http.GET

interface IncomeTaxApiService {
    @GET("incomeTransactions")
    suspend fun getIncomeRecentTransactions(): List<IncomeTransactionResponse>
}