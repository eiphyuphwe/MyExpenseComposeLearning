package com.example.myapplication

import com.example.myapplication.data.repository.IncomeDashboardRepository
import com.example.myapplication.domain.GetIncomeDashboardUseCase
import com.example.myapplication.model.IncomeTransaction
import com.example.myapplication.model.IncomeTransactionStatus
import com.example.myapplication.model.filter.IncomeTransactionFilterStatus
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith


@OptIn(ExperimentalCoroutinesApi::class)
class GetIncomeDashboardUseCaseTest {

    private lateinit var repository: IncomeDashboardRepository

    private lateinit var useCase: GetIncomeDashboardUseCase

    @Before
    fun setup() {
        repository = mockk()
        useCase = GetIncomeDashboardUseCase(repository)
    }

    /**
     * Happy path:
     * Verify only PAID transactions contribute
     * to income, tax and net calculations.
     */
    @Test
    fun getIncomeDashboard_returnsCorrectValues_forMixedPaidAndPendingTransactions() =
        runTest {

            // Given
            val transactions = listOf(
                IncomeTransaction(
                    transactionId = "1",
                    clientName = "Client A",
                    amount = 1000.0,
                    status = IncomeTransactionStatus.PAID,
                    date = "10/06/2016"
                ),
                IncomeTransaction(
                    transactionId = "2",
                    clientName = "Client B",
                    amount = 500.0,
                    status = IncomeTransactionStatus.PENDING,
                    date = "10/06/2016"
                ),
                IncomeTransaction(
                    transactionId = "3",
                    clientName = "Client C",
                    amount = 2000.0,
                    status = IncomeTransactionStatus.PAID,
                    date = "10/06/2016"
                )
            )

            coEvery {
                repository.getIncomeTransactions()
            } returns transactions

            // When
            val dashboard = useCase.getIncomeDashBoard()

            // Then
            dashboard.totalIncomeReceived?.let { assertEquals(3000.0, it, 0.01) }
            assertEquals(600.0, dashboard.taxSetAside, 0.01)
            assertEquals(2400.0, dashboard.netAvailable, 0.01)
        }

    /**
     * Edge case:
     * A new user may not have any transactions.
     */
    @Test
    fun getIncomeDashboard_returnsZeros_whenTransactionListIsEmpty() =
        runTest {

            // Given
            coEvery {
                repository.getIncomeTransactions()
            } returns emptyList()

            // When
            val dashboard = useCase.getIncomeDashBoard()

            // Then
            dashboard.totalIncomeReceived?.let { assertEquals(0.0, it, 0.01) }
            assertEquals(0.0, dashboard.taxSetAside, 0.01)
            assertEquals(0.0, dashboard.netAvailable, 0.01)

            assertTrue(dashboard.recentTransaction.isEmpty())
        }

    /**
     * Edge case:
     * If all transactions are pending,
     * nothing should contribute to income.
     */
    @Test
    fun getIncomeDashboard_returnsZeroIncome_whenAllTransactionsArePending() =
        runTest {

            // Given
            val transactions = listOf(
                IncomeTransaction(
                    transactionId = "1",
                    clientName = "Client A",
                    amount = 1000.0,
                    status = IncomeTransactionStatus.PENDING,
                    date = "10/06/2016"
                ),
                IncomeTransaction(
                    transactionId = "2",
                    clientName = "Client B",
                    amount = 2000.0,
                    status = IncomeTransactionStatus.PENDING,
                    date = "10/06/2016"
                )
            )

            coEvery {
                repository.getIncomeTransactions()
            } returns transactions

            // When
            val dashboard = useCase.getIncomeDashBoard()

            // Then
            dashboard.totalIncomeReceived?.let { assertEquals(0.0, it, 0.01) }
            assertEquals(0.0, dashboard.taxSetAside, 0.01)
            assertEquals(0.0, dashboard.netAvailable, 0.01)
        }

    /**
     * Basic happy path:
     * Verify calculations for a single paid transaction.
     */
    @Test
    fun getIncomeDashboard_returnsCorrectValues_forSinglePaidTransaction() =
        runTest {

            // Given
            val transactions = listOf(
                IncomeTransaction(
                    transactionId = "1",
                    clientName = "Client A",
                    amount = 1000.0,
                    status = IncomeTransactionStatus.PAID,
                    date = "10/06/2016"
                )
            )

            coEvery {
                repository.getIncomeTransactions()
            } returns transactions

            // When
            val dashboard = useCase.getIncomeDashBoard()

            // Then
            dashboard.totalIncomeReceived?.let { assertEquals(1000.0, it, 0.01) }
            assertEquals(200.0, dashboard.taxSetAside, 0.01)
            assertEquals(800.0, dashboard.netAvailable, 0.01)
        }

    /**
     * Negative scenario:
     * UseCase does not handle repository exceptions.
     * It should propagate the exception to the caller.
     */
    @Test
    fun getIncomeDashboard_throwsException_whenRepositoryFails() =
        runTest {

            // Given
            coEvery {
                repository.getIncomeTransactions()
            } throws IOException("Network error")
            // Then
            assertFailsWith<IOException> {
                useCase.getIncomeDashBoard()
            }
        }

    /**
     * TC1
     *
     * Given a list of paid and pending transactions
     * When ALL filter is selected
     * Then all transactions should be returned
     */
    @Test
    fun filterIncomeTransactionsByStatus_returnsAllTransactions_whenAllFilterSelected() {

        // Given
        val transactions = listOf(
            IncomeTransaction(
                id = "1",
                transactionId = "V001",
                clientName = "Client A",
                amount = 1000.0,
                date = "10/06/2026",
                status = IncomeTransactionStatus.PAID
            ),
            IncomeTransaction(
                id = "2",
                transactionId = "V002",
                clientName = "Client B",
                amount = 500.0,
                date = "10/06/2026",
                status = IncomeTransactionStatus.PENDING
            )
        )

        // When
        val dashboard = useCase.filterIncomeTransactionsByStatus(
            IncomeTransactionFilterStatus.ALL,
            transactions
        )

        // Then
        assertEquals(2, dashboard.recentTransaction.size)
    }

    @Test
    fun filterIncomeTransactionsByStatus_returnsPAIDTransactions_whenAllFilterSelected() {
        // Given
        val transactions = listOf(
            IncomeTransaction(
                id = "1",
                transactionId = "V001",
                clientName = "Client A",
                amount = 1000.0,
                date = "10/06/2026",
                status = IncomeTransactionStatus.PAID
            ),
            IncomeTransaction(
                id = "2",
                transactionId = "V002",
                clientName = "Client B",
                amount = 500.0,
                date = "10/06/2026",
                status = IncomeTransactionStatus.PENDING
            )
        )

        // When
        val dashboard = useCase.filterIncomeTransactionsByStatus(
            IncomeTransactionFilterStatus.PAID,
            transactions
        )

        // Then
        assertEquals(1, dashboard.recentTransaction.size)
    }

}
