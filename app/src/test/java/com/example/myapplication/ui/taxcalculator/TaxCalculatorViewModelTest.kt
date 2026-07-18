package com.example.myapplication.ui.taxcalculator

import app.cash.turbine.test
import com.example.myapplication.MainDispatcherRule
import com.example.myapplication.domain.TaxCalculationUseCase
import com.example.myapplication.model.taxcalculation.TaxData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class TaxCalculatorViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: TaxCalculatorViewModel
    private val useCase: TaxCalculationUseCase = mockk()

    @Before
    fun setUp() {
        viewModel = TaxCalculatorViewModel(useCase)
    }

    @Test
    fun `onIncomeChange should update income state`() {
        // When
        viewModel.onIncomeChange("50000")

        // Then
        assertEquals("50000", viewModel.income)
    }

    @Test
    fun `calculateTax with valid income should update totalTax and clear income`() = runTest {
        // Given
        val incomeStr = "60000"
        val incomeDecimal = BigDecimal(incomeStr)
        val tax = BigDecimal("10770.00")
        val net = BigDecimal("49230.00")

        viewModel.onIncomeChange(incomeStr)
        coEvery { useCase.calculateTax(incomeDecimal) } returns tax
        coEvery { useCase.netIncomeTax(incomeDecimal, tax) } returns net

        viewModel.totalTax.test {
            // Initial state (TaxData with zeros/nulls)
            assertEquals(BigDecimal.ZERO, awaitItem().income)

            // When
            viewModel.calculateTax()

            // Then
            val updatedState = awaitItem()
            assertEquals(incomeDecimal, updatedState.income)
            assertEquals(tax, updatedState.tax)
            assertEquals(net, updatedState.netIncome)
            assertEquals("", viewModel.income)
        }
    }

    @Test
    fun `calculateTax with invalid income should not call useCase`() = runTest {
        // Given
        viewModel.onIncomeChange("invalid")

        viewModel.totalTax.test {
            val initialState = awaitItem()

            // When
            viewModel.calculateTax()

            // Then
            // Ensure no new state is emitted (or it's the same as initial)
            // coVerify { useCase.calculateTax(any()) } was not called
            assertEquals(BigDecimal.ZERO, initialState.income)
            expectNoEvents()
        }
    }

    @Test
    fun `selectTaxData should update selectedTaxData state`() {
        // Given
        val taxData = TaxData(BigDecimal("50000"), BigDecimal("7000"), BigDecimal("43000"))

        // When
        viewModel.selectTaxData(taxData)

        // Then
        assertEquals(taxData, viewModel.selectedTaxData)

        // When
        viewModel.selectTaxData(null)

        // Then
        assertNull(viewModel.selectedTaxData)
    }
}
