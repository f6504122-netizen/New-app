package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.localization.AppLanguage
import com.example.model.Booking
import com.example.model.UserRole
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeeporamTopBar(
    viewModel: DeeporamViewModel,
    title: String,
    showBack: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    var showLangDialog by remember { mutableStateOf(false) }
    var showRoleMenu by remember { mutableStateOf(false) }

    val currentLang by viewModel.currentLanguage.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    Surface(
        color = Navy900,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if (showBack) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Gold400
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                } else {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Gold400, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.deeporam_logo),
                            contentDescription = "Deeporam Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = 17.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = if (currentUser?.role == UserRole.CUSTOMER) "દીપોરામ ચેરિટેબલ ટ્રસ્ટ" else "Administrator Console",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Gold300,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Switcher Badge
                Surface(
                    onClick = { showLangDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    color = Navy800,
                    border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier.testTag("language_selector_button")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Translate",
                            tint = Gold400,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = currentLang.nativeName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = PureWhite,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Role Switcher / Admin portal icon
                IconButton(
                    onClick = { showRoleMenu = true },
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("role_menu_button")
                ) {
                    Icon(
                        imageVector = if (currentUser?.role == UserRole.CUSTOMER) Icons.Default.AdminPanelSettings else Icons.Default.AccountCircle,
                        contentDescription = "Switch Role",
                        tint = Gold400
                    )
                }

                DropdownMenu(
                    expanded = showRoleMenu,
                    onDismissRequest = { showRoleMenu = false },
                    modifier = Modifier.background(Navy850)
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Customer App (ગ્રાહક પોર્ટલ)",
                                color = if (currentUser?.role == UserRole.CUSTOMER) Gold400 else PureWhite
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gold400) },
                        onClick = {
                            viewModel.switchUserRole(UserRole.CUSTOMER)
                            viewModel.navigateTo(Screen.HOME)
                            showRoleMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Admin Portal (ટ્રસ્ટી / એડમિન)",
                                color = if (currentUser?.role == UserRole.ADMIN) Gold400 else PureWhite
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = Gold400) },
                        onClick = {
                            viewModel.switchUserRole(UserRole.ADMIN)
                            viewModel.navigateTo(Screen.ADMIN_PORTAL)
                            showRoleMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "Compliance Officer / Auditor",
                                color = if (currentUser?.role == UserRole.COMPLIANCE_OFFICER) Gold400 else PureWhite
                            )
                        },
                        leadingIcon = { Icon(Icons.Default.Gavel, contentDescription = null, tint = Gold400) },
                        onClick = {
                            viewModel.switchUserRole(UserRole.COMPLIANCE_OFFICER)
                            viewModel.navigateTo(Screen.ADMIN_PORTAL)
                            showRoleMenu = false
                        }
                    )
                    HorizontalDivider(color = Navy700)
                    DropdownMenuItem(
                        text = { Text("Logout / Sign Out", color = StatusDanger) },
                        leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = StatusDanger) },
                        onClick = {
                            viewModel.logout()
                            showRoleMenu = false
                        }
                    )
                }
            }
        }
    }

    if (showLangDialog) {
        LanguageSelectorDialog(
            currentLanguage = currentLang,
            onSelect = {
                viewModel.setLanguage(it)
                showLangDialog = false
            },
            onDismiss = { showLangDialog = false }
        )
    }
}

@Composable
fun LanguageSelectorDialog(
    currentLanguage: AppLanguage,
    onSelect: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Navy900,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Language, contentDescription = null, tint = Gold400)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Select Language / ભાષા પસંદ કરો", color = PureWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AppLanguage.values().forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Surface(
                        onClick = { onSelect(lang) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Navy800 else Navy850,
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) Gold400 else Navy700
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("lang_opt_${lang.code}")
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = lang.nativeName,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = if (isSelected) Gold300 else PureWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = lang.displayName,
                                    style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                                )
                            }
                            if (isSelected) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = Gold400)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close / બંધ કરો", color = Gold400)
            }
        }
    )
}

@Composable
fun GoldGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    testTag: String = "gold_button"
) {
    val brush = Brush.horizontalGradient(
        colors = if (enabled) listOf(Gold400, Gold500, Gold600) else listOf(Slate500, Slate400)
    )

    Surface(
        onClick = onClick,
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp)
            .shadow(if (enabled) 6.dp else 0.dp, shape = RoundedCornerShape(14.dp), ambientColor = Gold500)
            .background(brush, RoundedCornerShape(14.dp))
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) Navy950 else Slate300,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (enabled) Navy950 else Slate300,
                    letterSpacing = 0.5.sp
                )
            )
        }
    }
}

@Composable
fun LegalSafetyGateBanner(
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Navy850,
        border = BorderStroke(1.dp, Gold500.copy(alpha = 0.4f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.VerifiedUser,
                    contentDescription = "Verified Trust",
                    tint = Gold400,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "કાનૂની સલામતી અને ચેરિટી ઓડિટ ગેટ (Legal Safety Gate)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold300
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "• માન્ય રજિસ્ટર્ડ ટ્રસ્ટ: E/9482/AHMEDABAD\n• કાનૂની સલાહકાર: એડવોકેટ હસમુખ એમ. દવે (બાર કાઉન્સિલ ઓફ ગુજરાત)\n• આવકવેરા કલમ 80G હેઠળ કર મુક્તિ માન્ય\n• તમામ ટિકિટ ફાળવણી અને ચુકવણી વ્યવહારો ક્રિપ્ટોગ્રાફિકલી ઓડિટ અને પ્રમાણિત છે.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate300,
                    lineHeight = 18.sp
                )
            )
        }
    }
}

@Composable
fun DigitalTicketPassCard(
    booking: Booking,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Navy850,
        border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.6f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("ticket_pass_${booking.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, Gold400, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.deeporam_logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "દીપોરામ ફાઉન્ડેશન",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold400
                            )
                        )
                        Text(
                            text = "સત્તાવાર ચેરિટી ડ્રો પાસ",
                            style = MaterialTheme.typography.labelSmall.copy(color = Slate400)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = StatusSuccess.copy(alpha = 0.2f),
                    border = BorderStroke(1.dp, StatusSuccess)
                ) {
                    Text(
                        text = "સક્રિય પાસ (VERIFIED)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = StatusSuccess
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Ticket Numbers Grid
            Text(
                text = "ફાળવેલ લકી ટિકિટ નંબરો:",
                style = MaterialTheme.typography.labelMedium.copy(color = Slate400)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                booking.ticketNumbers.forEach { num ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Navy800,
                        border = BorderStroke(1.dp, Gold400)
                    ) {
                        Text(
                            text = "#$num",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Gold300
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Navy700)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("બુકિંગ ID", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                    Text(booking.id, style = MaterialTheme.typography.bodySmall.copy(color = PureWhite, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("80G રસીદ નં.", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                    Text(booking.tax80GCertificateNumber, style = MaterialTheme.typography.bodySmall.copy(color = Gold300))
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("કુલ દાન રકમ", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                    Text("₹${booking.totalAmountInr}", style = MaterialTheme.typography.titleMedium.copy(color = Gold400, fontWeight = FontWeight.Bold))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("ચુકવણી સ્થિતિ", style = MaterialTheme.typography.labelSmall.copy(color = Slate400))
                    Text("સફળ (UPI Verified)", style = MaterialTheme.typography.bodySmall.copy(color = StatusSuccess))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Barcode & QR Code simulation visual
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = PureWhite,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "DIGITAL TRUST PASSPORT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Navy900,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = booking.qrToken.take(24) + "...",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                fontSize = 9.sp
                            )
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        tint = Navy950,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
