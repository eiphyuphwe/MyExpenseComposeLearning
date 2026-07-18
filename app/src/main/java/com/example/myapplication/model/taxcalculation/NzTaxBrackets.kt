package com.example.myapplication.model.taxcalculation

import java.math.BigDecimal
object NzTaxBrackets {
    val brackets = listOf(
        TaxBracket(
            lowerLimit = BigDecimal.ZERO,
            higherLimit = BigDecimal("14000"),
            rate = BigDecimal("0.105")
        ),
        TaxBracket(
            lowerLimit = BigDecimal("14000"),
            higherLimit = BigDecimal("50000"),
            rate = BigDecimal("0.175")
        ),
        TaxBracket(
            lowerLimit = BigDecimal("50000"),
            higherLimit = BigDecimal("80000"),
            rate = BigDecimal("0.30")
        ),
        TaxBracket(
            lowerLimit = BigDecimal("80000"),
            higherLimit = BigDecimal("150000"),
            rate = BigDecimal("0.33")
        ),
        TaxBracket(
            lowerLimit = BigDecimal("150000"),
            higherLimit = BigDecimal("1000000000"),
            rate = BigDecimal("0.39")
        )
    )
}