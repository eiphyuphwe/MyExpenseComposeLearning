package com.example.myapplication.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo("title")
    val title: String,
    @ColumnInfo("category")
    val category: String,
    @ColumnInfo("date")
    val date: Long,
    @ColumnInfo
    val amount: Double
)
