package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NigerianBank
import com.example.data.NigerianBankData
import com.example.data.TransactionEntity
import com.example.ui.BankAlertViewModel
import com.example.ui.components.BankSelectorDialog
import com.example.ui.theme.AlertCreditGreen
import com.example.ui.theme.AlertDebitRed
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.OPayGreen
import com.example.util.SmsAlertGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    viewModel: BankAlertViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsState()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "DEBIT", "CREDIT"
    var showClearConfirm by remember { mutableStateOf(false) }

    var showAddTxDialog by remember { mutableStateOf(false) }
    var showBankPicker by remember { mutableStateOf(false) }
    var newTxType by remember { mutableStateOf("CREDIT") }
    var newTxAmount by remember { mutableStateOf("") }
    var newTxBank by remember { mutableStateOf(NigerianBankData.getBankById("access")) }
    var newTxAccountNumber by remember { mutableStateOf("") }
    var newTxCounterparty by remember { mutableStateOf("") }
    var newTxNarration by remember { mutableStateOf("Transfer") }

    val filteredList = remember(transactions, searchQuery, selectedFilter) {
        transactions.filter { tx ->
            val matchesFilter = when (selectedFilter) {
                "DEBIT" -> tx.type == "DEBIT"
                "CREDIT" -> tx.type == "CREDIT"
                else -> true
            }
            val matchesSearch = if (searchQuery.isBlank()) true else {
                tx.recipientName.contains(searchQuery, ignoreCase = true) ||
                        tx.recipientBank.contains(searchQuery, ignoreCase = true) ||
                        tx.senderBank.contains(searchQuery, ignoreCase = true) ||
                        tx.reference.contains(searchQuery, ignoreCase = true) ||
                        tx.narration.contains(searchQuery, ignoreCase = true)
            }
            matchesFilter && matchesSearch
        }
    }

    if (showClearConfirm) {
        AlertDialog(
            onDismissRequest = { showClearConfirm = false },
            title = { Text("Clear Transaction History") },
            text = { Text("Are you sure you want to delete all saved bank transfers and generated SMS alerts?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearAllHistory()
                        showClearConfirm = false
                    }
                ) {
                    Text("Clear All", color = AlertDebitRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAddTxDialog) {
        AlertDialog(
            onDismissRequest = { showAddTxDialog = false },
            title = { Text("Record New Transaction", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Type selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = newTxType == "CREDIT",
                            onClick = { newTxType = "CREDIT" },
                            label = { Text("Credit (CR +)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AlertCreditGreen,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            selected = newTxType == "DEBIT",
                            onClick = { newTxType = "DEBIT" },
                            label = { Text("Debit (DR -)") },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = AlertDebitRed,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }

                    OutlinedTextField(
                        value = newTxAmount,
                        onValueChange = { newTxAmount = it.filter { c -> c.isDigit() || c == '.' } },
                        label = { Text("Amount (₦)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newTxCounterparty,
                        onValueChange = { newTxCounterparty = it },
                        label = { Text(if (newTxType == "CREDIT") "Sender Name" else "Beneficiary Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedButton(
                        onClick = { showBankPicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Bank: ${newTxBank.name}")
                    }

                    OutlinedTextField(
                        value = newTxAccountNumber,
                        onValueChange = { newTxAccountNumber = it.filter { c -> c.isDigit() }.take(10) },
                        label = { Text("Account Number (10 digits)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newTxNarration,
                        onValueChange = { newTxNarration = it },
                        label = { Text("Narration / Purpose") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = newTxAmount.toDoubleOrNull() ?: 0.0
                        if (amt > 0.0) {
                            val name = newTxCounterparty.ifBlank { "BENEFICIARY" }
                            val acc = newTxAccountNumber.ifBlank { "0123456789" }
                            viewModel.saveCustomTransaction(
                                type = newTxType,
                                amount = amt,
                                bank = newTxBank,
                                accountNumber = acc,
                                counterpartyName = name,
                                narration = newTxNarration
                            )
                            showAddTxDialog = false
                            newTxAmount = ""
                            newTxCounterparty = ""
                            newTxAccountNumber = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = OPayGreen)
                ) {
                    Text("Save to History")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTxDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showBankPicker) {
        BankSelectorDialog(
            selectedBank = newTxBank,
            onBankSelected = {
                newTxBank = it
                showBankPicker = false
            },
            onDismiss = { showBankPicker = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("history_screen_container")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Transfer & Alert History",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${transactions.size} transactions saved in database",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Record new transaction
                IconButton(
                    onClick = { showAddTxDialog = true },
                    modifier = Modifier.testTag("record_new_tx_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Record Transaction",
                        tint = OPayGreen
                    )
                }

                // Export statement
                if (transactions.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            viewModel.exportStatement(context, filteredList.ifEmpty { transactions })
                        },
                        modifier = Modifier.testTag("export_statement_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Save / Export Statement",
                            tint = OPayGreen
                        )
                    }

                    IconButton(
                        onClick = { showClearConfirm = true },
                        modifier = Modifier.testTag("clear_history_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Clear History",
                            tint = AlertDebitRed
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = OPayGreen.copy(alpha = 0.09f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Storage,
                    contentDescription = null,
                    tint = OPayGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Saved to local Room database • Available offline",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    color = OPayGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, bank, or reference...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("history_search_field"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Filter Chips (All, Debits, Credits)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("ALL" to "All", "DEBIT" to "Debits (DR)", "CREDIT" to "Credits (CR)").forEach { (key, label) ->
                FilterChip(
                    selected = selectedFilter == key,
                    onClick = { selectedFilter = key },
                    label = { Text(label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = NaijaGreenPrimary.copy(alpha = 0.15f),
                        selectedLabelColor = NaijaGreenPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Transactions List
        if (filteredList.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (transactions.isEmpty()) "No transactions yet" else "No matching transactions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (transactions.isEmpty()) "Perform a bank transfer to generate receipts and SMS alerts" else "Try adjusting your search query",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredList, key = { it.id }) { tx ->
                    TransactionItemCard(
                        transaction = tx,
                        onViewReceipt = { viewModel.showReceipt(tx) },
                        onCopySms = { viewModel.copySmsToClipboard(tx.smsBody) },
                        onShare = { viewModel.shareSms(tx.smsBody, tx.smsSenderHeader) },
                        onDelete = { viewModel.deleteTransaction(tx.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionItemCard(
    transaction: TransactionEntity,
    onViewReceipt: () -> Unit,
    onCopySms: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val isDebit = transaction.type == "DEBIT"
    val dtStr = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.US).format(Date(transaction.timestamp))
    val bank = NigerianBankData.getBankByName(if (isDebit) transaction.recipientBank else transaction.senderBank)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tx_card_${transaction.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (isDebit) AlertDebitRed.copy(alpha = 0.12f)
                                else AlertCreditGreen.copy(alpha = 0.12f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isDebit) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = null,
                            tint = if (isDebit) AlertDebitRed else AlertCreditGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = transaction.recipientName,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${transaction.recipientBank} • ${transaction.recipientAccountNumber}",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = (if (isDebit) "- " else "+ ") + SmsAlertGenerator.formatNaira(transaction.amount),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = if (isDebit) AlertDebitRed else AlertCreditGreen
                    )
                    Text(
                        text = dtStr,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // SMS Preview Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SMS Sender: [${transaction.smsSenderHeader}]",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NaijaGreenPrimary
                            )
                        )
                        Text(
                            text = "Ref: ${transaction.reference.takeLast(10)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontSize = 9.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = transaction.smsBody,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick Actions Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        onClick = onViewReceipt,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ReceiptLong,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Receipt", style = MaterialTheme.typography.labelSmall)
                    }

                    TextButton(
                        onClick = onCopySms,
                        modifier = Modifier.height(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Copy SMS", style = MaterialTheme.typography.labelSmall)
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
