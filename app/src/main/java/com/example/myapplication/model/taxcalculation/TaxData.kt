package com.example.myapplication.model.taxcalculation

import java.math.BigDecimal

data class TaxData(
    val income: BigDecimal = BigDecimal.ZERO,
    val tax: BigDecimal = BigDecimal.ZERO,
    val netIncome: BigDecimal = BigDecimal.ZERO
)
