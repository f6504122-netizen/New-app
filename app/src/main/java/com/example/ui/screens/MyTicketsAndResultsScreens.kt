package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.DrawResult
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.DeeporamTopBar
import com.example.ui.components.DigitalTicketPassCard
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.*

@Composable
fun MyTicketsScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val userBookings by viewModel.userBookings.collectAsState()

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("nav_my_tickets"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            BottomNavigationBar(viewModel = viewModel, currentScreen = Screen.MY_TICKETS)
        },
        containerColor = Navy950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .testTag("my_tickets_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "મારી સત્તાવાર ટિકિટો અને દાન રસીદો",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Navy800,
                        border = BorderStroke(1.dp, Gold400)
                    ) {
                        Text(
                            text = "${userBookings.sumOf { it.ticketNumbers.size }} પાસ ફાળવેલ",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold300
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            if (userBookings.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Navy900,
                        border = BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ConfirmationNumber,
                                contentDescription = null,
                                tint = Gold400,
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "હાલમાં તમારી પાસે કોઈ બુક કરેલી ટિકિટ નથી.",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Slate300,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.navigateTo(Screen.HOME) },
                                colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("અભિયાનો જુઓ & ટિકિટ પસંદ કરો", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            } else {
                items(userBookings) { booking ->
                    DigitalTicketPassCard(booking = booking)
                }
            }

            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Navy850,
                    border = BorderStroke(1.dp, Navy700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Gold400)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "કલમ 80G ડિજિટલ કર મુક્તિ પ્રમાણપત્ર",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold300
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "તમારા દાનની અધિકૃત કર મુક્તિ રસીદ આપના રજિસ્ટર્ડ ઇમેઇલ પર મોકલી દેવામાં આવેલ છે. તમામ ડ્રો પરિણામો ૧૦૦% પારદર્શક ઓડિટ હેઠળ જાહેર કરવામાં આવે છે.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DrawResultsScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val drawResults by viewModel.drawResults.collectAsState()
    val verifyState by viewModel.verifyTicketState.collectAsState()
    val liveSimState by viewModel.liveDrawSimulationState.collectAsState()
    val selectedCampaign by viewModel.selectedCampaign.collectAsState()

    var inputTicketNumber by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("official_results"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            BottomNavigationBar(viewModel = viewModel, currentScreen = Screen.DRAW_RESULTS)
        },
        containerColor = Navy950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .testTag("results_screen_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Live Draw CSPRNG Simulator Box
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold500),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Casino, contentDescription = null, tint = Gold400)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "પારદર્શક લાઈવ ડ્રો રેન્ડમાઇઝર (CSPRNG Simulation)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Large spinning number counter display
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Navy950,
                            border = BorderStroke(2.dp, Gold400),
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(64.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = liveSimState.currentNumberDisplay,
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Gold400,
                                        letterSpacing = 4.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                selectedCampaign?.id?.let { viewModel.triggerLiveDrawSimulation(it) }
                            },
                            enabled = !liveSimState.isRunning,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Gold400,
                                contentColor = Navy950
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.testTag("execute_live_draw_btn")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (liveSimState.isRunning) "ડ્રો ચાલી રહ્યો છે..." else "લાઈવ ડ્રો શરૂ કરો (સિમ્યુલેટ)",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Ticket Winner Verification Tool
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, CardBorderNavy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "તમારો ટિકિટ નંબર ચકાસો (શું તમે જીત્યા?)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold400
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "અધિકૃત ડ્રો પરિણામો સાથે તમારો ટિકિટ નંબર મેચ કરવા નંબર દાખલ કરો:",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputTicketNumber,
                                onValueChange = { inputTicketNumber = it },
                                placeholder = { Text("દા.ત. 1042") },
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Gold400,
                                    unfocusedBorderColor = Navy700,
                                    focusedTextColor = PureWhite,
                                    unfocusedTextColor = PureWhite,
                                    focusedContainerColor = Navy850,
                                    unfocusedContainerColor = Navy850
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("verify_ticket_input")
                            )

                            Button(
                                onClick = { viewModel.verifyTicket(inputTicketNumber) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Gold400,
                                    contentColor = Navy950
                                ),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier
                                    .height(54.dp)
                                    .testTag("verify_ticket_submit_btn")
                            ) {
                                Text("ચકાસો", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (verifyState.hasChecked) {
                            Spacer(modifier = Modifier.height(14.dp))
                            if (verifyState.checkedResult != null) {
                                val win = verifyState.checkedResult!!
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = StatusSuccess.copy(alpha = 0.2f),
                                    border = BorderStroke(1.dp, StatusSuccess),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "🎉 અભિનંદન! આ એક વિજેતા ટિકિટ છે!",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = StatusSuccess
                                            )
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "ઇનામ: #${win.rank} - ${win.prizeName}\nવિજેતા રેફરન્સ: ${win.winnerNameMasked}\nઓડિટ હેશ: ${win.verificationHash}",
                                            style = MaterialTheme.typography.bodySmall.copy(color = PureWhite)
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Navy850,
                                    border = BorderStroke(1.dp, Navy700),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "ટિકિટ #${verifyState.queryNumber} વિજેતા સૂચિમાં નથી. આપના સેવા દાન બદલ ફાઉન્ડેશન આપનો આભાર માને છે.",
                                        style = MaterialTheme.typography.bodySmall.copy(color = Slate300),
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Official Results List
            item {
                Text(
                    text = "સત્તાવાર સંપન્ન ડ્રો ઘોષણાઓ (Published Results)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                )
            }

            items(drawResults) { result ->
                DrawResultCard(result = result)
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun DrawResultCard(result: DrawResult) {
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
                    text = result.campaignTitle,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    ),
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StatusSuccess.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, StatusSuccess)
                ) {
                    Text(
                        text = "ઓડિટ પ્રમાણિત",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "તારીખ: ${result.drawDate} • ઓડિટર: ${result.independentAuditorName}",
                style = MaterialTheme.typography.labelSmall.copy(color = Gold300)
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Navy700)
            Spacer(modifier = Modifier.height(10.dp))

            // Winning tickets table
            result.winningTickets.forEach { win ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Navy800,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("#${win.rank}", color = Gold400, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = win.prizeName,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Gold400,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "#${win.ticketNumber}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Navy950
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "SHA-256 Entropy: ${result.entropyProofSha256.take(24)}...",
                style = MaterialTheme.typography.labelSmall.copy(color = Slate500, fontSize = 9.sp)
            )
        }
    }
}
