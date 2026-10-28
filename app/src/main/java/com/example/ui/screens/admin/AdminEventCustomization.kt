package com.example.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.*
import com.example.ui.DeeporamViewModel
import com.example.ui.theme.*
import java.util.UUID

@Composable
fun AdminEventCustomization(viewModel: DeeporamViewModel) {
    val campaigns by viewModel.campaigns.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var campaignToEdit by remember { mutableStateOf<Campaign?>(null) }
    var campaignToDelete by remember { mutableStateOf<Campaign?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilterStatus by remember { mutableStateOf<CampaignStatus?>(null) }

    val filteredCampaigns = campaigns.filter { camp ->
        (selectedFilterStatus == null || camp.status == selectedFilterStatus) &&
                (searchQuery.isBlank() ||
                        camp.titleGu.contains(searchQuery, ignoreCase = true) ||
                        camp.title.contains(searchQuery, ignoreCase = true) ||
                        camp.code.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header & Add Event Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ઇવેન્ટ્સ કસ્ટમાઇઝેશન (Event Manager)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold400
                    )
                )
                Text(
                    text = "ચેરિટી ડ્રો ઉમેરો, ટિકિટ ભાવ, ઇનામો અને સ્થિતિ કસ્ટમાઇઝ કરો",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp)
                )
            }

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("add_new_event_btn")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("નવી ઇવેન્ટ", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search & Filter
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("ઇવેન્ટ અથવા કોડ શોધો...", color = Slate500, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Gold400) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Slate400)
                    }
                }
            },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Gold400,
                unfocusedBorderColor = Navy700,
                focusedTextColor = PureWhite,
                unfocusedTextColor = PureWhite,
                cursorColor = Gold400
            ),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("event_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Status Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedFilterStatus == null,
                onClick = { selectedFilterStatus = null },
                label = { Text("બધા (${campaigns.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Gold400,
                    selectedLabelColor = Navy950,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedFilterStatus == CampaignStatus.ACTIVE,
                onClick = { selectedFilterStatus = if (selectedFilterStatus == CampaignStatus.ACTIVE) null else CampaignStatus.ACTIVE },
                label = { Text("ACTIVE", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = StatusSuccess,
                    selectedLabelColor = PureWhite,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedFilterStatus == CampaignStatus.PAUSED,
                onClick = { selectedFilterStatus = if (selectedFilterStatus == CampaignStatus.PAUSED) null else CampaignStatus.PAUSED },
                label = { Text("PAUSED", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = WarningOrange,
                    selectedLabelColor = PureWhite,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedFilterStatus == CampaignStatus.CLOSED,
                onClick = { selectedFilterStatus = if (selectedFilterStatus == CampaignStatus.CLOSED) null else CampaignStatus.CLOSED },
                label = { Text("CLOSED", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Slate500,
                    selectedLabelColor = PureWhite,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Events List
        if (filteredCampaigns.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("કોઈ ઇવેન્ટ મળી નથી / No Events Found", color = Slate400, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredCampaigns, key = { it.id }) { camp ->
                    AdminEventCard(
                        campaign = camp,
                        onEdit = { campaignToEdit = camp },
                        onDelete = { campaignToDelete = camp },
                        onStatusChange = { newStatus ->
                            viewModel.updateCampaignStatus(camp.id, newStatus)
                        },
                        onTriggerDraw = {
                            viewModel.triggerLiveDrawSimulation(camp.id)
                        }
                    )
                }
            }
        }
    }

    // Add New Event Dialog
    if (showAddDialog) {
        AddEditEventDialog(
            existingCampaign = null,
            onDismiss = { showAddDialog = false },
            onSave = { newCamp ->
                viewModel.addCampaign(newCamp)
                showAddDialog = false
            }
        )
    }

    // Edit Existing Event Dialog
    campaignToEdit?.let { camp ->
        AddEditEventDialog(
            existingCampaign = camp,
            onDismiss = { campaignToEdit = null },
            onSave = { updatedCamp ->
                viewModel.updateCampaign(updatedCamp)
                campaignToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    campaignToDelete?.let { camp ->
        AlertDialog(
            onDismissRequest = { campaignToDelete = null },
            title = {
                Text("ઇવેન્ટ ડિલીટ કરવાની પુષ્ટિ", color = StatusDanger, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "શું તમે ખરેખર '${camp.titleGu}' (${camp.code}) ઇવેન્ટને આર્કાઇવ/ડિલીટ કરવા માંગો છો? આ ક્રિયા ઓડિટ ટ્રેઇલમાં નોંધાશે.",
                    color = Slate300,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteCampaign(camp.id)
                        campaignToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger, contentColor = PureWhite)
                ) {
                    Text("હા, ડિલીટ કરો", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { campaignToDelete = null }) {
                    Text("રદ કરો", color = Slate400)
                }
            },
            containerColor = Navy900
        )
    }
}

@Composable
fun AdminEventCard(
    campaign: Campaign,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onStatusChange: (CampaignStatus) -> Unit,
    onTriggerDraw: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Navy900,
        border = BorderStroke(1.dp, CardBorderNavy),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = campaign.titleGu,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Text(
                        text = "${campaign.code} • ${campaign.title}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (campaign.status) {
                        CampaignStatus.ACTIVE -> StatusSuccess.copy(alpha = 0.2f)
                        CampaignStatus.APPROVED -> Gold500.copy(alpha = 0.2f)
                        CampaignStatus.PAUSED -> WarningOrange.copy(alpha = 0.2f)
                        CampaignStatus.CLOSED -> Slate500.copy(alpha = 0.2f)
                        else -> Slate700.copy(alpha = 0.4f)
                    },
                    border = BorderStroke(
                        1.dp,
                        when (campaign.status) {
                            CampaignStatus.ACTIVE -> StatusSuccess
                            CampaignStatus.APPROVED -> Gold500
                            CampaignStatus.PAUSED -> WarningOrange
                            CampaignStatus.CLOSED -> Slate500
                            else -> Slate700
                        }
                    )
                ) {
                    Text(
                        text = campaign.status.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Pricing & Capacity details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Navy800,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("ટિકિટ સેવા દાન", color = Slate400, fontSize = 10.sp)
                        Text("₹${campaign.ticketPriceInr}", color = Gold300, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Navy800,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("બુક / કુલ ટિકિટો", color = Slate400, fontSize = 10.sp)
                        Text("${campaign.soldTickets}/${campaign.totalTickets}", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Navy800,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("ડ્રો તારીખ", color = Slate400, fontSize = 10.sp)
                        Text(campaign.drawDate, color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grand Prize line
            Text(
                text = "મુખ્ય ઇનામ: ${campaign.grandPrizeNameGu}",
                style = MaterialTheme.typography.bodySmall.copy(color = Gold400, fontSize = 11.sp)
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Navy700)
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit / Customize Button
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Gold400),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Gold400),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("કસ્ટમાઇઝ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Status Toggle Button
                if (campaign.status != CampaignStatus.ACTIVE && campaign.status != CampaignStatus.CLOSED) {
                    OutlinedButton(
                        onClick = { onStatusChange(CampaignStatus.ACTIVE) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusSuccess),
                        border = BorderStroke(1.dp, StatusSuccess),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("ACTIVE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                } else if (campaign.status == CampaignStatus.ACTIVE) {
                    OutlinedButton(
                        onClick = { onStatusChange(CampaignStatus.PAUSED) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningOrange),
                        border = BorderStroke(1.dp, WarningOrange),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("PAUSE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (campaign.status == CampaignStatus.ACTIVE || campaign.status == CampaignStatus.PAUSED) {
                    OutlinedButton(
                        onClick = onTriggerDraw,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold300),
                        border = BorderStroke(1.dp, Gold400),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("DRAW", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Event", tint = StatusDanger)
                }
            }
        }
    }
}

@Composable
fun AddEditEventDialog(
    existingCampaign: Campaign?,
    onDismiss: () -> Unit,
    onSave: (Campaign) -> Unit
) {
    val isEdit = existingCampaign != null

    var code by remember { mutableStateOf(existingCampaign?.code ?: "DF-NEW-${(100..999).random()}") }
    var titleGu by remember { mutableStateOf(existingCampaign?.titleGu ?: "") }
    var titleEn by remember { mutableStateOf(existingCampaign?.title ?: "") }
    var ticketPriceStr by remember { mutableStateOf(existingCampaign?.ticketPriceInr?.toString() ?: "100") }
    var totalTicketsStr by remember { mutableStateOf(existingCampaign?.totalTickets?.toString() ?: "100") }
    var drawDate by remember { mutableStateOf(existingCampaign?.drawDate ?: "31 Dec 2026") }
    var grandPrizeGu by remember { mutableStateOf(existingCampaign?.grandPrizeNameGu ?: "") }
    var causeDescGu by remember { mutableStateOf(existingCampaign?.causeDescriptionGu ?: "") }
    var selectedStatus by remember { mutableStateOf(existingCampaign?.status ?: CampaignStatus.ACTIVE) }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEdit) "ઇવેન્ટ કસ્ટમાઇઝેશન (Edit Event)" else "નવી ઇવેન્ટ ઉમેરો (Add New Event)",
                color = Gold400,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Campaign Code
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text("ઇવેન્ટ કોડ (Event Code)", fontSize = 11.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Title Gujarati
                OutlinedTextField(
                    value = titleGu,
                    onValueChange = { titleGu = it },
                    label = { Text("ઇવેન્ટ શીર્ષક - ગુજરાતી (Title Gujarati)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. દીપોરામ શિયાળુ સેવા ડ્રો", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Title English
                OutlinedTextField(
                    value = titleEn,
                    onValueChange = { titleEn = it },
                    label = { Text("ઇવેન્ટ શીર્ષક - English (Title English)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. Deeporam Winter Relief Draw", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Ticket Price & Capacity
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = ticketPriceStr,
                        onValueChange = { ticketPriceStr = it.filter { char -> char.isDigit() } },
                        label = { Text("ટિકિટ દાન ₹", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gold400,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = totalTicketsStr,
                        onValueChange = { totalTicketsStr = it.filter { char -> char.isDigit() } },
                        label = { Text("કુલ ટિકિટ ક્ષમતા", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gold400,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Draw Date
                OutlinedTextField(
                    value = drawDate,
                    onValueChange = { drawDate = it },
                    label = { Text("ડ્રો તારીખ (Draw Date)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. 15 Jan 2027", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Grand Prize
                OutlinedTextField(
                    value = grandPrizeGu,
                    onValueChange = { grandPrizeGu = it },
                    label = { Text("મુખ્ય ઇનામ (Grand Prize)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. 50g સોનાનો સિક્કો અથવા ઇલેક્ટ્રિક બાઇક", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Cause Description
                OutlinedTextField(
                    value = causeDescGu,
                    onValueChange = { causeDescGu = it },
                    label = { Text("સેવા હેતુ વર્ણન (Charitable Cause)", fontSize = 11.sp) },
                    placeholder = { Text("આ ડ્રોનું સમગ્ર દાન કયા સેવા કાર્યમાં વપરાશે...", color = Slate500) },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Status Selector
                Text("ઇવેન્ટ સ્થિતિ (Event Status):", color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(CampaignStatus.DRAFT, CampaignStatus.APPROVED, CampaignStatus.ACTIVE, CampaignStatus.PAUSED).forEach { st ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (selectedStatus == st) Gold400 else Navy800,
                            border = BorderStroke(1.dp, if (selectedStatus == st) Gold400 else Navy700),
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            onClick = { selectedStatus = st }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = st.name.take(4),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedStatus == st) Navy950 else Slate300
                                )
                            }
                        }
                    }
                }

                // Error Banner
                AnimatedVisibility(visible = validationError != null) {
                    Text(validationError ?: "", color = StatusDanger, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (titleGu.isBlank()) {
                        validationError = "ગુજરાતી શીર્ષક જરૂરી છે / Title is required"
                        return@Button
                    }
                    val price = ticketPriceStr.toLongOrNull() ?: 100L
                    val total = totalTicketsStr.toIntOrNull() ?: 100

                    val campaign = if (existingCampaign != null) {
                        existingCampaign.copy(
                            code = code.trim(),
                            titleGu = titleGu.trim(),
                            title = if (titleEn.isBlank()) titleGu else titleEn.trim(),
                            ticketPriceInr = price,
                            totalTickets = total,
                            drawDate = drawDate.trim(),
                            grandPrizeNameGu = if (grandPrizeGu.isBlank()) "લકી સુવર્ણ સિક્કો" else grandPrizeGu.trim(),
                            grandPrizeName = if (grandPrizeGu.isBlank()) "Lucky Gold Coin" else grandPrizeGu.trim(),
                            causeDescriptionGu = if (causeDescGu.isBlank()) "આ ડ્રોનું સમગ્ર દાન સામાજિક કલ્યાણ માટે વપરાશે." else causeDescGu.trim(),
                            status = selectedStatus
                        )
                    } else {
                        val newId = "camp_${code.lowercase().replace("-", "_")}_${UUID.randomUUID().toString().take(4)}"
                        Campaign(
                            id = newId,
                            code = code.trim(),
                            title = if (titleEn.isBlank()) titleGu else titleEn.trim(),
                            titleGu = titleGu.trim(),
                            titleHi = titleGu.trim(),
                            causeDescription = if (causeDescGu.isBlank()) "Charitable relief donation drive." else causeDescGu.trim(),
                            causeDescriptionGu = if (causeDescGu.isBlank()) "આ ડ્રોનું સમગ્ર દાન સામાજિક કલ્યાણ માટે વપરાશે." else causeDescGu.trim(),
                            causeDescriptionHi = if (causeDescGu.isBlank()) "दान सेवा कार्य हेतु।" else causeDescGu.trim(),
                            ticketPriceInr = price,
                            totalTickets = total,
                            soldTickets = 0,
                            reservedTickets = 0,
                            drawDate = drawDate.trim(),
                            status = selectedStatus,
                            grandPrizeName = if (grandPrizeGu.isBlank()) "Lucky Gold Coin" else grandPrizeGu.trim(),
                            grandPrizeNameGu = if (grandPrizeGu.isBlank()) "લકી સુવર્ણ સિક્કો" else grandPrizeGu.trim(),
                            grandPrizeNameHi = if (grandPrizeGu.isBlank()) "लकी स्वर्ण सिक्का" else grandPrizeGu.trim(),
                            prizes = listOf(
                                Prize(1, "Grand Prize", grandPrizeGu.ifBlank { "મુખ્ય ઇનામ" }, "मुख्य पुरस्कार", "Official charity prize", price * 200)
                            )
                        )
                    }
                    onSave(campaign)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950)
            ) {
                Text(if (isEdit) "સેવ કરો (Save)" else "ઇવેન્ટ ઉમેરો (Create)", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("રદ કરો (Cancel)", color = Slate400)
            }
        },
        containerColor = Navy900
    )
}
