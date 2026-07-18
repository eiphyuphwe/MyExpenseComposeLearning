package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.ui.dashboard.IncomeDashboardUiState
import com.example.myapplication.ui.dashboard.IncomeDashboardViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextSecondary
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.myapplication.model.filter.IncomeTransactionFilterStatus
import com.example.myapplication.model.filter.displayName
import com.example.myapplication.ui.dashboard.EmptyRecentTransactions
import com.example.myapplication.ui.dashboard.IncomeDashboardScreen
import com.example.myapplication.ui.expense.ExpenseScreen
import com.example.myapplication.ui.expense.ExpenseViewModel
import com.example.myapplication.ui.navigation.AppMainScreen
import com.example.myapplication.ui.taxcalculator.TaxCalculatorScreen
import com.example.myapplication.ui.taxcalculator.TaxCalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dashboardVM: IncomeDashboardViewModel by viewModels()
    private val expenseViewModel: ExpenseViewModel by viewModels()
    private val taxViewModel: TaxCalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                AppMainScreen(
                    incomeDashboardRoute = {
                        val uiState by dashboardVM.incomeDashboardUiState.collectAsStateWithLifecycle()
                        when (val state = uiState) {
                            is IncomeDashboardUiState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is IncomeDashboardUiState.Success -> {
                                if (state.data.recentTransaction.isNotEmpty()) {
                                    IncomeDashboardScreen (
                                        dashboard = state.data,
                                        selectedFilter = state.incomeTransactionFilterStatus,
                                        onFilterSelected = dashboardVM::filterIncomeTransactionsByStatus
                                    )
                                } else {
                                    EmptyRecentTransactions()
                                }
                            }

                            is IncomeDashboardUiState.Error -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = state.message)
                                }
                            }

                            is IncomeDashboardUiState.NoInternet -> {
                                NoInternetScreen {
                                    dashboardVM.loadIncomeDashboardData()
                                }
                            }
                        }
                    },
                    expenseRoute = {
                        ExpenseScreen(
                            viewModel = expenseViewModel
                        )
                    },
                    taxRoute = {
                        TaxCalculatorScreen(
                            innerPadding = PaddingValues(),
                            viewModel = taxViewModel,
                        )
                    }
                )
            }
        }
    }

}

@Composable
fun NoInternetScreen(
    onRetryClicked: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .padding(24.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Color(0xFF8B2E2E),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Couldn't load your dashboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Check your connection and try again.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(
                onClick = onRetryClicked
            ) {
                Text("Retry")
            }
        }
    }
}


private fun formatCurrency(amount: Double): String =
    "NZ" + "%,.2f".format(amount)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        TransactionStatusFilterDropdown(
            IncomeTransactionFilterStatus.PAID,
            {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionStatusFilterDropdown(
    selectedFilter: IncomeTransactionFilterStatus,
    onFilterSelected: (IncomeTransactionFilterStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    // Controls whether the dropdown menu is open or closed
    var expanded by remember { mutableStateOf(false) }

    // List of filter options shown in the dropdown
    val filterOptions = IncomeTransactionFilterStatus.entries

    Column(modifier = modifier.fillMaxWidth()) {

        // Label above dropdown
        Text(
            text = "Filter by status",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                // Open/close dropdown when user taps the field
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedFilter.displayName(),
                onValueChange = {
                    // Read-only field, so we do not update text manually
                },
                readOnly = true,
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                singleLine = true,

                // Dropdown arrow icon
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    // Close dropdown when user taps outside
                    expanded = false
                }
            ) {
                filterOptions.forEach { filter ->
                    DropdownMenuItem(
                        text = {
                            Text(text = filter.displayName())
                        },
                        onClick = {
                            // 1. Send selected filter to ViewModel
                            onFilterSelected(filter)

                            // 2. Close dropdown after selecting item
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
