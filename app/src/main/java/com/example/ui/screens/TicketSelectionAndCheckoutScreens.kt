package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Campaign
import com.example.model.DrawTicket
import com.example.model.TicketStatus
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.DeeporamTopBar
import com.example.ui.components.GoldGradientButton
import com.example.ui.components.LegalSafetyGateBanner
import com.example.ui.theme.*

@Composable
fun TicketSelectionScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val campaign by viewModel.selectedCampaign.collectAsState()
    val ticketsMap by viewModel.ticketsByCampaign.collectAsState()
    val selectionState by viewModel.ticketSelectionState.collectAsState()

    val tickets = ticketsMap[campaign?.id] ?: emptyList()

    val filteredTickets = remember(tickets, selectionState.statusFilter, selectionState.searchQuery) {
        tickets.filter { t ->
            val matchesFilter = when (selectionState.statusFilter) {
                null -> true
                else -> t.status == selectionState.statusFilter
            }
            val matchesSearch = if (selectionState.searchQuery.isBlank()) {
                true
            } else {
                t.ticketNumber.toString().contains(selectionState.searchQuery.trim())
            }
            matchesFilter && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("ticket_selection_title"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            if (selectionState.selectedTicketNumbers.isNotEmpty()) {
                Surface(
                    color = Navy900,
                    tonalElevation = 8.dp,
                    border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "પસંદ કરેલ: ${selectionState.selectedTicketNumbers.size} ટિકિટ",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Slate300)
                                )
                                Text(
                                    text = "દાન રકમ: ₹${(campaign?.ticketPriceInr ?: 250) * selectionState.selectedTicketNumbers.size}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Gold400
                                    )
                                )
                            }
                            Button(
                                onClick = { viewModel.navigateTo(Screen.CHECKOUT) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gold400,
                                    contentColor = Navy950
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("checkout_button")
                            ) {
                                Text("બુકિંગ આગળ ધપાવો", fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        },
        containerColor = Navy950
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            // Search and Lucky Pick controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = selectionState.searchQuery,
                    onValueChange = { viewModel.setTicketSearchQuery(it) },
                    placeholder = { Text("નંબર શોધો દા.ત. 1042", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Gold400) },
                    trailingIcon = {
                        if (selectionState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setTicketSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = Slate400)
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite,
                        focusedContainerColor = Navy900,
                        unfocusedContainerColor = Navy900
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ticket_search_field")
                )

                // Lucky Pick Button
                Button(
                    onClick = { viewModel.pickLuckyTickets(1) },
                    colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Gold300),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Gold400),
                    modifier = Modifier.testTag("lucky_pick_1_btn")
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("લકી ૧", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectionState.statusFilter == null,
                    onClick = { viewModel.setTicketFilter(null) },
                    label = { Text("બધી") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold400,
                        selectedLabelColor = Navy950,
                        containerColor = Navy900,
                        labelColor = Slate300
                    )
                )
                FilterChip(
                    selected = selectionState.statusFilter == TicketStatus.AVAILABLE,
                    onClick = { viewModel.setTicketFilter(TicketStatus.AVAILABLE) },
                    label = { Text("ઉપલબ્ધ") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold400,
                        selectedLabelColor = Navy950,
                        containerColor = Navy900,
                        labelColor = Slate300
                    )
                )
                FilterChip(
                    selected = selectionState.statusFilter == TicketStatus.RESERVED,
                    onClick = { viewModel.setTicketFilter(TicketStatus.RESERVED) },
                    label = { Text("રિઝર્વ") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold400,
                        selectedLabelColor = Navy950,
                        containerColor = Navy900,
                        labelColor = Slate300
                    )
                )
                FilterChip(
                    selected = selectionState.statusFilter == TicketStatus.BOOKED,
                    onClick = { viewModel.setTicketFilter(TicketStatus.BOOKED) },
                    label = { Text("બુક થયેલ") },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Gold400,
                        selectedLabelColor = Navy950,
                        containerColor = Navy900,
                        labelColor = Slate300
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LegendIndicator(color = Navy800, label = "ઉપલબ્ધ")
                LegendIndicator(color = Gold400, label = "પસંદ કરેલ")
                LegendIndicator(color = WarningOrange, label = "હોલ્ડ (રિઝર્વ)")
                LegendIndicator(color = Slate500, label = "બુક થયેલ")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Ticket Numbers Grid (5 columns)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("tickets_grid")
            ) {
                items(filteredTickets) { ticket ->
                    val isSelected = selectionState.selectedTicketNumbers.contains(ticket.ticketNumber)
                    TicketItemGridCell(
                        ticket = ticket,
                        isSelected = isSelected,
                        onToggle = { viewModel.toggleTicketSelection(ticket.ticketNumber) }
                    )
                }
            }
        }
    }
}

@Composable
fun LegendIndicator(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall.copy(color = Slate400, fontSize = 11.sp))
    }
}

@Composable
fun TicketItemGridCell(
    ticket: DrawTicket,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    val isAvailable = ticket.status == TicketStatus.AVAILABLE || isSelected
    val containerColor = when {
        isSelected -> Gold400
        ticket.status == TicketStatus.RESERVED -> Navy800
        ticket.status == TicketStatus.BOOKED -> Navy900.copy(alpha = 0.5f)
        else -> Navy900
    }

    val textColor = when {
        isSelected -> Navy950
        ticket.status == TicketStatus.BOOKED -> Slate500
        ticket.status == TicketStatus.RESERVED -> WarningOrange
        else -> PureWhite
    }

    val borderColor = when {
        isSelected -> Gold300
        ticket.status == TicketStatus.RESERVED -> WarningOrange
        ticket.status == TicketStatus.BOOKED -> Navy700
        else -> CardBorderNavy
    }

    Surface(
        onClick = { if (ticket.status == TicketStatus.AVAILABLE || isSelected) onToggle() },
        enabled = ticket.status == TicketStatus.AVAILABLE || isSelected,
        shape = RoundedCornerShape(10.dp),
        color = containerColor,
        border = BorderStroke(1.2.dp, borderColor),
        modifier = Modifier
            .height(56.dp)
            .testTag("ticket_cell_${ticket.ticketNumber}")
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "#${ticket.ticketNumber}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = textColor,
                        fontSize = 14.sp
                    )
                )
                if (ticket.status == TicketStatus.BOOKED) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Booked",
                        tint = Slate500,
                        modifier = Modifier.size(12.dp)
                    )
                } else if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Navy950,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BookingCheckoutScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.TICKET_SELECTION)
    }

    val campaign by viewModel.selectedCampaign.collectAsState()
    val selectionState by viewModel.ticketSelectionState.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val termsAccepted by viewModel.termsAccepted.collectAsState()

    val totalAmount = (campaign?.ticketPriceInr ?: 250) * selectionState.selectedTicketNumbers.size
    var selectedPaymentMode by remember { mutableStateOf("UPI_DEMO") }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("booking_summary"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.TICKET_SELECTION) }
            )
        },
        bottomBar = {
            Surface(
                color = Navy900,
                tonalElevation = 8.dp,
                border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp)
                ) {
                    GoldGradientButton(
                        text = "દાન ચુકવણી પૂર્ણ કરો અને ટિકિટ કન્ફર્મ કરો (₹$totalAmount)",
                        onClick = { viewModel.completeBooking(selectedPaymentMode) },
                        enabled = termsAccepted,
                        icon = Icons.Default.CheckCircle,
                        testTag = "confirm_payment_btn"
                    )
                }
            }
        },
        containerColor = Navy950
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Campaign and Ticket summary
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Navy900,
                border = BorderStroke(1.dp, CardBorderNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = campaign?.titleGu ?: "દીપોરામ ચેરિટી ડ્રો",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ટ્રસ્ટ રજિસ્ટ્રેશન: ${campaign?.statutoryTrustRegNo}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Gold400)
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Navy700)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("પસંદ કરેલી ટિકિટો:", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        selectionState.selectedTicketNumbers.forEach { num ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Navy800,
                                border = BorderStroke(1.dp, Gold400)
                            ) {
                                Text(
                                    text = "#$num",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Gold300
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("દાન રકમ (₹${campaign?.ticketPriceInr} x ${selectionState.selectedTicketNumbers.size}):", color = Slate300)
                        Text("₹$totalAmount", fontWeight = FontWeight.Bold, color = Gold400)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• કલમ 80G હેઠળ ૫૦% કર કપાત પાત્ર રહેશે.",
                        style = MaterialTheme.typography.labelSmall.copy(color = StatusSuccess)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Official Contribution & Payment Mode Selection
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Navy900,
                border = BorderStroke(1.2.dp, Gold400.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Gold400)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "સુરક્ષિત ચુકવણી પદ્ધતિ (Payment Options)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold300
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    val options = listOf(
                        Triple("UPI", "UPI (Google Pay, PhonePe, BHIM, Paytm)", Icons.Default.QrCodeScanner),
                        Triple("NET_BANKING", "નેટ બેન્કિંગ (SBI, HDFC, Bank of Baroda, ICICI)", Icons.Default.AccountBalance),
                        Triple("CARD", "ડેબિટ / ક્રેડિટ કાર્ડ (RuPay, Visa, Mastercard)", Icons.Default.CreditCard)
                    )

                    options.forEach { (mode, label, icon) ->
                        val isSelected = selectedPaymentMode == mode
                        Surface(
                            onClick = { selectedPaymentMode = mode },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) Navy800 else Navy850,
                            border = BorderStroke(1.dp, if (isSelected) Gold400 else Navy700),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = { selectedPaymentMode = mode },
                                    colors = RadioButtonDefaults.colors(selectedColor = Gold400, unselectedColor = Slate400)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(icon, contentDescription = null, tint = if (isSelected) Gold400 else Slate400, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = if (isSelected) PureWhite else Slate300,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Donor Information Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Navy900,
                border = BorderStroke(1.dp, Navy700),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "દાતાની વિગતો (Donor KYC):",
                        style = MaterialTheme.typography.labelMedium.copy(color = Gold400, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("નામ: ${currentUser?.name}", color = PureWhite, style = MaterialTheme.typography.bodyMedium)
                    Text("મોબાઇલ: ${currentUser?.phone}", color = Slate300, style = MaterialTheme.typography.bodySmall)
                    Text("રાજ્ય: ${currentUser?.state} (માન્ય પાત્ર ક્ષેત્ર)", color = Slate300, style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Terms Checkbox
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setTermsAccepted(!termsAccepted) },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { viewModel.setTermsAccepted(it) },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Gold400,
                        checkmarkColor = Navy950,
                        uncheckedColor = Slate400
                    ),
                    modifier = Modifier.testTag("accept_terms_checkbox")
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "હું પ્રમાણિત કરું છું કે મારી ઉંમર ૧૮+ વર્ષ છે, હું ભારતીય નાગરિક છું અને ચેરિટેબલ ટ્રસ્ટના તમામ નિયમો તથા ડ્રો શરતો સ્વીકારું છું.",
                    style = MaterialTheme.typography.bodySmall.copy(color = PureWhite, lineHeight = 18.sp)
                )
            }
        }
    }
}
