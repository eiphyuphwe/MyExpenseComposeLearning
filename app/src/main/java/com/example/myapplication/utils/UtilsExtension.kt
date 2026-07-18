package com.example.myapplication.utils

import com.example.myapplication.model.expense.ExpenseCategory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun Long.covertToDateStr(): String {
// 1. Create a formatter matching your desired pattern
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // 2. Convert the Long milliseconds to an Instant, apply a TimeZone, and format it
    return Instant.ofEpochMilli(this)
        .atZone(ZoneId.systemDefault()) // Or use ZoneId.of("UTC")
        .format(formatter)
}

fun String.covertToDateLong(): Long {
    // Define the pattern matching your string structure
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Parse to LocalDate
    val localDate = LocalDate.parse(this, formatter)

    // Convert to epoch milliseconds using a specific time zone
    val dateLong =
        localDate.atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    return dateLong
}

fun String.toConvertExpenseCategory(): ExpenseCategory {
    val category = when (this.uppercase()) {
        "FOOD" -> ExpenseCategory.FOOD
        "TRANSPORT" -> ExpenseCategory.TRANSPORT
        "OFFICE" -> ExpenseCategory.OFFICE
        "OTHERS" -> ExpenseCategory.OTHERS
        else -> {
            ExpenseCategory.OTHERS
        }
    }
    return category
}

