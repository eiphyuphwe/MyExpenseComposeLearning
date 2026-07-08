package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.data.repository.IncomeDashboardRepositoryImpl
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import com.example.myapplication.mock.MockIncomeTransactions
import com.example.myapplication.model.IncomeDashboard
import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.model.IncomeTransactionStatus
import com.example.myapplication.ui.common.ViewModelFactory
import com.example.myapplication.ui.dashboard.IncomeDashboardUiState
import com.example.myapplication.ui.dashboard.IncomeDashboardViewModel
import com.example.myapplication.ui.theme.CardBackground
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.StatusPaidText
import com.example.myapplication.ui.theme.TextMuted
import com.example.myapplication.ui.theme.TextSecondary

class MainActivity : ComponentActivity() {

    private val repository: IncomeDashboardRepository by lazy {
        IncomeDashboardRepositoryImpl(MockIncomeTransactions.incomeTransactions)
    }
    private val incomeDashboardUseCase: GetIncomeDashboardUseCase  by lazy {
        GetIncomeDashboardUseCase(repository)
    }
    private val dashboardVM: IncomeDashboardViewModel by viewModels {
        ViewModelFactory { IncomeDashboardViewModel(incomeDashboardUseCase) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val uiState by dashboardVM.incomeDashboardUiState.collectAsStateWithLifecycle()
            MyApplicationTheme {
                when(val state = uiState)  {
                    is IncomeDashboardUiState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    is IncomeDashboardUiState.Success -> {
                        IncomeDashboardScreen(dashboard = state.data)
                    }
                    is IncomeDashboardUiState.Error -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = state.message)
                        }
                    }
                }
                }
            }
        }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello Ei Phyu Phwe$name!",
        modifier = modifier
    )
}

@Composable
fun IncomeDashboardScreen(
    dashboard: IncomeDashboard,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = CardBackground
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(Color.White)
                .padding(20.dp)
        ) {
            Text(
                text = "Income dashboard",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(16.dp))

            TotalIncomeCard(amount = dashboard.totalIncomeReceived)

            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(
                    label = "Tax set aside",
                    amount = dashboard.taxSetAside,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    label = "Net available",
                    amount = dashboard.netAvailable,
                    modifier = Modifier.weight(1f),
                    valueColor = StatusPaidText
                )
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "Recent transactions",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            Spacer(Modifier.height(4.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(
                    items = dashboard.recentTransaction,
                    key = { _, txn -> txn.transactionId }
                ) { index, txn ->
                    TransactionRow(txn)
                    if (index < dashboard.recentTransaction.lastIndex) {
                        HorizontalDivider(color = Color(0xFFEDEDED))
                    }
                }

            }
        }

    }

}

@Composable
private fun TotalIncomeCard(amount: Double){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Text(
            text = "Total income received",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun SummaryCard(
    label: String,
    amount: Double,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Black
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(16.dp)
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
        Spacer(Modifier.height(4.dp))
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
private fun TransactionRow(transaction: IncomeTransaction) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {
            Text(
                text = transaction.clientName,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = transaction.transactionId,
                style = MaterialTheme.typography.bodySmall,
                color = TextMuted
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatCurrency(transaction.amount),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(4.dp))
            StatusBadge(transaction.status)
        }
    }
}

@Composable
private fun StatusBadge(status: IncomeTransactionStatus) {
    val (bg, text, label) = when (status) {
        IncomeTransactionStatus.PAID -> Triple(Color(0xFFDCEFD9), Color(0xFF2E7D32), "Paid")
        IncomeTransactionStatus.PENDING -> Triple(Color(0xFFF5E3C2), Color(0xFF8A6D1E), "Pending")
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ){
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = text)
    }
}


private fun formatCurrency(amount: Double): String =
    "NZ" + "%,.2f".format(amount)


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}