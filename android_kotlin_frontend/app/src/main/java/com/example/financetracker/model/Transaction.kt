package com.example.financetracker.model

import java.time.LocalDate
import java.util.UUID

enum class TransactionType { INCOME, EXPENSE }

// PUBLIC_INTERFACE
data class Transaction(
    /** Unique id for transaction */
    val id: String = UUID.randomUUID().toString(),
    /** Date of the transaction */
    val date: LocalDate,
    /** Description or memo */
    val description: String,
    /** Amount in BRL; positive value for both types; type indicates sign in UI */
    val amount: Double,
    /** Type INCOME or EXPENSE */
    val type: TransactionType,
)
