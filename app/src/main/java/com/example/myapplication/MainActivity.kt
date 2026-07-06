package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.data.repository.IncomeDashboardRepositoryImpl
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import com.example.myapplication.mock.MockIncomeTransactions
import com.example.myapplication.ui.common.ViewModelFactory
import com.example.myapplication.ui.dashboard.IncomeDashboardViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

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
            val uiState = dashboardVM.incomeDashboardUiState.collectAsStateWithLifecycle()
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = uiState.value.toString(),
                        modifier = Modifier.padding(innerPadding)
                    )
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}