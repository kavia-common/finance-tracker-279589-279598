package com.example.financetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financetracker.state.TransactionsState
import com.example.financetracker.ui.components.AddTransactionButtons
import com.example.financetracker.ui.components.AddTransactionDialog
import com.example.financetracker.ui.components.AppTitleBar
import com.example.financetracker.ui.components.MonthTopAppBar
import com.example.financetracker.ui.components.SummaryBar
import com.example.financetracker.ui.components.TransactionsTable
import com.example.financetracker.ui.theme.FinanceTheme
import java.time.LocalDate

/**
 * PUBLIC_INTERFACE
 * Main activity entrypoint.
 * Displays a top title bar, month navigation, a filtered transaction table,
 * controls to add income/expense, and a bottom summary bar updating in real time.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FinanceTheme {
                val state = remember { TransactionsState() }
                val showIncome = remember { mutableStateOf(false) }
                val showExpense = remember { mutableStateOf(false) }
                val snackbar = remember { SnackbarHostState() }

                Scaffold(
                    topBar = {
                        // Blue title bar above the month selector
                        AppTitleBar(title = "Sistema Financeiro")
                    },
                    bottomBar = {
                        SummaryBar(
                            income = state.incomeTotal(),
                            expense = state.expenseTotal(),
                            net = state.netBalance(),
                            formatter = state.formatter,
                        )
                    },
                    snackbarHost = { SnackbarHost(hostState = snackbar) }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .systemBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Month selector directly below the title bar
                        MonthTopAppBar(
                            state = state,
                            onPrev = { state.setMonth(state.selectedMonth.minusMonths(1)) },
                            onNext = { state.setMonth(state.selectedMonth.plusMonths(1)) }
                        )

                        TransactionsTable(
                            items = state.monthTransactions(),
                            formatter = state.formatter,
                            modifier = Modifier
                        )
                        AddTransactionButtons(
                            onAddIncome = { showIncome.value = true },
                            onAddExpense = { showExpense.value = true }
                        )
                    }

                    if (showIncome.value) {
                        AddTransactionDialog(
                            title = "Adicionar Receita",
                            onDismiss = { showIncome.value = false },
                            onConfirm = { dateStr, desc, amtStr ->
                                try {
                                    val date = if (dateStr.isBlank()) LocalDate.now() else LocalDate.parse(dateStr)
                                    val amount = amtStr.replace(",", ".").toDouble()
                                    state.addIncome(date, desc.ifBlank { "Receita" }, amount)
                                    showIncome.value = false
                                } catch (_: Exception) {
                                    // ignore parse error for now
                                }
                            }
                        )
                    }

                    if (showExpense.value) {
                        AddTransactionDialog(
                            title = "Adicionar Despesa",
                            onDismiss = { showExpense.value = false },
                            onConfirm = { dateStr, desc, amtStr ->
                                try {
                                    val date = if (dateStr.isBlank()) LocalDate.now() else LocalDate.parse(dateStr)
                                    val amount = amtStr.replace(",", ".").toDouble()
                                    state.addExpense(date, desc.ifBlank { "Despesa" }, amount)
                                    showExpense.value = false
                                } catch (_: Exception) {
                                    // ignore parse error for now
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
