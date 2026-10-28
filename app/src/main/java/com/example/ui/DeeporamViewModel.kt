package com.example.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.DeeporamRepository
import com.example.data.FirebaseAuthService
import com.example.data.PhoneAuthState
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class Screen {
    SPLASH,
    AUTH,
    NEW_USER_SETUP,
    HOME,
    CAMPAIGN_DETAIL,
    TICKET_SELECTION,
    CHECKOUT,
    MY_TICKETS,
    DRAW_RESULTS,
    SUPPORT,
    PROFILE,
    ADMIN_PORTAL
}

data class TicketSelectionState(
    val selectedTicketNumbers: Set<Int> = emptySet(),
    val searchQuery: String = "",
    val statusFilter: TicketStatus? = null,
    val reservationWarning: String? = null
)

data class VerifyTicketState(
    val queryNumber: String = "",
    val isChecking: Boolean = false,
    val checkedResult: WinningTicket? = null,
    val hasChecked: Boolean = false
)

data class LiveDrawSimulationState(
    val isRunning: Boolean = false,
    val currentNumberDisplay: String = "----",
    val completedResult: DrawResult? = null
)

class DeeporamViewModel(
    private val repository: DeeporamRepository = DeeporamRepository.getInstance()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(Screen.SPLASH)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _screenHistory = mutableListOf<Screen>()

    val currentLanguage: StateFlow<AppLanguage> = repository.currentLanguage
    val currentUser: StateFlow<AppUser?> = repository.currentUser
    val campaigns: StateFlow<List<Campaign>> = repository.campaigns
    val ticketsByCampaign: StateFlow<Map<String, List<DrawTicket>>> = repository.ticketsByCampaign
    val userBookings: StateFlow<List<Booking>> = repository.userBookings
    val drawResults: StateFlow<List<DrawResult>> = repository.drawResults
    val auditLogs: StateFlow<List<AuditLog>> = repository.auditLogs
    val complianceGates: StateFlow<Map<String, ComplianceGate>> = repository.complianceGates

    private val _selectedCampaignId = MutableStateFlow<String?>("camp_deepotsav_2026")
    val selectedCampaignId: StateFlow<String?> = _selectedCampaignId.asStateFlow()

    private val _ticketSelectionState = MutableStateFlow(TicketSelectionState())
    val ticketSelectionState: StateFlow<TicketSelectionState> = _ticketSelectionState.asStateFlow()

    private val _verifyTicketState = MutableStateFlow(VerifyTicketState())
    val verifyTicketState: StateFlow<VerifyTicketState> = _verifyTicketState.asStateFlow()

    private val _liveDrawSimulationState = MutableStateFlow(LiveDrawSimulationState())
    val liveDrawSimulationState: StateFlow<LiveDrawSimulationState> = _liveDrawSimulationState.asStateFlow()

    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _termsAccepted = MutableStateFlow(false)
    val termsAccepted: StateFlow<Boolean> = _termsAccepted.asStateFlow()

    private val authService = FirebaseAuthService.getInstance()
    val authState: StateFlow<PhoneAuthState> = authService.authState
    val incomingSms = authService.incomingSms

    private val _pendingPhone = MutableStateFlow("+91 98765 43210")
    val pendingPhone: StateFlow<String> = _pendingPhone.asStateFlow()

    val selectedCampaign: StateFlow<Campaign?> = combine(campaigns, selectedCampaignId) { list, id ->
        list.find { it.id == id } ?: list.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun sendPhoneOtp(activity: Activity?, phone: String) {
        _pendingPhone.value = phone
        authService.sendVerificationCode(activity, phone)
        showToast("સુરક્ષિત SMS સંદેશ મોકલાયો છે / SMS OTP Dispatched")
    }

    fun verifyPhoneOtp(otp: String, onVerified: (Boolean) -> Unit) {
        authService.verifyCode(otp) { success, message ->
            if (success) {
                showToast("મોબાઇલ નંબર સફળતાપૂર્વક પ્રમાણિત થયો! / Phone verified!")
                navigateTo(Screen.NEW_USER_SETUP)
            } else {
                showToast(message)
            }
            onVerified(success)
        }
    }

    fun dismissIncomingSms() {
        authService.dismissIncomingSms()
    }

    fun completeNewUserRegistration(name: String, city: String) {
        val phone = _pendingPhone.value.ifBlank { "+91 98765 43210" }
        repository.registerNewUser(name, phone, city)
        authService.reset()
        showToast("સ્વાગત છે! ૧ મફત લકી ટિકિટ એન્ટ્રી ફાળવાઈ ગઈ છે! / Welcome! 1 Free Entry Pass allocated!")
        navigateTo(Screen.HOME)
    }

    fun getString(key: String): String {
        return AppStrings.get(key, repository.currentLanguage.value)
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            _screenHistory.add(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun navigateBack(): Boolean {
        if (_screenHistory.isNotEmpty()) {
            val prev = _screenHistory.removeAt(_screenHistory.size - 1)
            _currentScreen.value = prev
            return true
        }
        if (_currentScreen.value != Screen.HOME) {
            _currentScreen.value = Screen.HOME
            return true
        }
        return false
    }

    fun setLanguage(language: AppLanguage) {
        repository.setLanguage(language)
    }

    fun selectCampaign(campaignId: String) {
        _selectedCampaignId.value = campaignId
        _ticketSelectionState.value = TicketSelectionState()
    }

    fun toggleTicketSelection(ticketNumber: Int) {
        val cid = _selectedCampaignId.value ?: return
        val currentSet = _ticketSelectionState.value.selectedTicketNumbers.toMutableSet()
        if (currentSet.contains(ticketNumber)) {
            currentSet.remove(ticketNumber)
            repository.unreserveTicket(cid, ticketNumber)
        } else {
            if (currentSet.size >= 10) {
                showToast("Maximum 10 tickets per booking permitted / એક સાથે વધુમાં વધુ ૧૦ ટિકિટ")
                return
            }
            val success = repository.reserveTicket(cid, ticketNumber)
            if (success) {
                currentSet.add(ticketNumber)
            } else {
                showToast("Ticket is already reserved or booked / ટિકિટ પહેલેથી જ આરક્ષિત છે")
            }
        }
        _ticketSelectionState.value = _ticketSelectionState.value.copy(selectedTicketNumbers = currentSet)
    }

    fun pickLuckyTickets(count: Int) {
        val cid = _selectedCampaignId.value ?: return
        val picked = repository.pickLuckyTickets(cid, count)
        if (picked.isEmpty()) {
            showToast("No available tickets left in this campaign")
        } else {
            val currentSet = _ticketSelectionState.value.selectedTicketNumbers.toMutableSet()
            currentSet.addAll(picked)
            _ticketSelectionState.value = _ticketSelectionState.value.copy(selectedTicketNumbers = currentSet)
            showToast("Picked ${picked.size} Lucky Numbers! / ${picked.size} લકી નંબર ફાળવવામાં આવ્યા!")
        }
    }

    fun setTicketFilter(status: TicketStatus?) {
        _ticketSelectionState.value = _ticketSelectionState.value.copy(statusFilter = status)
    }

    fun setTicketSearchQuery(query: String) {
        _ticketSelectionState.value = _ticketSelectionState.value.copy(searchQuery = query)
    }

    fun setTermsAccepted(accepted: Boolean) {
        _termsAccepted.value = accepted
    }

    fun completeBooking(paymentMethod: String) {
        val cid = _selectedCampaignId.value ?: return
        val tickets = _ticketSelectionState.value.selectedTicketNumbers.toList()
        if (tickets.isEmpty()) {
            showToast("Please select at least one ticket / કૃપા કરીને ટિકિટ પસંદ કરો")
            return
        }
        if (!_termsAccepted.value) {
            showToast("Please accept statutory terms and eligibility / કૃપા કરીને શરતો સ્વીકારો")
            return
        }

        viewModelScope.launch {
            val booking = repository.completeBooking(cid, tickets, paymentMethod)
            if (booking != null) {
                _ticketSelectionState.value = TicketSelectionState()
                _termsAccepted.value = false
                showToast(getString("booking_success"))
                navigateTo(Screen.MY_TICKETS)
            } else {
                showToast("Failed to book tickets. Please try again.")
            }
        }
    }

    fun verifyTicket(numberStr: String) {
        val num = numberStr.trim().toIntOrNull()
        if (num == null) {
            showToast("Please enter a valid ticket number")
            return
        }
        _verifyTicketState.value = _verifyTicketState.value.copy(queryNumber = numberStr, isChecking = true)

        viewModelScope.launch {
            delay(600) // Brief realistic animation
            var foundWinner: WinningTicket? = null
            for (res in drawResults.value) {
                val match = res.winningTickets.find { it.ticketNumber == num }
                if (match != null) {
                    foundWinner = match
                    break
                }
            }
            _verifyTicketState.value = _verifyTicketState.value.copy(
                isChecking = false,
                checkedResult = foundWinner,
                hasChecked = true
            )
        }
    }

    fun triggerLiveDrawSimulation(campaignId: String) {
        if (_liveDrawSimulationState.value.isRunning) return

        viewModelScope.launch {
            _liveDrawSimulationState.value = LiveDrawSimulationState(isRunning = true, currentNumberDisplay = "1042")
            // Animate spinning numbers
            val randomNumbers = listOf("1088", "1012", "1054", "1029", "1071", "1033", "1095", "1018", "1042")
            for (rnd in randomNumbers) {
                _liveDrawSimulationState.value = _liveDrawSimulationState.value.copy(currentNumberDisplay = rnd)
                delay(150)
            }
            val result = repository.executeTransparentDraw(campaignId)
            _liveDrawSimulationState.value = LiveDrawSimulationState(
                isRunning = false,
                currentNumberDisplay = result?.winningTickets?.firstOrNull()?.ticketNumber?.toString() ?: "1042",
                completedResult = result
            )
            showToast("Transparent Draw Executed & Published! / લકી ડ્રો સફળતાપૂર્વક સંપન્ન!")
        }
    }

    fun updateCampaignStatus(campaignId: String, newStatus: CampaignStatus) {
        repository.updateCampaignStatus(campaignId, newStatus)
        showToast("Campaign status updated to ${newStatus.name}")
    }

    fun switchUserRole(role: UserRole) {
        repository.switchUserRole(role)
        showToast("Switched active role to ${role.name}")
    }

    fun loginDemoUser() {
        repository.loginDemoUser()
        navigateTo(Screen.HOME)
    }

    fun loginAdmin(role: UserRole) {
        repository.loginAdmin(role)
        navigateTo(Screen.ADMIN_PORTAL)
    }

    fun logout() {
        repository.logout()
        navigateTo(Screen.AUTH)
    }

    fun exportCsv(): String {
        val csv = repository.exportCsvData()
        showToast("CSV Exported successfully (Records verified) / ડેટા એક્સપોર્ટ પૂર્ણ")
        return csv
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }
}
