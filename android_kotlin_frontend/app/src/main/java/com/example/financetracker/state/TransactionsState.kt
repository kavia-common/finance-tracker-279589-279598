package com.example.financetracker.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.financetracker.model.Transaction
import com.example.financetracker.model.TransactionType
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.util.Locale

// PUBLIC_INTERFACE
class TransactionsState(
    initialMonth: YearMonth = YearMonth.now(),
    seed: List<Transaction> = sampleSeed()
) {
    /** Selected month for filtering */
    var selectedMonth by mutableStateOf(initialMonth)
        private set

    /** All transactions in memory */
    private val all = mutableStateListOf<Transaction>().apply { addAll(seed) }

    /** Currency formatter for BRL */
    val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).apply {
        currency = java.util.Currency.getInstance("BRL")
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    // PUBLIC_INTERFACE
    fun setMonth(month: YearMonth) {
        selectedMonth = month
    }

    // PUBLIC_INTERFACE
    fun addIncome(date: LocalDate, description: String, amount: Double) {
        all.add(
            Transaction(
                date = date,
                description = description,
                amount = amount,
                type = TransactionType.INCOME
            )
        )
    }

    // PUBLIC_INTERFACE
    fun addExpense(date: LocalDate, description: String, amount: Double) {
        all.add(
            Transaction(
                date = date,
                description = description,
                amount = amount,
                type = TransactionType.EXPENSE
            )
        )
    }

    // PUBLIC_INTERFACE
    fun monthTransactions(): List<Transaction> {
        return all.filter { t ->
            val ym = YearMonth.from(t.date)
            ym == selectedMonth
        }.sortedByDescending { it.date }
    }

    // PUBLIC_INTERFACE
    fun incomeTotal(): Double = monthTransactions()
        .filter { it.type == TransactionType.INCOME }
        .sumOf { it.amount }

    // PUBLIC_INTERFACE
    fun expenseTotal(): Double = monthTransactions()
        .filter { it.type == TransactionType.EXPENSE }
        .sumOf { it.amount }

    // PUBLIC_INTERFACE
    fun netBalance(): Double = incomeTotal() - expenseTotal()

    companion object {
        private fun sampleSeed(): List<Transaction> {
            val today = LocalDate.now()
            val ym = YearMonth.from(today)
            return listOf(
                Transaction(
                    date = ym.atDay(3),
                    description = "Salário",
                    amount = 5500.0,
                    type = TransactionType.INCOME
                ),
                Transaction(
                    date = ym.atDay(5),
                    description = "Aluguel",
                    amount = 1800.0,
                    type = TransactionType.EXPENSE
                ),
                Transaction(
                    date = ym.atDay(10),
                    description = "Mercado",
                    amount = 620.45,
                    type = TransactionType.EXPENSE
                ),
                Transaction(
                    date = ym.atDay(15),
                    description = "Freelance",
                    amount = 1200.0,
                    type = TransactionType.INCOME
                )
            )
        }
    }
}
