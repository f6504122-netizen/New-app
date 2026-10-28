package com.example.ui.screens.admin

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
fun AdminAccountCustomization(viewModel: DeeporamViewModel) {
    val allAccounts by viewModel.allAccounts.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var accountToEdit by remember { mutableStateOf<AppUser?>(null) }
    var accountToDelete by remember { mutableStateOf<AppUser?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedRoleFilter by remember { mutableStateOf<UserRole?>(null) }

    val filteredAccounts = allAccounts.filter { acc ->
        (selectedRoleFilter == null || acc.role == selectedRoleFilter) &&
                (searchQuery.isBlank() ||
                        acc.name.contains(searchQuery, ignoreCase = true) ||
                        acc.phone.contains(searchQuery, ignoreCase = true) ||
                        acc.email.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header & Add Account Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "એકાઉન્ટ્સ કસ્ટમાઇઝેશન (Accounts Manager)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold400
                    )
                )
                Text(
                    text = "દાતા, ટ્રસ્ટી, એડમિન અને કમ્પ્લાયન્સ ખાતાઓનું સંચાલન કરો",
                    style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp)
                )
            }

            Button(
                onClick = { showAddDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("add_new_account_btn")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("નવું ખાતું", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Search Input
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("નામ, ફોન અથવા ઇમેલ દ્વારા શોધો...", color = Slate500, fontSize = 13.sp) },
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
                .testTag("account_search_input")
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Role Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                selected = selectedRoleFilter == null,
                onClick = { selectedRoleFilter = null },
                label = { Text("બધા (${allAccounts.size})", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Gold400,
                    selectedLabelColor = Navy950,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.CUSTOMER,
                onClick = { selectedRoleFilter = if (selectedRoleFilter == UserRole.CUSTOMER) null else UserRole.CUSTOMER },
                label = { Text("દાતા (Donor)", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Navy700,
                    selectedLabelColor = PureWhite,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.ADMIN,
                onClick = { selectedRoleFilter = if (selectedRoleFilter == UserRole.ADMIN) null else UserRole.ADMIN },
                label = { Text("એડમિન", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Gold500,
                    selectedLabelColor = Navy950,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
            FilterChip(
                selected = selectedRoleFilter == UserRole.COMPLIANCE_OFFICER,
                onClick = { selectedRoleFilter = if (selectedRoleFilter == UserRole.COMPLIANCE_OFFICER) null else UserRole.COMPLIANCE_OFFICER },
                label = { Text("ટ્રસ્ટી/કમ્પ્લાયન્સ", fontSize = 11.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Navy600,
                    selectedLabelColor = PureWhite,
                    containerColor = Navy800,
                    labelColor = Slate300
                )
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Accounts List
        if (filteredAccounts.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("કોઈ એકાઉન્ટ મળ્યું નથી / No Accounts Found", color = Slate400, fontSize = 13.sp)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredAccounts, key = { it.id }) { acc ->
                    AdminAccountCard(
                        account = acc,
                        onEdit = { accountToEdit = acc },
                        onDelete = { accountToDelete = acc },
                        onToggleSuspension = {
                            viewModel.toggleAccountSuspension(acc.id)
                        }
                    )
                }
            }
        }
    }

    // Add New Account Dialog
    if (showAddDialog) {
        AddEditAccountDialog(
            existingAccount = null,
            onDismiss = { showAddDialog = false },
            onSave = { newAcc ->
                viewModel.addAccount(newAcc)
                showAddDialog = false
            }
        )
    }

    // Edit Existing Account Dialog
    accountToEdit?.let { acc ->
        AddEditAccountDialog(
            existingAccount = acc,
            onDismiss = { accountToEdit = null },
            onSave = { updatedAcc ->
                viewModel.updateAccount(updatedAcc)
                accountToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    accountToDelete?.let { acc ->
        AlertDialog(
            onDismissRequest = { accountToDelete = null },
            title = {
                Text("ખાતું ડિલીટ કરવાની પુષ્ટિ", color = StatusDanger, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "શું તમે ખરેખર '${acc.name}' (${acc.phone}) નું ખાતું ડિલીટ કરવા માંગો છો? આ ક્રિયા સુરક્ષિત ઓડિટ લૉગમાં રેકોર્ડ થશે.",
                    color = Slate300,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteAccount(acc.id)
                        accountToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusDanger, contentColor = PureWhite)
                ) {
                    Text("હા, ડિલીટ કરો", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { accountToDelete = null }) {
                    Text("રદ કરો", color = Slate400)
                }
            },
            containerColor = Navy900
        )
    }
}

@Composable
fun AdminAccountCard(
    account: AppUser,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleSuspension: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Navy900,
        border = BorderStroke(1.dp, if (account.isSuspended) StatusDanger.copy(alpha = 0.5f) else CardBorderNavy),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Circle
                Surface(
                    shape = CircleShape,
                    color = when (account.role) {
                        UserRole.ADMIN -> Gold500.copy(alpha = 0.2f)
                        UserRole.COMPLIANCE_OFFICER -> Navy600
                        UserRole.AUDITOR -> Slate600
                        else -> Navy800
                    },
                    border = BorderStroke(
                        1.5.dp,
                        when (account.role) {
                            UserRole.ADMIN -> Gold400
                            UserRole.COMPLIANCE_OFFICER -> Gold300
                            else -> Slate600
                        }
                    ),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            when (account.role) {
                                UserRole.ADMIN -> Icons.Default.AdminPanelSettings
                                UserRole.COMPLIANCE_OFFICER -> Icons.Default.Gavel
                                UserRole.AUDITOR -> Icons.Default.Verified
                                else -> Icons.Default.Person
                            },
                            contentDescription = null,
                            tint = if (account.role == UserRole.ADMIN) Gold400 else PureWhite,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = account.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Text(
                        text = "${account.phone} • ${account.email}",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400, fontSize = 11.sp)
                    )
                }

                // Role badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (account.role) {
                        UserRole.ADMIN -> Gold500.copy(alpha = 0.2f)
                        UserRole.COMPLIANCE_OFFICER -> Navy700
                        UserRole.AUDITOR -> Slate700
                        else -> StatusSuccess.copy(alpha = 0.15f)
                    },
                    border = BorderStroke(
                        1.dp,
                        when (account.role) {
                            UserRole.ADMIN -> Gold400
                            UserRole.COMPLIANCE_OFFICER -> Navy600
                            UserRole.AUDITOR -> Slate600
                            else -> StatusSuccess
                        }
                    )
                ) {
                    Text(
                        text = account.role.name,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = when (account.role) {
                                UserRole.ADMIN -> Gold300
                                else -> PureWhite
                            }
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: KYC, Status, Booking Limit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // KYC status
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (account.isKycVerified) StatusSuccess.copy(alpha = 0.15f) else WarningOrange.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (account.isKycVerified) "✓ KYC VERIFIED" else "KYC PENDING",
                        color = if (account.isKycVerified) StatusSuccess else WarningOrange,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Account active/suspended
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (account.isSuspended) StatusDanger.copy(alpha = 0.2f) else StatusSuccess.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (account.isSuspended) "SUSPENDED" else "ACTIVE",
                        color = if (account.isSuspended) StatusDanger else StatusSuccess,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Ticket limit
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Navy800
                ) {
                    Text(
                        text = "મહત્તમ ટિકિટ: ${account.maxTicketLimit}",
                        color = Slate300,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Navy700)
            Spacer(modifier = Modifier.height(10.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Edit Account Button
                Button(
                    onClick = onEdit,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Gold400),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, Gold400),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("કસ્ટમાઇઝ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Suspend / Activate Toggle
                OutlinedButton(
                    onClick = onToggleSuspension,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = if (account.isSuspended) StatusSuccess else WarningOrange
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (account.isSuspended) StatusSuccess else WarningOrange
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (account.isSuspended) "સક્રિય કરો" else "સસ્પેન્ડ કરો",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Account", tint = StatusDanger)
                }
            }
        }
    }
}

@Composable
fun AddEditAccountDialog(
    existingAccount: AppUser?,
    onDismiss: () -> Unit,
    onSave: (AppUser) -> Unit
) {
    val isEdit = existingAccount != null

    var name by remember { mutableStateOf(existingAccount?.name ?: "") }
    var phone by remember { mutableStateOf(existingAccount?.phone ?: "+91 ") }
    var email by remember { mutableStateOf(existingAccount?.email ?: "") }
    var location by remember { mutableStateOf(existingAccount?.state ?: "Gujarat") }
    var selectedRole by remember { mutableStateOf(existingAccount?.role ?: UserRole.CUSTOMER) }
    var isKycVerified by remember { mutableStateOf(existingAccount?.isKycVerified ?: true) }
    var isSuspended by remember { mutableStateOf(existingAccount?.isSuspended ?: false) }
    var maxTicketLimitStr by remember { mutableStateOf(existingAccount?.maxTicketLimit?.toString() ?: "10") }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (isEdit) "એકાઉન્ટ કસ્ટમાઇઝેશન (Edit Account)" else "નવું એકાઉન્ટ ઉમેરો (Add Account)",
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
                // Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("સંપૂર્ણ નામ (Full Name)", fontSize = 11.sp) },
                    placeholder = { Text("e.g. નવીનભાઈ મહેતા", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Phone Field
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("મોબાઇલ નંબર (Mobile Phone)", fontSize = 11.sp) },
                    placeholder = { Text("+91 98765 00000", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("ઈમેલ સરનામું (Email)", fontSize = 11.sp) },
                    placeholder = { Text("user@example.com", color = Slate500) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Location
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("શહેર / રાજ્ય (City / State)", fontSize = 11.sp) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Max Ticket Booking Limit
                OutlinedTextField(
                    value = maxTicketLimitStr,
                    onValueChange = { maxTicketLimitStr = it.filter { ch -> ch.isDigit() } },
                    label = { Text("મહત્તમ ટિકિટ મર્યાદા (Max Ticket Limit)", fontSize = 11.sp) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Role Selector
                Text("એકાઉન્ટ ભૂમિકા (Account Role):", color = PureWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf(UserRole.CUSTOMER, UserRole.ADMIN, UserRole.COMPLIANCE_OFFICER, UserRole.AUDITOR).forEach { role ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (selectedRole == role) Gold400 else Navy800,
                            border = BorderStroke(1.dp, if (selectedRole == role) Gold400 else Navy700),
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            onClick = { selectedRole = role }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = when (role) {
                                        UserRole.CUSTOMER -> "દાતા"
                                        UserRole.ADMIN -> "એડમિન"
                                        UserRole.COMPLIANCE_OFFICER -> "ટ્રસ્ટી"
                                        UserRole.AUDITOR -> "ઓડિટર"
                                    },
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selectedRole == role) Navy950 else Slate300
                                )
                            }
                        }
                    }
                }

                // Toggles
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("KYC વેરિફિકેશન (KYC Verified)", color = PureWhite, fontSize = 12.sp)
                    Switch(
                        checked = isKycVerified,
                        onCheckedChange = { isKycVerified = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Gold400, checkedTrackColor = Navy800)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("એકાઉન્ટ સસ્પેન્ડ (Suspend Account)", color = PureWhite, fontSize = 12.sp)
                    Switch(
                        checked = isSuspended,
                        onCheckedChange = { isSuspended = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = StatusDanger, checkedTrackColor = Navy800)
                    )
                }

                AnimatedVisibility(visible = validationError != null) {
                    Text(validationError ?: "", color = StatusDanger, fontSize = 11.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isBlank()) {
                        validationError = "નામ દાખલ કરવું જરૂરી છે / Name is required"
                        return@Button
                    }
                    val limit = maxTicketLimitStr.toIntOrNull() ?: 10

                    val user = if (existingAccount != null) {
                        existingAccount.copy(
                            name = name.trim(),
                            phone = phone.trim(),
                            email = if (email.isBlank()) "user.${phone.takeLast(4)}@deeporam.org" else email.trim(),
                            state = location.trim(),
                            role = selectedRole,
                            isKycVerified = isKycVerified,
                            isSuspended = isSuspended,
                            maxTicketLimit = limit
                        )
                    } else {
                        val newId = "usr_acc_${UUID.randomUUID().toString().take(6)}"
                        AppUser(
                            id = newId,
                            name = name.trim(),
                            phone = phone.trim(),
                            email = if (email.isBlank()) "user.${phone.takeLast(4)}@deeporam.org" else email.trim(),
                            state = location.trim(),
                            role = selectedRole,
                            isKycVerified = isKycVerified,
                            isSuspended = isSuspended,
                            maxTicketLimit = limit
                        )
                    }
                    onSave(user)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950)
            ) {
                Text(if (isEdit) "સેવ કરો (Save)" else "ખાતું ઉમેરો (Create)", fontWeight = FontWeight.Bold)
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
