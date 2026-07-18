package com.example.myapplication.model.taxcalculation

import java.math.BigDecimal

data class TaxBracket(
    val lowerLimit: BigDecimal,
    val higherLimit: BigDecimal,
    val rate: BigDecimal
)