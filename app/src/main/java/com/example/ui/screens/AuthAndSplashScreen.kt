package com.example.ui.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.PhoneAuthState
import com.example.localization.AppLanguage
import com.example.model.UserRole
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.GoldGradientButton
import com.example.ui.components.LegalSafetyGateBanner
import com.example.ui.theme.*

@Composable
fun SplashScreen(viewModel: DeeporamViewModel) {
    val currentLang by viewModel.currentLanguage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Navy950, Navy900, Navy850)
                )
            )
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Branded Diya Logo with Glowing Border
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape)
                    .border(2.5.dp, Gold400, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.deeporam_logo),
                    contentDescription = "Deeporam Foundation Logo",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "દીપોરામ ફાઉન્ડેશન",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Gold300,
                    letterSpacing = 0.5.sp
                ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "DEEPORAM FOUNDATION",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Slate300,
                    letterSpacing = 2.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = viewModel.getString("tagline"),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Slate400,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Language Selector
            Text(
                text = viewModel.getString("select_language"),
                style = MaterialTheme.typography.labelMedium.copy(color = Gold400, fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                AppLanguage.values().forEach { lang ->
                    val isSelected = lang == currentLang
                    OutlinedButton(
                        onClick = { viewModel.setLanguage(lang) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isSelected) Navy800 else Navy950,
                            contentColor = if (isSelected) Gold300 else Slate400
                        ),
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) Gold400 else Navy700
                        ),
                        modifier = Modifier.testTag("splash_lang_${lang.code}")
                    ) {
                        Text(lang.nativeName, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            GoldGradientButton(
                text = viewModel.getString("continue_btn"),
                onClick = { viewModel.navigateTo(Screen.AUTH) },
                icon = Icons.Default.ArrowForward,
                testTag = "splash_continue_btn"
            )
        }
    }
}

@Composable
fun AuthScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.SPLASH)
    }

    val context = LocalContext.current
    val activity = context as? Activity
    val keyboardController = LocalSoftwareKeyboardController.current

    val authState by viewModel.authState.collectAsState()
    val incomingSms by viewModel.incomingSms.collectAsState()

    var phoneInput by remember { mutableStateOf("") }
    // Clean, empty manual OTP input - user enters manually, never pre-filled or shown in auth box
    var manualOtp by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(authState) {
        if (authState is PhoneAuthState.CodeSent) {
            manualOtp = "" // Strictly clean OTP box when OTP is dispatched
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy950)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("auth_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header with Logo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .border(1.5.dp, Gold400, CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.deeporam_logo),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "દીપોરામ ફાઉન્ડેશન",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            )
                            Text(
                                text = "સુરક્ષિત મોબાઇલ વેરિફિકેશન",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gold400)
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Navy800,
                        border = BorderStroke(1.dp, Gold500)
                    ) {
                        Text(
                            text = "FIREBASE AUTH",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Gold300
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Steps progress indicator for New Users
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StepChip(title = "૧. મોબાઇલ", isActive = authState is PhoneAuthState.Idle || authState is PhoneAuthState.SendingCode)
                    StepChip(title = "૨. OTP વેરિફિકેશન", isActive = authState is PhoneAuthState.CodeSent || authState is PhoneAuthState.Verifying)
                    StepChip(title = "૩. પ્રોફાઇલ નોંધણી", isActive = false)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = viewModel.getString("login_title"),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                )

                Text(
                    text = "તમારા મોબાઇલ નંબર પર SMS દ્વારા સુરક્ષિત વેરિફિકેશન કોડ મોકલવામાં આવશે.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Slate400)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Phone Input Field
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { input ->
                        val digits = input.filter { it.isDigit() }
                        if (digits.length <= 10) phoneInput = digits
                    },
                    label = { Text(viewModel.getString("enter_phone")) },
                    placeholder = { Text("98765 43210") },
                    leadingIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Text("🇮🇳", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "+91",
                                color = Gold400,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                        }
                    },
                    singleLine = true,
                    enabled = authState !is PhoneAuthState.CodeSent && authState !is PhoneAuthState.Verifying,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite,
                        focusedLabelColor = Gold300,
                        unfocusedLabelColor = Slate400,
                        focusedContainerColor = Navy900,
                        unfocusedContainerColor = Navy900
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("phone_input_field")
                )

                // OTP Verification Section when SMS is dispatched
                if (authState is PhoneAuthState.CodeSent || authState is PhoneAuthState.Verifying) {
                    val codeSentState = authState as? PhoneAuthState.CodeSent

                    Spacer(modifier = Modifier.height(22.dp))

                    Text(
                        text = "SMS વેરિફિકેશન કોડ (મેન્યુઅલી દાખલ કરો):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gold300
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Clean, empty OTP Boxes with hidden manual input
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                focusRequester.requestFocus()
                                keyboardController?.show()
                            }
                    ) {
                        // Invisible BasicTextField capturing manual keypresses
                        BasicTextField(
                            value = manualOtp,
                            onValueChange = { newOtp ->
                                val filtered = newOtp.filter { it.isDigit() }
                                if (filtered.length <= 6) {
                                    manualOtp = filtered
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    if (manualOtp.length == 6) {
                                        viewModel.verifyPhoneOtp(manualOtp) {}
                                    }
                                }
                            ),
                            modifier = Modifier
                                .focusRequester(focusRequester)
                                .fillMaxWidth()
                                .height(52.dp)
                                .alpha(0.01f) // Hidden from view, handles input
                        )

                        // 6 separate, clean OTP Digit Boxes (User enters manually, no OTP displayed in auth box)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            for (i in 0 until 6) {
                                val char = manualOtp.getOrNull(i)?.toString() ?: ""
                                val isFocusedBox = manualOtp.length == i

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Navy900,
                                    border = BorderStroke(
                                        if (isFocusedBox) 2.dp else 1.2.dp,
                                        if (isFocusedBox) Gold400 else if (char.isNotEmpty()) Gold300 else Navy700
                                    ),
                                    modifier = Modifier
                                        .size(46.dp)
                                        .testTag("otp_box_$i")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = char,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = Gold400,
                                                fontSize = 20.sp
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val remaining = codeSentState?.secondsRemaining ?: 0
                        if (remaining > 0) {
                            Text(
                                text = "ફરીથી SMS મોકલો: ${remaining}s",
                                style = MaterialTheme.typography.bodySmall.copy(color = Slate400)
                            )
                        } else {
                            TextButton(
                                onClick = {
                                    manualOtp = "" // Keep clean
                                    viewModel.sendPhoneOtp(activity, phoneInput)
                                },
                                modifier = Modifier.testTag("resend_otp_btn")
                            ) {
                                Text(
                                    text = "ફરીથી SMS કોડ મોકલો",
                                    color = Gold400,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Text(
                            text = "૬ અંકનો કોડ દાખલ કરો",
                            style = MaterialTheme.typography.labelSmall.copy(color = Slate500)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Primary Action Button
                if (authState !is PhoneAuthState.CodeSent && authState !is PhoneAuthState.Verifying) {
                    GoldGradientButton(
                        text = viewModel.getString("get_otp"),
                        onClick = {
                            if (phoneInput.length >= 10) {
                                viewModel.sendPhoneOtp(activity, phoneInput)
                            } else {
                                viewModel.showToast("કૃપા કરીને માન્ય ૧૦ અંકનો નંબર દાખલ કરો")
                            }
                        },
                        icon = Icons.Default.Sms,
                        enabled = phoneInput.length >= 10,
                        testTag = "get_otp_btn"
                    )
                } else {
                    GoldGradientButton(
                        text = viewModel.getString("verify_otp"),
                        onClick = {
                            viewModel.verifyPhoneOtp(manualOtp) { success ->
                                // Navigation handled inside viewModel
                            }
                        },
                        icon = Icons.Default.CheckCircle,
                        enabled = manualOtp.length == 6,
                        testTag = "verify_otp_btn"
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Official Portal Switcher
                OutlinedButton(
                    onClick = { viewModel.loginAdmin(UserRole.ADMIN) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("quick_admin_login"),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Navy900,
                        contentColor = PureWhite
                    ),
                    border = BorderStroke(1.dp, Navy700)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = Gold400, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(viewModel.getString("admin_login"), fontWeight = FontWeight.SemiBold)
                }
            }

            LegalSafetyGateBanner(modifier = Modifier.padding(bottom = 12.dp))
        }

        // Heads-up Incoming SMS Notification Banner (Simulating phone carrier SMS arrival, without auto-filling the auth box)
        AnimatedVisibility(
            visible = incomingSms != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(12.dp)
        ) {
            incomingSms?.let { sms ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.5.dp, Gold400),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(16.dp))
                        .testTag("incoming_sms_banner")
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
                                    color = Gold400,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Chat, contentDescription = null, tint = Navy950, modifier = Modifier.size(14.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "SMS સંદેશ • ${sms.sender} • Now",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Gold300,
                                        letterSpacing = 0.5.sp
                                    )
                                )
                            }

                            IconButton(
                                onClick = { viewModel.dismissIncomingSms() },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Close", tint = Slate400, modifier = Modifier.size(16.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "દીપોરામ ફાઉન્ડેશન વેરિફિકેશન કોડ આપના નંબર પર પ્રાપ્ત થયો છે: ${sms.otpCode}. કૃપા કરીને આ ૬ અંકનો કોડ બોક્સમાં મેન્યુઅલી દાખલ કરો.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PureWhite,
                                lineHeight = 18.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = { viewModel.dismissIncomingSms() }
                            ) {
                                Text("સમજાઈ ગયું (Dismiss)", color = Gold400, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepChip(title: String, isActive: Boolean) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isActive) Navy800 else Navy900,
        border = BorderStroke(1.dp, if (isActive) Gold400 else Navy700),
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) Gold300 else Slate500
            ),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun NewUserSetupScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateTo(Screen.AUTH)
    }

    var fullName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("અમદાવાદ (Ahmedabad)") }
    var termsConfirmed by remember { mutableStateOf(true) }

    val gujaratCities = listOf(
        "અમદાવાદ (Ahmedabad)",
        "સુરત (Surat)",
        "વડોદરા (Vadodara)",
        "રાજકોટ (Rajkot)",
        "ગાંધીનગર (Gandhinagar)",
        "ભાવનગર (Bhavnagar)",
        "જામનગર (Jamnagar)",
        "જૂનાગઢ (Junagadh)",
        "આણંદ (Anand)",
        "અન્ય જિલ્લો (Other Gujarat District)"
    )

    var cityDropdownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Navy950)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
            .testTag("new_user_setup_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Gold400, CircleShape)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.deeporam_logo),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = StatusSuccess.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, StatusSuccess)
                    ) {
                        Text(
                            text = "OTP VERIFIED ✓",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = StatusSuccess
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = viewModel.getString("new_user_setup"),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PureWhite
                    )
                )

                Text(
                    text = "દીપોરામ ચેરિટેબલ ટ્રસ્ટમાં આપનું સ્વાગત છે. 80G કર મુક્તિ રસીદ અને ડ્રો ટિકિટ માટે તમારી વિગતો પૂર્ણ કરો.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Slate400)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Welcome Gift Badge Card
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Navy900,
                    border = BorderStroke(1.5.dp, Gold400),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CardGiftcard, contentDescription = null, tint = Gold400, modifier = Modifier.size(32.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "નવા દાતા સ્વાગત પુરસ્કાર:",
                                style = MaterialTheme.typography.labelSmall.copy(color = Gold300, fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "૧ મફત લકી ટિકિટ એન્ટ્રી ફાળવવામાં આવશે!",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PureWhite
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Full Name Input
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text(viewModel.getString("enter_full_name")) },
                    placeholder = { Text("દા.ત. અર્જુન સોલંકી (Arjun Solanki)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Gold400) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Gold400,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = PureWhite,
                        unfocusedTextColor = PureWhite,
                        focusedLabelColor = Gold300,
                        unfocusedLabelColor = Slate400,
                        focusedContainerColor = Navy900,
                        unfocusedContainerColor = Navy900
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("full_name_input")
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Gujarat City Selection
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = selectedCity,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("ગુજરાત રાજ્ય જિલ્લો / શહેર") },
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = Gold400) },
                        trailingIcon = {
                            IconButton(onClick = { cityDropdownExpanded = !cityDropdownExpanded }) {
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Gold400)
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gold400,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite,
                            focusedContainerColor = Navy900,
                            unfocusedContainerColor = Navy900
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { cityDropdownExpanded = true }
                    )

                    DropdownMenu(
                        expanded = cityDropdownExpanded,
                        onDismissRequest = { cityDropdownExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .background(Navy850)
                    ) {
                        gujaratCities.forEach { city ->
                            DropdownMenuItem(
                                text = { Text(city, color = PureWhite) },
                                onClick = {
                                    selectedCity = city
                                    cityDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 18+ declaration checkbox
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { termsConfirmed = !termsConfirmed },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = termsConfirmed,
                        onCheckedChange = { termsConfirmed = it },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Gold400,
                            checkmarkColor = Navy950,
                            uncheckedColor = Slate400
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "હું પ્રમાણિત કરું છું કે મારી ઉંમર ૧૮+ વર્ષ છે અને હું ભારતીય નાગરિક છું.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Slate300)
                    )
                }
            }

            Column {
                GoldGradientButton(
                    text = "નોંધણી પૂર્ણ કરો & પ્રવેશ કરો",
                    onClick = {
                        viewModel.completeNewUserRegistration(fullName, selectedCity)
                    },
                    enabled = fullName.isNotBlank() && termsConfirmed,
                    icon = Icons.Default.CheckCircle,
                    testTag = "complete_registration_btn"
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
