package com.example.myapplication.domain

import com.example.myapplication.model.taxcalculation.NzTaxBrackets
import java.math.BigDecimal

class TaxCalculationUseCase {
    suspend fun calculateTax(income: BigDecimal): BigDecimal {
        var totalTax = BigDecimal.ZERO
        for (bracket in NzTaxBrackets.brackets) {
            if (income > bracket.lowerLimit) {
                val taxableAmount = minOf(income, bracket.higherLimit) - bracket.lowerLimit
                val tax = taxableAmount * bracket.rate
                totalTax += tax
            }
        }
        return totalTax.setScale(2, java.math.RoundingMode.HALF_UP)
    }

    suspend fun netIncomeTax(income: BigDecimal, tax: BigDecimal): BigDecimal = income - tax
}