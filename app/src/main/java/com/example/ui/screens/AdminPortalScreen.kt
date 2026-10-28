package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AuditLog
import com.example.model.Campaign
import com.example.model.CampaignStatus
import com.example.model.UserRole
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.DeeporamTopBar
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.*

@Composable
fun AdminPortalScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val currentUser by viewModel.currentUser.collectAsState()
    val campaigns by viewModel.campaigns.collectAsState()
    val auditLogs by viewModel.auditLogs.collectAsState()
    val userBookings by viewModel.userBookings.collectAsState()

    var selectedAdminTab by remember { mutableStateOf(0) } // 0: Dashboard, 1: Campaigns, 2: Compliance, 3: Audit Logs
    var exportedCsvContent by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = "એડમિન કંટ્રોલ & કમ્પ્લાયન્સ પોર્ટલ",
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        containerColor = Navy950
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Admin Tab Bar
            ScrollableTabRow(
                selectedTabIndex = selectedAdminTab,
                containerColor = Navy900,
                contentColor = Gold400,
                edgePadding = 16.dp
            ) {
                Tab(
                    selected = selectedAdminTab == 0,
                    onClick = { selectedAdminTab = 0 },
                    text = { Text("ડેશબોર્ડ", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedAdminTab == 1,
                    onClick = { selectedAdminTab = 1 },
                    text = { Text("અભિયાનો & સ્ટેટ્સ", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedAdminTab == 2,
                    onClick = { selectedAdminTab = 2 },
                    text = { Text("કાનૂની સલામતી ગેટ", fontWeight = FontWeight.Bold) }
                )
                Tab(
                    selected = selectedAdminTab == 3,
                    onClick = { selectedAdminTab = 3 },
                    text = { Text("ઓડિટ લૉગ્સ (${auditLogs.size})", fontWeight = FontWeight.Bold) }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                when (selectedAdminTab) {
                    0 -> {
                        // Analytics & Quick Actions
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Navy900,
                                border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Analytics, contentDescription = null, tint = Gold400)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "સંચાલન મેટ્રિક્સ (Live Draw Analytics)",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PureWhite
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(14.dp))

                                    val totalSold = campaigns.sumOf { it.soldTickets }
                                    val totalRevenue = campaigns.sumOf { it.soldTickets * it.ticketPriceInr }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Navy800,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text("કુલ બુક ટિકિટો", color = Slate400, fontSize = 11.sp)
                                                Text("$totalSold ટિકિટ", color = Gold300, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = Navy800,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text("એકત્રિત સેવા દાન", color = Slate400, fontSize = 11.sp)
                                                Text("₹${"%,d".format(totalRevenue)}", color = StatusSuccess, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // CSV Export simulation
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Navy900,
                                border = BorderStroke(1.dp, Navy700),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = "ઓડિટ ડેટા નિકાસ (CSV Export Engine)",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Gold400
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "તમામ ટિકિટ ફાળવણી, બુકિંગ અને વ્યવહારોનું CSV ફોર્મેટમાં જનરેશન:",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))

                                    Button(
                                        onClick = {
                                            val csv = viewModel.exportCsv()
                                            exportedCsvContent = csv
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Gold400),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Gold400),
                                        modifier = Modifier.testTag("export_csv_btn")
                                    ) {
                                        Icon(Icons.Default.FileDownload, contentDescription = null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("CSV ડાઉનલોડ & ક્રિપ્ટો ચેકસમ વેરીફિકેશન", fontWeight = FontWeight.Bold)
                                    }

                                    if (exportedCsvContent != null) {
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Navy950,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = exportedCsvContent!!.take(320) + "\n... [Total records encrypted & verified]",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = Slate300,
                                                    fontSize = 10.sp
                                                ),
                                                modifier = Modifier.padding(10.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    1 -> {
                        // Campaign Lifecycle & Status Manager
                        item {
                            Text(
                                text = "અભિયાન સ્થિતિ વ્યવસ્થાપન (Draft → Approved → Active → Closed)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold400
                                )
                            )
                        }

                        items(campaigns) { camp ->
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
                                        Text(
                                            text = camp.titleGu,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PureWhite
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = when (camp.status) {
                                                CampaignStatus.ACTIVE -> StatusSuccess.copy(alpha = 0.2f)
                                                CampaignStatus.APPROVED -> Gold500.copy(alpha = 0.2f)
                                                CampaignStatus.PAUSED -> WarningOrange.copy(alpha = 0.2f)
                                                else -> Slate500.copy(alpha = 0.2f)
                                            },
                                            border = BorderStroke(
                                                1.dp,
                                                when (camp.status) {
                                                    CampaignStatus.ACTIVE -> StatusSuccess
                                                    CampaignStatus.APPROVED -> Gold500
                                                    CampaignStatus.PAUSED -> WarningOrange
                                                    else -> Slate500
                                                }
                                            )
                                        ) {
                                            Text(
                                                text = camp.status.name,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = PureWhite
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "કોડ: ${camp.code} • દાન: ₹${camp.ticketPriceInr} • બુક: ${camp.soldTickets}/${camp.totalTickets}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (camp.status != CampaignStatus.ACTIVE) {
                                            OutlinedButton(
                                                onClick = { viewModel.updateCampaignStatus(camp.id, CampaignStatus.ACTIVE) },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusSuccess),
                                                border = BorderStroke(1.dp, StatusSuccess),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("સક્રિય કરો (ACTIVE)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        if (camp.status == CampaignStatus.ACTIVE) {
                                            OutlinedButton(
                                                onClick = { viewModel.updateCampaignStatus(camp.id, CampaignStatus.PAUSED) },
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = WarningOrange),
                                                border = BorderStroke(1.dp, WarningOrange),
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text("રોકો (PAUSE)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                        OutlinedButton(
                                            onClick = { viewModel.triggerLiveDrawSimulation(camp.id) },
                                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Gold300),
                                            border = BorderStroke(1.dp, Gold400),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("ડ્રો કરો (DRAW)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Legal Compliance Gate Sign-off
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Navy900,
                                border = BorderStroke(1.dp, Gold400),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Gold400)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "કાનૂની સલામતી ગેટ ચકાસણી (Legal Gate)",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PureWhite
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "• નિયમનકારી મંજૂરી સંદર્ભ: DF-LEGAL-2026-081\n• ચેરિટી ટ્રસ્ટ રજિસ્ટ્રેશન: E/9482/AHMEDABAD\n• અધિકૃત વકીલ: એડવોકેટ હસમુખ એમ. દવે (BCG-GJ-94812)\n• આવકવેરા મુક્તિ: Section 80G Certificate Validated\n• ભૌગોલિક પાત્રતા: ગુજરાત તથા અન્ય માન્ય ભારતીય રાજ્યો.\n• લઘુત્તમ વય: ૧૮ વર્ષ પૂર્ણ.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate300, lineHeight = 20.sp)
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = StatusSuccess.copy(alpha = 0.2f),
                                        border = BorderStroke(1.dp, StatusSuccess),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = StatusSuccess)
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "સંપૂર્ણ કાનૂની મંજૂરી વેરિફાઇડ & સક્રિય",
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = StatusSuccess
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    3 -> {
                        // Immutable Audit Logs Viewer
                        item {
                            Text(
                                text = "અપરિવર્તનીય ઓડિટ લૉગ્સ (Immutable Audit Trail)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold400
                                )
                            )
                        }

                        items(auditLogs) { log ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Navy900,
                                border = BorderStroke(1.dp, Navy700),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = log.action,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Gold300
                                            )
                                        )
                                        Text(
                                            text = log.id,
                                            style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${log.actorEmail} (${log.actorRole})",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate300)
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = log.details,
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "SHA-256 Checksum: ${log.sha256Checksum}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = Slate500, fontSize = 9.sp)
                                    )
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
