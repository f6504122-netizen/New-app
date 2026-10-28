package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.model.Campaign
import com.example.model.CampaignStatus
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.DeeporamTopBar
import com.example.ui.components.GoldGradientButton
import com.example.ui.components.LegalSafetyGateBanner
import com.example.ui.theme.*

@Composable
fun HomeScreen(viewModel: DeeporamViewModel) {
    val campaigns by viewModel.campaigns.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val userBookings by viewModel.userBookings.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("app_title")
            )
        },
        bottomBar = {
            BottomNavigationBar(viewModel = viewModel, currentScreen = Screen.HOME)
        },
        containerColor = Navy950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .testTag("home_screen_list"),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
                // Personalized Clean Welcome Banner for New Users
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.2.dp, Gold400),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("new_user_welcome_banner")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Navy800,
                                    border = BorderStroke(1.dp, Gold400),
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.VolunteerActivism,
                                            contentDescription = null,
                                            tint = Gold400,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "નમસ્તે, ${currentUser?.name ?: "દાતા"}!",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PureWhite
                                    )
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = StatusSuccess.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, StatusSuccess)
                            ) {
                                Text(
                                    text = "ID: #${currentUser?.id?.takeLast(5) ?: "DF01"}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = StatusSuccess
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "સરળ ૩ પગલાં: ૧. સેવા અભિયાન પસંદ કરો ➔ ૨. લકી નંબર ચૂંટો ➔ ૩. ડિજિટલ પાસ & 80G કર મુક્તિ રસીદ મેળવો.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate300, lineHeight = 18.sp)
                        )

                        if (userBookings.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.navigateTo(Screen.MY_TICKETS) },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Gold400, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "આપની સ્વાગત ટિકિટ ફાળવાઈ ગઈ છે! ડિજિટલ પાસ જુઓ →",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Gold300, fontWeight = FontWeight.Bold)
                                )
                            }
                        }
                    }
                }
            }

            item {
                // Hero Banner
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painter = painterResource(id = R.drawable.charity_banner),
                            contentDescription = "Charity Festival",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Navy950.copy(alpha = 0.2f), Navy950.copy(alpha = 0.85f))
                                    )
                                )
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(16.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Gold500,
                                modifier = Modifier.padding(bottom = 6.dp)
                            ) {
                                Text(
                                    text = "સત્તાવાર ચેરિટેબલ ડ્રો ૨૦૨૬",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Navy950
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = "દીપોરામ દીપોત્સવ સેવા મહા ડ્રો",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = "પ્રથમ ઇનામ: લક્ઝરી ઇલેક્ટ્રિક SUV • ₹25,00,000",
                                style = MaterialTheme.typography.bodySmall.copy(color = Gold300)
                            )
                        }
                    }
                }
            }

            // Quick Stats Grid
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    StatCard(
                        title = "મારી ટિકિટો",
                        value = "${userBookings.sumOf { it.ticketNumbers.size }} પાસ",
                        icon = Icons.Default.ConfirmationNumber,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "આવકવેરા મુક્તિ",
                        value = "80G માન્ય",
                        icon = Icons.Default.Shield,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "ઓડિટ પરિણામ",
                        value = "૧૦૦% પારદર્શક",
                        icon = Icons.Default.Verified,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Section Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = viewModel.getString("active_campaigns"),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Text(
                        text = "${campaigns.size} સક્રિય",
                        style = MaterialTheme.typography.labelSmall.copy(color = Gold400)
                    )
                }
            }

            // Active Campaign Cards
            items(campaigns) { campaign ->
                CampaignCard(
                    campaign = campaign,
                    currentLang = currentLang,
                    onSelect = {
                        viewModel.selectCampaign(campaign.id)
                        viewModel.navigateTo(Screen.CAMPAIGN_DETAIL)
                    },
                    onPickTickets = {
                        viewModel.selectCampaign(campaign.id)
                        viewModel.navigateTo(Screen.TICKET_SELECTION)
                    }
                )
            }

            item {
                LegalSafetyGateBanner()
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Navy900,
        border = BorderStroke(1.dp, Navy700),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Gold400,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Slate400,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
fun CampaignCard(
    campaign: Campaign,
    currentLang: AppLanguage,
    onSelect: () -> Unit,
    onPickTickets: () -> Unit
) {
    val title = when (currentLang) {
        AppLanguage.GUJARATI -> campaign.titleGu
        AppLanguage.HINDI -> campaign.titleHi
        AppLanguage.ENGLISH -> campaign.title
    }
    val grandPrize = when (currentLang) {
        AppLanguage.GUJARATI -> campaign.grandPrizeNameGu
        AppLanguage.HINDI -> campaign.grandPrizeNameHi
        AppLanguage.ENGLISH -> campaign.grandPrizeName
    }
    val cause = when (currentLang) {
        AppLanguage.GUJARATI -> campaign.causeDescriptionGu
        AppLanguage.HINDI -> campaign.causeDescriptionHi
        AppLanguage.ENGLISH -> campaign.causeDescription
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Navy900,
        border = BorderStroke(1.dp, CardBorderNavy),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .testTag("campaign_card_${campaign.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Navy800,
                    border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = campaign.code,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gold300
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = StatusSuccess.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, StatusSuccess)
                ) {
                    Text(
                        text = "ડ્રો તારીખ: ${campaign.drawDate}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = cause,
                style = MaterialTheme.typography.bodySmall.copy(color = Slate400),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Grand Prize highlight box
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Navy850,
                border = BorderStroke(1.dp, Gold400.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Gold400,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "મહા પ્રથમ ઇનામ:",
                            style = MaterialTheme.typography.labelSmall.copy(color = Gold300)
                        )
                        Text(
                            text = grandPrize,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar of Tickets
            val progress = campaign.soldTickets.toFloat() / campaign.totalTickets.toFloat()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${campaign.soldTickets} / ${campaign.totalTickets} ટિકિટ બુક",
                    style = MaterialTheme.typography.labelSmall.copy(color = Slate400)
                )
                Text(
                    text = "દાન મૂલ્ય: ₹${campaign.ticketPriceInr}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold300
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Gold400,
                trackColor = Navy800
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onSelect,
                    modifier = Modifier
                        .weight(1f)
                        .height(46.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PureWhite),
                    border = BorderStroke(1.dp, Navy700)
                ) {
                    Text("વિગત & ઇનામો")
                }

                Button(
                    onClick = onPickTickets,
                    modifier = Modifier
                        .weight(1.3f)
                        .height(46.dp)
                        .testTag("pick_tickets_${campaign.id}"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Gold400,
                        contentColor = Navy950
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.ConfirmationNumber, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("લકી નંબર પસંદ કરો", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CampaignDetailScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val campaign by viewModel.selectedCampaign.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()
    val complianceGates by viewModel.complianceGates.collectAsState()

    if (campaign == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Campaign not found", color = PureWhite)
        }
        return
    }

    val comp = complianceGates[campaign!!.id]

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = "અભિયાન વિગતો & ઇનામ સૂચિ",
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            Surface(
                color = Navy900,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ટિકિટ દીઠ દાન:", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                        Text(
                            "₹${campaign!!.ticketPriceInr}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Gold400
                            )
                        )
                    }
                    Button(
                        onClick = { viewModel.navigateTo(Screen.TICKET_SELECTION) },
                        colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .height(50.dp)
                            .testTag("proceed_to_ticket_selection")
                    ) {
                        Icon(Icons.Default.ConfirmationNumber, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(viewModel.getString("select_tickets_btn"), fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        containerColor = Navy950
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.prize_showcase),
                        contentDescription = "Prizes Showcase",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            item {
                Text(
                    text = campaign!!.titleGu,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = campaign!!.causeDescriptionGu,
                    style = MaterialTheme.typography.bodyMedium.copy(color = Slate300)
                )
            }

            item {
                Text(
                    text = "સત્તાવાર ઇનામોની વિગત (Prize Gallery)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold400
                    )
                )
            }

            items(campaign!!.prizes) { prize ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, if (prize.rank == 1) Gold400 else Navy700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (prize.rank == 1) Gold400 else Navy800,
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "#${prize.rank}",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (prize.rank == 1) Navy950 else Gold300
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = prize.titleGu,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            )
                            Text(
                                text = prize.description,
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                            )
                        }

                        Text(
                            text = "₹${"%,d".format(prize.valuationInr)}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold400
                            )
                        )
                    }
                }
            }

            // Legal Transparency Certificate Card
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy850,
                    border = BorderStroke(1.dp, Gold500.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = Gold400)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "કાનૂની સલામતી પ્રમાણપત્ર & શરતો",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold300
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "• ટ્રસ્ટ રજિસ્ટ્રેશન: ${campaign!!.statutoryTrustRegNo}\n• કાનૂની મંજૂરી સંદર્ભ: ${campaign!!.complianceApprovalNumber}\n• કાનૂની સલાહકાર: ${comp?.legalCounsel ?: "Advocate Gujarat Bar Council"}\n• પાત્રતા: માત્ર ૧૮ વર્ષથી વધુ ઉંમરના ગુજરાત/ભારતીય નાગરિકો.\n• કર મુક્તિ: IT Act Section 80G હેઠળ માન્ય.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate300, lineHeight = 20.sp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun BottomNavigationBar(viewModel: DeeporamViewModel, currentScreen: Screen) {
    val currentUser by viewModel.currentUser.collectAsState()

    Surface(
        color = Navy900,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBar(
            containerColor = Navy900,
            contentColor = Slate300,
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
        ) {
            NavigationBarItem(
                selected = currentScreen == Screen.HOME,
                onClick = { viewModel.navigateTo(Screen.HOME) },
                icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                label = { Text(viewModel.getString("nav_home"), fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy950,
                    indicatorColor = Gold400,
                    selectedTextColor = Gold400,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_home_tab")
            )

            NavigationBarItem(
                selected = currentScreen == Screen.MY_TICKETS,
                onClick = { viewModel.navigateTo(Screen.MY_TICKETS) },
                icon = { Icon(Icons.Default.ConfirmationNumber, contentDescription = "Tickets") },
                label = { Text(viewModel.getString("nav_my_tickets"), fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy950,
                    indicatorColor = Gold400,
                    selectedTextColor = Gold400,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_my_tickets_tab")
            )

            NavigationBarItem(
                selected = currentScreen == Screen.DRAW_RESULTS,
                onClick = { viewModel.navigateTo(Screen.DRAW_RESULTS) },
                icon = { Icon(Icons.Default.EmojiEvents, contentDescription = "Results") },
                label = { Text(viewModel.getString("nav_results"), fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy950,
                    indicatorColor = Gold400,
                    selectedTextColor = Gold400,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_results_tab")
            )

            NavigationBarItem(
                selected = currentScreen == Screen.SUPPORT,
                onClick = { viewModel.navigateTo(Screen.SUPPORT) },
                icon = { Icon(Icons.Default.HeadsetMic, contentDescription = "Support") },
                label = { Text("સહાયતા", fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy950,
                    indicatorColor = Gold400,
                    selectedTextColor = Gold400,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_support_tab")
            )

            NavigationBarItem(
                selected = currentScreen == Screen.PROFILE,
                onClick = { viewModel.navigateTo(Screen.PROFILE) },
                icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                label = { Text(viewModel.getString("nav_profile"), fontSize = 11.sp) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy950,
                    indicatorColor = Gold400,
                    selectedTextColor = Gold400,
                    unselectedIconColor = Slate400,
                    unselectedTextColor = Slate400
                ),
                modifier = Modifier.testTag("nav_profile_tab")
            )
        }
    }
}
