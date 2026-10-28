package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import com.example.localization.AppLanguage
import com.example.model.UserRole
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.DeeporamTopBar
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.*

@Composable
fun CustomerSupportScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    var selectedFaqIndex by remember { mutableStateOf<Int?>(null) }
    var issueCategory by remember { mutableStateOf("ટિકિટ બુકિંગ સહાય") }
    var grievanceText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("support_helpdesk"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            BottomNavigationBar(viewModel = viewModel, currentScreen = Screen.SUPPORT)
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
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.SupportAgent, contentDescription = null, tint = Gold400, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "દીપોરામ કસ્ટમર સહાયતા કેન્દ્ર",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PureWhite
                                    )
                                )
                                Text(
                                    text = "સવાર ૯:૦૦ થી સાંજે ૭:૦૦ (સોમવાર - શનિવાર)",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Slate400)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Navy700)
                        Spacer(modifier = Modifier.height(14.dp))

                        Text("• હેલ્પલાઇન: +91 79 2658 0000 / +91 94260 00111", color = Slate300, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• ઈમેલ: support@deeporam.org / grievance@deeporam.org", color = Slate300, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("• કાર્યાલય: દીપોરામ ટ્રસ્ટ ભવન, એલિસબ્રિજ, અમદાવાદ - ૩૮૦૦૦૬", color = Slate300, fontSize = 13.sp)
                    }
                }
            }

            // FAQs
            item {
                Text(
                    text = "વારંવાર પૂછાતા પ્રશ્નો (FAQ)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Gold400
                    )
                )
            }

            val faqs = listOf(
                Pair(
                    "આ ચેરિટી ડ્રો કાયદેસર છે?",
                    "હા, દીપોરામ ફાઉન્ડેશન ગુજરાત પબ્લિક ટ્રસ્ટ એક્ટ ૧૯૫૦ હેઠળ નોંધાયેલ ટ્રસ્ટ (Reg No: E/9482/AHMEDABAD) છે. ડ્રોની તમામ પ્રક્રિયા કાનૂની સલાહકાર અને ચાર્ટર્ડ એકાઉન્ટન્ટ ઓડિટ હેઠળ થાય છે."
                ),
                Pair(
                    "ટિકિટ બુકિંગ પછી રસીદ ક્યારે મળે છે?",
                    "બુકિંગ સફળ થતાં જ એપમાં ડિજિટલ ટિકિટ પાસ અને QR કોડ જનરેટ થાય છે તથા આવકવેરા કલમ 80G કર મુક્તિ રસીદ આપના પ્રોફાઇલમાં ઉપલબ્ધ થાય છે."
                ),
                Pair(
                    "ડ્રો પરિણામોની પારદર્શિતા કેવી રીતે સુનિશ્ચિત થાય છે?",
                    "ડ્રો ક્રિપ્ટોગ્રાફિક રેન્ડમ નંબર જનરેટર (CSPRNG) દ્વારા સ્વતંત્ર ઓડિટર અને વિડિયો રેકોર્ડિંગની ઉપસ્થિતિમાં સંપન્ન થાય છે. પરિણામો SHA-256 હેશ સાથે સાર્વજનિક થાય છે."
                ),
                Pair(
                    "ડેમો મોડ શા માટે દર્શાવે છે?",
                    "કાનૂની સલામતી ગેટ પ્રોટોકોલ મુજબ, સંબંધિત સરકારી પરમિટ વેરિફિકેશન સુધી વાસ્તવિક નાણાકીય પેમેન્ટ અક્ષમ રાખી ડેમો સિમ્યુલેશન ચાલુ રાખેલ છે."
                )
            )

            items(faqs.size) { index ->
                val (q, a) = faqs[index]
                val isExpanded = selectedFaqIndex == index
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, if (isExpanded) Gold400 else Navy700),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedFaqIndex = if (isExpanded) null else index
                        }
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = q,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                ),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = Gold400
                            )
                        }
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = a, style = MaterialTheme.typography.bodySmall.copy(color = Slate300, lineHeight = 20.sp))
                        }
                    }
                }
            }

            // Grievance submission form
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, CardBorderNavy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "ફરિયાદ / સહાયતા નોંધણી (Grievance Desk)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold400
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = grievanceText,
                            onValueChange = { grievanceText = it },
                            placeholder = { Text("તમારી ફરિયાદ અથવા પ્રશ્ન અહીં લખો...") },
                            minLines = 3,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Gold400,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = PureWhite,
                                unfocusedTextColor = PureWhite,
                                focusedContainerColor = Navy850,
                                unfocusedContainerColor = Navy850
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("grievance_input_field")
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (grievanceText.isNotBlank()) {
                                    viewModel.showToast("ફરિયાદ નોંધાઈ ગઈ છે (ટિકિટ ID: GRV-${System.currentTimeMillis().toString().takeLast(5)})")
                                    grievanceText = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Gold400, contentColor = Navy950),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("submit_grievance_btn")
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("સહાયતા વિનંતી મોકલો", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.HOME)
    }

    val currentUser by viewModel.currentUser.collectAsState()
    val currentLang by viewModel.currentLanguage.collectAsState()

    var consentSms by remember { mutableStateOf(true) }
    var consentAudit by remember { mutableStateOf(true) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            DeeporamTopBar(
                viewModel = viewModel,
                title = viewModel.getString("nav_profile"),
                showBack = true,
                onBackClick = { viewModel.navigateTo(Screen.HOME) }
            )
        },
        bottomBar = {
            BottomNavigationBar(viewModel = viewModel, currentScreen = Screen.PROFILE)
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
            // Profile Header Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Navy800,
                            border = BorderStroke(2.dp, Gold400),
                            modifier = Modifier.size(68.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = Gold400, modifier = Modifier.size(40.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = currentUser?.name ?: "દાતા પ્રોફાઇલ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        )

                        Text(
                            text = currentUser?.phone ?: "",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = StatusSuccess.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, StatusSuccess)
                        ) {
                            Text(
                                text = "KYC & નાગરિકત્વ પ્રમાણિત (Gujarat)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = StatusSuccess
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Consents & Privacy
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.dp, Navy700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "સંમતિ અને ગોપનીયતા (Consents & Data Privacy)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold400
                            )
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ડ્રો સૂચનાઓ & SMS એલર્ટ", color = PureWhite, style = MaterialTheme.typography.bodySmall)
                                Text("ડ્રો પરિણામ અને ટિકિટ એલર્ટ મેળવો", color = Slate400, fontSize = 11.sp)
                            }
                            Switch(
                                checked = consentSms,
                                onCheckedChange = { consentSms = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Gold400, checkedTrackColor = Navy800)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = Navy700)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("ક્રિપ્ટોગ્રાફિક ઓડિટ ટ્રેઇલ સંમતિ", color = PureWhite, style = MaterialTheme.typography.bodySmall)
                                Text("પારદર્શિતા હેતુ ટિકિટ ક્રમ સુરક્ષિત રાખવો", color = Slate400, fontSize = 11.sp)
                            }
                            Switch(
                                checked = consentAudit,
                                onCheckedChange = { consentAudit = it },
                                colors = SwitchDefaults.colors(checkedThumbColor = Gold400, checkedTrackColor = Navy800)
                            )
                        }
                    }
                }
            }

            // Secure Admin Portal Entry
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = Gold400, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "એડમિનિસ્ટ્રેટર કન્સોલ (Admin & Trustee Access)",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold400
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "ઇવેન્ટ્સ/ડ્રો કસ્ટમાઇઝેશન, દાન ખાતાઓનું સંચાલન અને કમ્પ્લાયન્સ માટે સ્પેશિયલ ID અને પાસવર્ડ દ્વારા અધિકૃત લૉગિન કરો.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Slate300, lineHeight = 18.sp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                viewModel.navigateTo(Screen.ADMIN_LOGIN)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Navy800, contentColor = Gold400),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Gold400),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("open_admin_login_btn")
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("સ્પેશિયલ ID દ્વારા એડમિન લૉગિન", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Account deletion request
            item {
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusDanger),
                    border = BorderStroke(1.dp, StatusDanger),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ખાતું અને ડેટા ડિલીટ કરવાની વિનંતી")
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = Navy900,
            title = { Text("ખાતું ડિલીટ કરવાની વિનંતી", color = StatusDanger, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    "આપના એકાઉન્ટ ડિલીશનની અરજી સ્વીકારવામાં આવશે. ચેરિટી ઓડિટ અને કાનૂની હિસાબ જરૂરિયાતો માટે ટિકિટ રેકોર્ડ્સ નિયમાનુસાર સચવાશે, જ્યારે અંગત ઓળખ ૩૦ દિવસની ગ્રેસ પિરિયડ બાદ હટાવી દેવામાં આવશે.",
                    color = Slate300
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    viewModel.showToast("ડિલીશન વિનંતી સફળતાપૂર્વક નોંધાઈ છે.")
                }) {
                    Text("વિનંતી સબમિટ કરો", color = StatusDanger, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("રદ કરો", color = Slate400)
                }
            }
        )
    }
}
