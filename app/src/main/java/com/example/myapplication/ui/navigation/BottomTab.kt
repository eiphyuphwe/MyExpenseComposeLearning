package com.example.myapplication.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomTab(
    val title: String,
    val route: String,
    val icon: ImageVector
)

val bottomTabs = listOf(
    BottomTab(
        title = "Income",
        route = AppRoute.INCOME,
        icon = Icons.Default.Payments
    ),
    BottomTab(
        title = "Expenses",
        route = AppRoute.EXPENSE,
        icon = Icons.Default.ReceiptLong
    ),
    BottomTab(
        title = "Tax",
        route = AppRoute.TAX,
        icon = Icons.Default.Calculate
    )
)