package com.example.myapplication.domain

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.math.BigDecimal

class TaxCalculationUseCaseTest {

    private lateinit var useCase: TaxCalculationUseCase

    @Before
    fun setUp() {
        useCase = TaxCalculationUseCase()
    }

    @Test
    fun `calculateTax for income in first bracket (under 14k)`() = runTest {
        val income = BigDecimal("10000")
        // 10000 * 0.105 = 1050.00
        val expectedTax = BigDecimal("1050.00")
        
        val result = useCase.calculateTax(income)
        
        assertEquals(expectedTax, result)
    }

    @Test
    fun `calculateTax for income in second bracket (14k to 50k)`() = runTest {
        val income = BigDecimal("30000")
        // 14000 * 0.105 = 1470
        // (30000 - 14000) * 0.175 = 16000 * 0.175 = 2800
        // Total = 1470 + 2800 = 4270
        val expectedTax = BigDecimal("4270.00")
        
        val result = useCase.calculateTax(income)
        
        assertEquals(expectedTax, result)
    }

    @Test
    fun `calculateTax for income in third bracket (50k to 80k)`() = runTest {
        val income = BigDecimal("60000")
        // 14000 * 0.105 = 1470
        // (50000 - 14000) * 0.175 = 36000 * 0.175 = 6300
        // (60000 - 50000) * 0.30 = 10000 * 0.30 = 3000
        // Total = 1470 + 6300 + 3000 = 10770
        val expectedTax = BigDecimal("10770.00")
        
        val result = useCase.calculateTax(income)
        
        assertEquals(expectedTax, result)
    }

    @Test
    fun `calculateTax for zero income should return zero tax`() = runTest {
        val income = BigDecimal.ZERO
        val expectedTax = BigDecimal("0.00")
        
        val result = useCase.calculateTax(income)
        
        assertEquals(expectedTax, result)
    }

    @Test
    fun `netIncomeTax should correctly subtract tax from income`() = runTest {
        val income = BigDecimal("50000")
        val tax = BigDecimal("7770")
        val expectedNet = BigDecimal("42230")
        
        val result = useCase.netIncomeTax(income, tax)
        
        assertEquals(expectedNet, result)
    }
}
