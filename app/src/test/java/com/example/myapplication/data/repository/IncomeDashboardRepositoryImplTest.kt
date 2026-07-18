package com.example.myapplication.data.repository

import com.example.myapplication.data.model.IncomeTransactionResponse
import com.example.myapplication.data.remote.IncomeTaxApiService
import com.example.myapplication.model.IncomeTransactionStatus
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.test.assertFailsWith

class IncomeDashboardRepositoryImplTest {

    private lateinit var api: IncomeTaxApiService
    private lateinit var repository: IncomeDashboardRepository

    @Before
    fun setUp() {
        api = mockk()
        repository = IncomeDashboardRepositoryImpl(api)
    }

    @Test
    fun getIncomeTransactions_returnsData() = runTest {


// Given
        val response = listOf(
            IncomeTransactionResponse(
                id = "1",
                transactionId = "INV-001",
                clientName = "ABC Ltd",
                amount = 1000.0,
                date = "2025-07-01",
                status = "PAID"
            )
        )

        coEvery {
            api.getIncomeRecentTransactions()
        } returns response

        // When

        val result =
            repository.getIncomeTransactions()
        TestCase.assertEquals(result.size, 1)


        TestCase.assertEquals(
            "INV-001",
            result.first().transactionId
        )

        TestCase.assertEquals(
            IncomeTransactionStatus.PAID,
            result.first().status
        )


    }

    /**
     * TC2
     *
     * Given API returns an empty list
     * When repository fetches data
     * Then repository should return an empty list.
     */
    @Test
    fun getIncomeTransactions_returnsEmptyList_whenApiReturnsEmptyList() =
        runTest {

            // Given
            coEvery {
                api.getIncomeRecentTransactions()
            } returns emptyList()

            // When
            val result = repository.getIncomeTransactions()

            // Then
            TestCase.assertTrue(result.isEmpty())
        }

    /**
     * TC3
     *
     * Given API throws an exception
     * When repository fetches data
     * Then the exception should propagate
     * to the caller.
     */
    @Test
    fun getIncomeTransactions_throwsException_whenApiFails() =
        runTest {

            // Given
            coEvery {
                api.getIncomeRecentTransactions()
            } throws IOException("Network Error")

            // Then
            assertFailsWith<IOException> {
                repository.getIncomeTransactions()
            }
        }
}