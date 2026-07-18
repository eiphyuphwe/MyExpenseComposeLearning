package com.example.myapplication.ui.dashboard

import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.core.NetworkMonitor
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals

class IncomeDashboardViewModelTest {


    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: GetIncomeDashboardUseCase
    private lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setup() {
        useCase = mockk()
        networkMonitor = mockk()
    }


    /**
     * TC5
     *
     * Given network is unavailable
     * When ViewModel loads dashboard data
     * Then NoInternet state should be emitted
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loadIncomeDashboardData_emitsNoInternet_whenDeviceIsOffline() = runTest {

        // Given
        every { networkMonitor.isConnected() } returns false

        // When
        val viewModel = IncomeDashboardViewModel(
            useCase,
            networkMonitor
        )

// Executes all pending coroutines scheduled on the TestDispatcher.
//
// The ViewModel automatically calls loadIncomeDashboardData()
// inside its init block.
//
// loadIncomeDashboardData() launches a coroutine using
// viewModelScope.launch, which runs asynchronously.
//
// advanceUntilIdle() forces the test scheduler to execute
// all queued coroutine work until there is nothing left to run.
//
// Without this call, assertions may run before the ViewModel
// finishes updating the UI state.
        advanceUntilIdle()

        // Then
        assertEquals(
            IncomeDashboardUiState.NoInternet(),
            viewModel.incomeDashboardUiState.value
        )
    }

    /**
     * TC6
     *
     * Given network is unavailable
     * When ViewModel loads dashboard data
     * Then UseCase should never be called
     */
    @Test
    fun loadIncomeDashboardData_doesNotCallUseCase_whenDeviceIsOffline() = runTest {

        // Given
        every { networkMonitor.isConnected() } returns false

        // When
        IncomeDashboardViewModel(
            useCase,
            networkMonitor
        )

        advanceUntilIdle()

        // Then
        coVerify(exactly = 0) {
            useCase.getIncomeDashBoard()
        }
    }
    /**
     * TC7
     *
     * Given network is available
     * And UseCase throws IOException
     * When dashboard loads
     * Then Error state should be emitted
     */
    @Test
    fun loadIncomeDashboardData_emitsError_whenIOExceptionOccurs() = runTest {

        // Given
        every { networkMonitor.isConnected() } returns true

        coEvery {
            useCase.getIncomeDashBoard()
        } throws IOException()

        // When
        val viewModel = IncomeDashboardViewModel(
            useCase,
            networkMonitor
        )

        advanceUntilIdle()

        // Then
        assertEquals(
            IncomeDashboardUiState.Error(
                message = "No Internet connection",
                isNetworkError = true
            ),
            viewModel.incomeDashboardUiState.value
        )
    }
    /**
     * TC8
     *
     * Given network is available
     * And UseCase throws RuntimeException
     * When dashboard loads
     * Then Error state should be emitted
     */
    @Test
    fun loadIncomeDashboardData_emitsError_whenGenericExceptionOccurs() = runTest {

        // Given
        every { networkMonitor.isConnected() } returns true

        coEvery {
            useCase.getIncomeDashBoard()
        } throws RuntimeException("Server Error")

        // When
        val viewModel = IncomeDashboardViewModel(
            useCase,
            networkMonitor
        )

        advanceUntilIdle()

        // Then
        assertEquals(
            IncomeDashboardUiState.Error(
                message = "Server Error",
                isNetworkError = false
            ),
            viewModel.incomeDashboardUiState.value
        )
    }

}