package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ui.DeeporamViewModel
import com.example.ui.Screen
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.Navy950

class MainActivity : ComponentActivity() {

    private val viewModel: DeeporamViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val currentScreen by viewModel.currentScreen.collectAsState()
                val toastMessage by viewModel.toastMessage.collectAsState()
                val context = LocalContext.current

                LaunchedEffect(toastMessage) {
                    toastMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearToast()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Navy950
                ) {
                    Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                        when (screen) {
                            Screen.SPLASH -> SplashScreen(viewModel = viewModel)
                            Screen.AUTH -> AuthScreen(viewModel = viewModel)
                            Screen.NEW_USER_SETUP -> NewUserSetupScreen(viewModel = viewModel)
                            Screen.HOME -> HomeScreen(viewModel = viewModel)
                            Screen.CAMPAIGN_DETAIL -> CampaignDetailScreen(viewModel = viewModel)
                            Screen.TICKET_SELECTION -> TicketSelectionScreen(viewModel = viewModel)
                            Screen.CHECKOUT -> BookingCheckoutScreen(viewModel = viewModel)
                            Screen.MY_TICKETS -> MyTicketsScreen(viewModel = viewModel)
                            Screen.DRAW_RESULTS -> DrawResultsScreen(viewModel = viewModel)
                            Screen.SUPPORT -> CustomerSupportScreen(viewModel = viewModel)
                            Screen.PROFILE -> ProfileScreen(viewModel = viewModel)
                            Screen.ADMIN_PORTAL -> AdminPortalScreen(viewModel = viewModel)
                        }
                    }
                }
            }
        }
    }
}
