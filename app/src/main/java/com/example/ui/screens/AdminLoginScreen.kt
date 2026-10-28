package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.components.GoldGradientButton
import com.example.ui.theme.*

@Composable
fun AdminLoginScreen(viewModel: DeeporamViewModel) {
    BackHandler {
        viewModel.navigateBack()
    }

    val focusManager = LocalFocusManager.current
    var specialId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    fun submitLogin() {
        if (specialId.isBlank()) {
            errorMessage = "કૃપા કરીને સ્પેશિયલ એડમિન આઈડી દાખલ કરો / Please enter Special Admin ID"
            return
        }
        if (password.isBlank()) {
            errorMessage = "કૃપા કરીને એડમિન પાસવર્ડ દાખલ કરો / Please enter Admin Password"
            return
        }

        errorMessage = null
        isAuthenticating = true
        val success = viewModel.loginAdminWithSpecialCredentials(specialId, password)
        isAuthenticating = false
        if (!success) {
            errorMessage = "અમાન્ય સ્પેશિયલ આઈડી અથવા પાસવર્ડ! કૃપા કરીને ફરી ચકાસો. (Invalid Credentials)"
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = Navy950,
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { viewModel.navigateBack() },
                        modifier = Modifier.testTag("admin_login_back_btn")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "પાછા જાઓ",
                            tint = PureWhite
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "એડમિન પોર્ટલ પ્રવેશ / Admin Login",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gold400
                        )
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Shield / Admin Branding Badge
            Surface(
                shape = CircleShape,
                color = Navy900,
                border = BorderStroke(2.dp, Gold400),
                modifier = Modifier.size(88.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin Shield",
                        tint = Gold400,
                        modifier = Modifier.size(52.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "દીપોરામ ફાઉન્ડેશન",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = PureWhite
                )
            )

            Text(
                text = "અધિકૃત એડમિનિસ્ટ્રેટર & ટ્રસ્ટી કન્સોલ",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Gold300,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "ઇવેન્ટ્સ, કમ્પ્લાયન્સ અને યુઝર એકાઉન્ટ્સ કસ્ટમાઇઝેશન માટે આપનું સ્પેશિયલ ID અને પાસવર્ડ દાખલ કરો.",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Slate400,
                    lineHeight = 18.sp
                ),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Login Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Navy900,
                border = BorderStroke(1.5.dp, Gold500.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "વિશેષ ઓળખ પ્રમાણીકરણ (Special Credentials)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Gold400
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Special Admin ID Field
                    Text(
                        text = "સ્પેશિયલ એડમિન ID (Special Admin ID)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = specialId,
                        onValueChange = {
                            specialId = it
                            errorMessage = null
                        },
                        placeholder = { Text("e.g. DEEPORAM_ADMIN", color = Slate500) },
                        leadingIcon = {
                            Icon(Icons.Default.Badge, contentDescription = null, tint = Gold400)
                        },
                        trailingIcon = {
                            if (specialId.isNotBlank()) {
                                IconButton(onClick = { specialId = "" }) {
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_special_id_input")
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Admin Password Field
                    Text(
                        text = "સિક્યુરિટી પાસવર્ડ (Security Password)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = {
                            password = it
                            errorMessage = null
                        },
                        placeholder = { Text("••••••••••••", color = Slate500) },
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Gold400)
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                    tint = if (passwordVisible) Gold400 else Slate400
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Gold400,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = PureWhite,
                            unfocusedTextColor = PureWhite,
                            cursorColor = Gold400
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                focusManager.clearFocus()
                                submitLogin()
                            }
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("admin_password_input")
                    )

                    // Error Message
                    AnimatedVisibility(visible = errorMessage != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = StatusDanger.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, StatusDanger),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = StatusDanger)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage ?: "",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = PureWhite,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    GoldGradientButton(
                        text = if (isAuthenticating) "પ્રમાણીકરણ ચાલુ છે..." else "સુરક્ષિત એડમિન પ્રવેશ (Verify & Login)",
                        onClick = {
                            focusManager.clearFocus()
                            submitLogin()
                        },
                        icon = Icons.Default.Key,
                        enabled = !isAuthenticating,
                        modifier = Modifier.testTag("admin_login_submit_btn")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Quick Auto-fill Assist Card for testing convenience
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Navy900.copy(alpha = 0.8f),
                border = BorderStroke(1.dp, Navy700),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = Gold400, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "માન્ય સ્પેશિયલ ઓળખપત્ર (Credentials)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Gold300
                                )
                            )
                        }

                        TextButton(
                            onClick = {
                                specialId = "DEEPORAM_ADMIN"
                                password = "Deeporam@2026"
                                errorMessage = null
                            },
                            modifier = Modifier.testTag("quick_autofill_admin_btn")
                        ) {
                            Icon(Icons.Default.AutoFixHigh, contentDescription = null, tint = Gold400, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ઓટોફિલ કરો", color = Gold400, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• સ્પેશિયલ ID: DEEPORAM_ADMIN અથવા ADMIN786\n• પાસવર્ડ: Deeporam@2026 અથવા Admin@123",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Slate400,
                            lineHeight = 18.sp,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
