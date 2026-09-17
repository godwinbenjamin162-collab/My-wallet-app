package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HeadsetMic
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.NigerianBank
import com.example.data.NigerianBankData
import com.example.ui.BankAlertViewModel
import com.example.ui.MainTab
import com.example.ui.components.BankSelectorDialog
import com.example.ui.theme.AlertCreditGreen
import com.example.ui.theme.AlertDebitRed
import com.example.ui.theme.NaijaGold
import com.example.ui.theme.NaijaGreenDark
import com.example.ui.theme.NaijaGreenPrimary
import com.example.ui.theme.OPayBlue
import com.example.ui.theme.OPayBlueLight
import com.example.ui.theme.OPayGreen
import com.example.ui.theme.OPayGreenContainer
import com.example.ui.theme.OPayGreenDark
import com.example.ui.theme.OPayGreenLight
import com.example.ui.theme.OPayOrange
import com.example.ui.theme.OPayOrangeLight
import com.example.ui.theme.OPayPurple
import com.example.ui.theme.OPayPurpleLight
import com.example.ui.theme.OPayRed
import com.example.ui.theme.OPayTeal
import com.example.util.SmsAlertGenerator
import com.example.util.SmsDispatcher
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TransferScreen(
    viewModel: BankAlertViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userBank by viewModel.userBank.collectAsState()
    val userAccountName by viewModel.userAccountName.collectAsState()
    val userAccountNumber by viewModel.userAccountNumber.collectAsState()
    val userBalance by viewModel.userBalance.collectAsState()
    val isBalanceVisible by viewModel.isBalanceVisible.collectAsState()

    val recipientBank by viewModel.recipientBank.collectAsState()
    val recipientAccountNumber by viewModel.recipientAccountNumber.collectAsState()
    val recipientPhoneNumber by viewModel.recipientPhoneNumber.collectAsState()
    val autoSendSms by viewModel.autoSendSms.collectAsState()
    val lastSmsDeliveryResult by viewModel.lastSmsDeliveryResult.collectAsState()
    val recipientName by viewModel.recipientName.collectAsState()
    val isUserCustomizedName by viewModel.isUserCustomizedName.collectAsState()
    val isResolvingName by viewModel.isResolvingName.collectAsState()
    val transferAmount by viewModel.transferAmount.collectAsState()
    val narration by viewModel.narration.collectAsState()
    val alertType by viewModel.alertType.collectAsState()
    val isProcessingTransfer by viewModel.isProcessingTransfer.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    // Screen navigation state: Dashboard vs Inputting Account Details
    var isInputtingDetails by remember { mutableStateOf(false) }
    var showBankPicker by remember { mutableStateOf(false) }
    var showCustomPhoneField by remember { mutableStateOf(false) }

    val smsPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    if (showBankPicker) {
        BankSelectorDialog(
            selectedBank = recipientBank,
            onBankSelected = { viewModel.selectRecipientBank(it) },
            onDismiss = { showBankPicker = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("transfer_screen_container")
    ) {
        if (!isInputtingDetails) {
            // ==========================================
            // MAIN OPay DASHBOARD VIEW (HOME)
            // ==========================================

            // OPay User Greeting Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(OPayGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userAccountName.take(1).ifBlank { "B" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = OPayGreen
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Hi, ${userAccountName.split(" ").firstOrNull() ?: "Benjamin"}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(OPayGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Tier 3",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = OPayGreen
                                    )
                                )
                            }
                        }
                        Text(
                            text = "${userBank.name} • $userAccountNumber",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Quick Action Icons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { /* Customer Support */ },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HeadsetMic,
                            contentDescription = "Support",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    IconButton(
                        onClick = { /* QR scan */ },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = "Scan",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Box {
                        IconButton(
                            onClick = { viewModel.setTab(MainTab.ALERT_GENERATOR) },
                            modifier = Modifier.size(34.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = "Alerts",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(AlertDebitRed)
                                .align(Alignment.TopEnd)
                        )
                    }
                }
            }

            // OPay Total Balance Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("account_balance_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    OPayGreen,
                                    Color(0xFF009B62)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Total Balance",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { viewModel.toggleBalanceVisibility() },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("toggle_balance_btn")
                                ) {
                                    Icon(
                                        imageVector = if (isBalanceVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle Balance",
                                        tint = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.setTab(MainTab.HISTORY) }
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "Transaction History >",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    ),
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = if (isBalanceVisible) SmsAlertGenerator.formatNaira(userBalance) else "₦ • • • • • •",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // OPay Cashback & SMS Status Ribbon
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.18f))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CardGiftcard,
                                contentDescription = null,
                                tint = OPayOrange,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Cashback: ₦250.00 • 100% Free Instant Alerts Active",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // The 3 Classic OPay Action Buttons (Add Money, Transfer, Withdraw)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // + Add Money Button
                            Button(
                                onClick = {
                                    viewModel.triggerSmsAlert(
                                        type = "CREDIT",
                                        amount = 5000.0,
                                        counterparty = "OPAY WALLET TOPUP",
                                        bank = userBank,
                                        userAccountNumber = userAccountNumber
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.22f)
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Add Money",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                            }

                            // Transfer Button (Primary)
                            Button(
                                onClick = { isInputtingDetails = true },
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(44.dp)
                                    .testTag("transfer_main_button"),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = null,
                                        tint = OPayGreenDark,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Transfer",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 13.sp,
                                            color = OPayGreenDark
                                        )
                                    )
                                }
                            }

                            // Withdraw Button
                            Button(
                                onClick = {
                                    viewModel.triggerSmsAlert(
                                        type = "DEBIT",
                                        amount = 2000.0,
                                        counterparty = "ATM CASH WITHDRAWAL",
                                        bank = userBank,
                                        userAccountNumber = userAccountNumber
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp),
                                shape = RoundedCornerShape(22.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White.copy(alpha = 0.22f)
                                ),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NorthEast,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Withdraw",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.sp,
                                            color = Color.White
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // OPay Financial Services Grid (The 8 Services Grid)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Services",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Instant 0% Fee",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OPayGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Row 1 of services
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OPayServiceItem(
                            title = "To OPay",
                            icon = Icons.Default.PhoneAndroid,
                            iconBgColor = OPayGreenLight,
                            iconColor = OPayGreen,
                            onClick = {
                                viewModel.selectRecipientBank(NigerianBankData.getBankById("opay"))
                                isInputtingDetails = true
                            }
                        )
                        OPayServiceItem(
                            title = "To Bank",
                            icon = Icons.Default.AccountBalance,
                            iconBgColor = OPayBlueLight,
                            iconColor = OPayBlue,
                            testTag = "quick_transfer_tile",
                            onClick = { isInputtingDetails = true }
                        )
                        OPayServiceItem(
                            title = "Airtime",
                            icon = Icons.Default.PhoneAndroid,
                            iconBgColor = OPayOrangeLight,
                            iconColor = OPayOrange,
                            onClick = {
                                viewModel.setNarration("Airtime Recharge")
                                isInputtingDetails = true
                            }
                        )
                        OPayServiceItem(
                            title = "Data",
                            icon = Icons.Default.Wifi,
                            iconBgColor = OPayPurpleLight,
                            iconColor = OPayPurple,
                            onClick = {
                                viewModel.setNarration("Mobile Data Bundle")
                                isInputtingDetails = true
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Row 2 of services
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OPayServiceItem(
                            title = "Betting",
                            icon = Icons.Default.SportsSoccer,
                            iconBgColor = Color(0xFFFFEBEE),
                            iconColor = OPayRed,
                            onClick = {
                                viewModel.setNarration("Betting Top-up")
                                isInputtingDetails = true
                            }
                        )
                        OPayServiceItem(
                            title = "Electricity",
                            icon = Icons.Default.Bolt,
                            iconBgColor = Color(0xFFFFF8E1),
                            iconColor = Color(0xFFFFA000),
                            onClick = {
                                viewModel.setNarration("Electricity Bill")
                                isInputtingDetails = true
                            }
                        )
                        OPayServiceItem(
                            title = "TV Cable",
                            icon = Icons.Default.Tv,
                            iconBgColor = Color(0xFFE0F2F1),
                            iconColor = OPayTeal,
                            onClick = {
                                viewModel.setNarration("TV Subscription")
                                isInputtingDetails = true
                            }
                        )
                        OPayServiceItem(
                            title = "SMS Alerts",
                            icon = Icons.Default.Sms,
                            iconBgColor = OPayGreenLight,
                            iconColor = OPayGreen,
                            testTag = "quick_sms_hub_tile",
                            onClick = { viewModel.setTab(MainTab.ALERT_GENERATOR) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Recent Beneficiaries Carousel (OPay Style)
            if (transactions.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Beneficiaries",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )

                    TextButton(onClick = { viewModel.setTab(MainTab.HISTORY) }) {
                        Text("View All", color = OPayGreen, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Horizontal list of recent recipients
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val recentUnique = transactions.distinctBy { it.recipientAccountNumber }.take(6)
                    items(recentUnique) { tx ->
                        val b = NigerianBankData.getBankByName(tx.recipientBank)
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    viewModel.selectRecipientBank(b)
                                    viewModel.setRecipientAccountNumber(tx.recipientAccountNumber)
                                    viewModel.setRecipientName(tx.recipientName)
                                    isInputtingDetails = true
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(b.brandColor),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = b.shortName.take(2).uppercase(),
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = b.brandTextColor
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = tx.recipientName.split(" ").firstOrNull() ?: tx.recipientName,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1
                                )
                                Text(
                                    text = b.shortName,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Recent Transactions List (OPay Style)
            if (transactions.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent Transactions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    TextButton(onClick = { viewModel.setTab(MainTab.HISTORY) }) {
                        Text("All >", color = OPayGreen, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    transactions.take(4).forEach { tx ->
                        val isDebit = tx.type == "DEBIT"
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .clickable {
                                    viewModel.openReceipt(tx)
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isDebit) Color(0xFFF0F2F5) else OPayGreenLight
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = if (isDebit) Icons.Default.NorthEast else Icons.Default.SouthWest,
                                            contentDescription = null,
                                            tint = if (isDebit) Color(0xFF4A5568) else OPayGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = tx.recipientName.ifBlank { tx.recipientBank },
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            maxLines = 1
                                        )
                                        val formattedDate = SimpleDateFormat("dd MMM, hh:mm a", Locale.US).format(Date(tx.timestamp))
                                        Text(
                                            text = "$formattedDate • ${tx.recipientBank}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${if (isDebit) "-" else "+"}${SmsAlertGenerator.formatNaira(tx.amount)}",
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 15.sp
                                        ),
                                        color = if (isDebit) MaterialTheme.colorScheme.onSurface else OPayGreen
                                    )
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(OPayGreenLight)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "Successful",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OPayGreen
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
            }

            // Security & NIBSS Certification Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(OPayGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = OPayGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Licensed by CBN • Insured by NDIC",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "NIBSS Instant Payment (NIP) enabled. All bank transfers trigger instant debit and credit SMS alerts.",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 15.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }

        } else {
            // ==========================================
            // INPUT ACCOUNT DETAILS VIEW (AFTER CLICKING TRANSFER)
            // ==========================================

            // Top Bar with Back Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { isInputtingDetails = false },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .testTag("back_from_input_details_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Transfer to Bank Account",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Input beneficiary account details",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Compact Source Account Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "From: ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                        Text(
                            text = "${userBank.shortName} (${userAccountNumber})",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = "Bal: ${SmsAlertGenerator.formatNaira(userBalance)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NaijaGreenDark
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section Label: Recipient Bank
            Text(
                text = "Select Recipient Bank",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selected Bank Trigger Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { showBankPicker = true }
                    .testTag("select_bank_btn"),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(recipientBank.brandColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = recipientBank.shortName.take(2).uppercase(),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = recipientBank.brandTextColor
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Recipient Bank",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = recipientBank.name,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Change",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NaijaGreenPrimary
                            )
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = NaijaGreenPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Bank Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(NigerianBankData.banks.filter { it.isPopular }) { bank ->
                    val isSelected = bank.id == recipientBank.id
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectRecipientBank(bank) },
                        label = { Text(bank.shortName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = NaijaGreenPrimary.copy(alpha = 0.15f),
                            selectedLabelColor = NaijaGreenPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Beneficiary Row (Self + Recent transfers)
            val recentBeneficiaries = remember(transactions) {
                transactions.filter { it.recipientAccountNumber.isNotBlank() && it.recipientName.isNotBlank() }
                    .distinctBy { it.recipientAccountNumber }
                    .take(3)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Beneficiary Details",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = {
                        viewModel.selectBeneficiary("0239481729", "Guaranty Trust Bank", "BENJAMIN GODWIN")
                    },
                    modifier = Modifier.height(28.dp)
                ) {
                    Text(
                        text = "Self (Benjamin Godwin)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NaijaGreenPrimary
                        )
                    )
                }
            }

            if (recentBeneficiaries.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    items(recentBeneficiaries) { b ->
                        SuggestionChip(
                            onClick = {
                                viewModel.selectBeneficiary(b.recipientAccountNumber, b.recipientBank, b.recipientName)
                            },
                            label = { Text(b.recipientName.take(20)) },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        )
                    }
                }
            }

            // Recipient Account Number Field (NUBAN 10-digits)
            OutlinedTextField(
                value = recipientAccountNumber,
                onValueChange = { viewModel.setRecipientAccountNumber(it) },
                label = { Text("NUBAN Account Number (10 Digits)") },
                placeholder = { Text("e.g. 0123456789") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipient_account_field"),
                singleLine = true,
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${recipientAccountNumber.length}/10",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (recipientAccountNumber.length == 10) AlertCreditGreen else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        IconButton(
                            onClick = {
                                val text = clipboardManager.getText()?.text ?: ""
                                val digits = text.filter { it.isDigit() }.take(10)
                                if (digits.isNotEmpty()) {
                                    viewModel.setRecipientAccountNumber(digits)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentPaste,
                                contentDescription = "Paste",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Beneficiary / Account Name Field (Editable & NIBSS-verified)
            OutlinedTextField(
                value = recipientName,
                onValueChange = { viewModel.setRecipientName(it) },
                label = { Text("Account Name / Beneficiary") },
                placeholder = { Text("Auto-resolves or enter full name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (recipientName.isNotBlank()) AlertCreditGreen else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    if (isResolvingName) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = NaijaGreenPrimary
                        )
                    } else if (recipientName.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.reVerifyRecipientName() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Re-verify",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Verified",
                                tint = AlertCreditGreen,
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 4.dp)
                            )
                        }
                    }
                },
                supportingText = {
                    if (isResolvingName) {
                        Text(
                            text = "Querying NIBSS central banking directory...",
                            color = NaijaGreenPrimary,
                            style = MaterialTheme.typography.labelSmall
                        )
                    } else if (recipientName.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = AlertCreditGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isUserCustomizedName) "Custom beneficiary name specified (tap to edit)"
                                else "NIBSS Verified Account Name (tap to edit if needed)",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = if (isUserCustomizedName) NaijaGreenPrimary else AlertCreditGreen
                            )
                        }
                    } else {
                        Text(
                            text = "Auto-resolves on 10 digits, or type recipient name directly",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipient_name_field"),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Automatic SMS Alert Delivery to Account Number Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("auto_sms_delivery_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (autoSendSms) AlertCreditGreen.copy(alpha = 0.08f)
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                ),
                border = CardDefaults.outlinedCardBorder().copy(
                    brush = Brush.horizontalGradient(
                        if (autoSendSms) listOf(AlertCreditGreen.copy(alpha = 0.4f), AlertCreditGreen.copy(alpha = 0.4f))
                        else listOf(MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    )
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (autoSendSms) AlertCreditGreen.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sms,
                                    contentDescription = null,
                                    tint = if (autoSendSms) AlertCreditGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Auto-Send SMS Alert",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (recipientAccountNumber.isNotBlank())
                                        "SMS sent to: ${recipientPhoneNumber.ifBlank { SmsDispatcher.formatNigerianPhoneNumber(recipientAccountNumber) }}"
                                    else "Automatically sends SMS alert to account number",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }

                        Switch(
                            checked = autoSendSms,
                            onCheckedChange = { isChecked ->
                                viewModel.setAutoSendSms(isChecked)
                                if (isChecked && !SmsDispatcher.hasSmsPermission(context)) {
                                    smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = AlertCreditGreen
                            ),
                            modifier = Modifier.testTag("auto_send_sms_switch")
                        )
                    }

                    if (autoSendSms) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.Black.copy(alpha = 0.05f))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = AlertCreditGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (!SmsDispatcher.hasSmsPermission(context))
                                        "Will prompt for SMS permission on transfer"
                                    else "Active: Real SMS dispatched via Android SMS Manager",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                                )
                            }

                            TextButton(
                                onClick = { showCustomPhoneField = !showCustomPhoneField },
                                modifier = Modifier.height(28.dp)
                            ) {
                                Text(
                                    text = if (showCustomPhoneField) "Done" else "Custom No.",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = NaijaGreenPrimary
                                    )
                                )
                            }
                        }

                        if (showCustomPhoneField) {
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = recipientPhoneNumber,
                                onValueChange = { viewModel.setRecipientPhoneNumber(it) },
                                label = { Text("Destination Phone Number") },
                                placeholder = { Text("e.g. 08012345678") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_sms_phone_field"),
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Transfer Amount Field
            OutlinedTextField(
                value = transferAmount,
                onValueChange = { viewModel.setTransferAmount(it) },
                label = { Text("Transfer Amount") },
                placeholder = { Text("0.00") },
                prefix = {
                    Text(
                        text = "₦ ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NaijaGreenPrimary
                        )
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("transfer_amount_field"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Quick Amount Chips
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val chips = listOf(
                    Pair("+₦2,000", 2000.0),
                    Pair("+₦5,000", 5000.0),
                    Pair("+₦10,000", 10000.0),
                    Pair("+₦20,000", 20000.0),
                    Pair("+₦50,000", 50000.0),
                    Pair("+₦100,000", 100000.0)
                )
                chips.forEach { (label, amount) ->
                    SuggestionChip(
                        onClick = { viewModel.addQuickAmount(amount) },
                        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Narration Field
            OutlinedTextField(
                value = narration,
                onValueChange = { viewModel.setNarration(it) },
                label = { Text("Narration / Description") },
                placeholder = { Text("e.g. Rent payment, Food, Upkeep") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("narration_field"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Quick Narration Tags
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val tags = listOf("Rent", "Food & Chops", "Upkeep", "Refund", "Business Supply", "Family Support", "Emergency")
                items(tags) { tag ->
                    SuggestionChip(
                        onClick = { viewModel.setNarration(tag) },
                        label = { Text(tag, style = MaterialTheme.typography.labelSmall) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SMS Alert Generation Options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("alert_options_card"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                )
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "SMS Alert to Generate",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val options = listOf(
                            Triple("DEBIT", "Debit Alert (${userBank.smsSenderId})", AlertDebitRed),
                            Triple("CREDIT", "Credit Alert (${recipientBank.smsSenderId})", AlertCreditGreen)
                        )

                        options.forEach { (type, label, accentColor) ->
                            val isSelected = alertType == type
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) accentColor.copy(alpha = 0.15f)
                                        else MaterialTheme.colorScheme.surface
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) accentColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable { viewModel.setAlertType(type) }
                                    .padding(vertical = 10.dp, horizontal = 8.dp)
                                    .testTag("alert_option_$type"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Action: Send & Generate SMS Alert
            Button(
                onClick = {
                    if (autoSendSms && !SmsDispatcher.hasSmsPermission(context)) {
                        smsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
                    }
                    viewModel.executeTransfer {
                        // After success, return to dashboard to see saved transaction in history
                        isInputtingDetails = false
                    }
                },
                enabled = !isProcessingTransfer,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("send_transfer_btn"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NaijaGreenPrimary
                )
            ) {
                if (isProcessingTransfer) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Processing Transfer & Alert...",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Transfer & Generate SMS Alert",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Cancel / Go Back to Dashboard Button
            OutlinedButton(
                onClick = { isInputtingDetails = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("cancel_input_details_btn"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel and Back to Dashboard")
            }
        }

        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun OPayServiceItem(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    testTag: String? = null
) {
    val baseModifier = Modifier
        .width(72.dp)
        .clip(RoundedCornerShape(12.dp))
        .clickable { onClick() }
        .padding(vertical = 4.dp)
    val finalModifier = if (testTag != null) baseModifier.testTag(testTag) else baseModifier

    Column(
        modifier = finalModifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(iconBgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}


