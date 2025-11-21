package com.example.financetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.outlined.MoneyOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.financetracker.model.Transaction
import com.example.financetracker.model.TransactionType
import com.example.financetracker.state.TransactionsState
import java.time.format.DateTimeFormatter
import java.util.Locale

private val monthFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("pt", "BR"))
private val dayFormatter = DateTimeFormatter.ofPattern("dd/MM", Locale("pt", "BR"))

// PUBLIC_INTERFACE
@Composable
fun MonthTopAppBar(state: TransactionsState, onPrev: () -> Unit, onNext: () -> Unit) {
    Surface(tonalElevation = 2.dp, shadowElevation = 4.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onPrev) {
                Icon(Icons.Filled.ArrowBackIosNew, contentDescription = "Mês anterior")
            }
            Text(
                text = state.selectedMonth.format(monthFormatter)
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("pt","BR")) else it.toString() },
                style = LocalTextStyle.current.copy(fontWeight = FontWeight.SemiBold)
            )
            IconButton(onClick = onNext) {
                Icon(Icons.Filled.ArrowForwardIos, contentDescription = "Próximo mês")
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun TransactionsTable(modifier: Modifier = Modifier, items: List<Transaction>, formatter: java.text.NumberFormat) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(Modifier.background(MaterialTheme.colorScheme.surface)) {
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Data", fontWeight = FontWeight.SemiBold)
                Text("Descrição", fontWeight = FontWeight.SemiBold)
                Text("Valor", fontWeight = FontWeight.SemiBold)
            }
            Divider()
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Sem transações neste mês.", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn {
                    items(items) { t ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(t.date.format(dayFormatter))
                            Text(t.description, modifier = Modifier.weight(1f).padding(horizontal = 12.dp))
                            val color = if (t.type == TransactionType.INCOME) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.error
                            val sign = if (t.type == TransactionType.EXPENSE) "-" else "+"
                            val formatted = formatter.format(t.amount).toString()
                            val cleaned = formatted.replace("R$-", "R$ ")
                            Text("$sign$cleaned",
                                color = color, fontWeight = FontWeight.SemiBold)
                        }
                        Divider()
                    }
                }
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun SummaryBar(
    income: Double,
    expense: Double,
    net: Double,
    formatter: java.text.NumberFormat,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth().shadow(4.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp,
        shadowElevation = 6.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Receitas", color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.SemiBold)
                Text(formatter.format(income))
            }
            Column {
                Text("Despesas", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.SemiBold)
                Text(formatter.format(expense))
            }
            Column(horizontalAlignment = Alignment.End) {
                val netColor = when {
                    net > 0 -> MaterialTheme.colorScheme.secondary
                    net < 0 -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.onSurface
                }
                Text("Saldo", color = netColor, fontWeight = FontWeight.SemiBold)
                Text(formatter.format(net), color = netColor, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun AddTransactionButtons(
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        FloatingActionButton(
            onClick = onAddIncome,
            containerColor = MaterialTheme.colorScheme.secondary
        ) {
            Icon(Icons.Filled.AttachMoney, contentDescription = "Adicionar receita", tint = Color.Black)
        }
        FloatingActionButton(
            onClick = onAddExpense,
            containerColor = MaterialTheme.colorScheme.error
        ) {
            Icon(Icons.Outlined.MoneyOff, contentDescription = "Adicionar despesa", tint = Color.White)
        }
    }
}

// PUBLIC_INTERFACE
@Composable
fun AddTransactionDialog(
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (dateStr: String, description: String, amountStr: String) -> Unit
) {
    val date = remember { mutableStateOf(TextFieldValue("")) }
    val description = remember { mutableStateOf(TextFieldValue("")) }
    val amount = remember { mutableStateOf(TextFieldValue("")) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = date.value,
                    onValueChange = { date.value = it },
                    label = { Text("Data (yyyy-MM-dd)") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = description.value,
                    onValueChange = { description.value = it },
                    label = { Text("Descrição") },
                    singleLine = true
                )
                OutlinedTextField(
                    value = amount.value,
                    onValueChange = { amount.value = it },
                    label = { Text("Valor (ex: 1250.50)") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(date.value.text, description.value.text, amount.value.text) }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
