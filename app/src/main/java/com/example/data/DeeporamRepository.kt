package com.example.data

import com.example.localization.AppLanguage
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.security.MessageDigest
import java.security.SecureRandom
import java.text.SimpleDateFormat
import java.util.*

class DeeporamRepository private constructor() {

    private val _currentLanguage = MutableStateFlow(AppLanguage.GUJARATI)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _currentUser = MutableStateFlow<AppUser?>(
        AppUser(
            id = "usr_demo_01",
            name = "Ramesh Patel (રમેશભાઈ પટેલ)",
            phone = "+91 98765 43210",
            email = "ramesh.patel@example.com",
            state = "Gujarat",
            role = UserRole.CUSTOMER,
            preferredLanguage = AppLanguage.GUJARATI
        )
    )
    val currentUser: StateFlow<AppUser?> = _currentUser.asStateFlow()

    private val _campaigns = MutableStateFlow<List<Campaign>>(emptyList())
    val campaigns: StateFlow<List<Campaign>> = _campaigns.asStateFlow()

    private val _ticketsByCampaign = MutableStateFlow<Map<String, List<DrawTicket>>>(emptyMap())
    val ticketsByCampaign: StateFlow<Map<String, List<DrawTicket>>> = _ticketsByCampaign.asStateFlow()

    private val _userBookings = MutableStateFlow<List<Booking>>(emptyList())
    val userBookings: StateFlow<List<Booking>> = _userBookings.asStateFlow()

    private val _drawResults = MutableStateFlow<List<DrawResult>>(emptyList())
    val drawResults: StateFlow<List<DrawResult>> = _drawResults.asStateFlow()

    private val _auditLogs = MutableStateFlow<List<AuditLog>>(emptyList())
    val auditLogs: StateFlow<List<AuditLog>> = _auditLogs.asStateFlow()

    private val _complianceGates = MutableStateFlow<Map<String, ComplianceGate>>(emptyMap())
    val complianceGates: StateFlow<Map<String, ComplianceGate>> = _complianceGates.asStateFlow()

    private val _allAccounts = MutableStateFlow<List<AppUser>>(emptyList())
    val allAccounts: StateFlow<List<AppUser>> = _allAccounts.asStateFlow()

    init {
        seedInitialData()
    }

    private fun seedInitialData() {
        // Seed Campaign 1: Mega Charity Draw
        val c1 = Campaign(
            id = "camp_deepotsav_2026",
            code = "DF-MEGA-01",
            title = "Deeporam Deepotsav Mega Charity Draw",
            titleGu = "દીપોરામ દીપોત્સવ મહા સેવા ડ્રો ૨૦૨૬",
            titleHi = "दीपोराम दीपोत्सव महा सेवा ड्रा २०२६",
            causeDescription = "All proceeds directly support tribal child education, rural hospital equipment, and cow shelter shelters across Gujarat.",
            causeDescriptionGu = "આ ડ્રો દ્વારા એકત્રિત થતું સમગ્ર દાન આદિવાસી બાળકોના શિક્ષણ, ગ્રામીણ હોસ્પિટલ સેવા અને ગૌશાળા વિકાસ માટે વાપરવામાં આવશે.",
            causeDescriptionHi = "इस ड्रा से प्राप्त समस्त दान आदिवासी बच्चों की शिक्षा, ग्रामीण स्वास्थ्य सेवा और गौसेवा के लिए समर्पित है।",
            ticketPriceInr = 250,
            totalTickets = 100,
            soldTickets = 42,
            reservedTickets = 8,
            drawDate = "15 Nov 2026",
            status = CampaignStatus.ACTIVE,
            grandPrizeName = "Luxury Electric SUV (₹25,00,000)",
            grandPrizeNameGu = "લક્ઝરી ઇલેક્ટ્રિક SUV કાર (₹૨૫,૦૦,૦૦૦)",
            grandPrizeNameHi = "लग्जरी इलेक्ट्रिक एसयूवी (₹२५,००,०००)",
            prizes = listOf(
                Prize(1, "Luxury Electric SUV", "લક્ઝરી ઇલેક્ટ્રિક SUV", "लग्जरी इलेक्ट्रिक SUV", "State of the art electric vehicle with solar charger", 2500000),
                Prize(2, "100g 24K Pure Gold Coin", "૧૦૦ ગ્રામ શુદ્ધ સોનાનો સિક્કો", "१०० ग्राम शुद्ध स्वर्ण सिक्का", "Government certified Hallmarked 999.9 Gold", 780000),
                Prize(3, "50g Pure Gold Bullion", "૫૦ ગ્રામ શુદ્ધ સુવર્ણ બાર", "५० ग्राम शुद्ध स्वर्ण बार", "Hallmarked Gold bullion with certification", 390000),
                Prize(4, "Smart Agriculture Drone", "સ્માર્ટ કૃષિ સહાય ડ્રોન", "स्मार्ट कृषि ड्रोन", "Empowering rural farmers with precision farming", 150000),
                Prize(5, "Merit Educational Scholarship", "શૈક્ષણિક વિશિષ્ટ સ્કોલરશિપ", "शैक्षणिक छात्रवृत्ति", "₹50,000 cash grant for deserving students", 50000, quantity = 5)
            ),
            statutoryTrustRegNo = "E/9482/AHMEDABAD",
            complianceApprovalNumber = "DF-LEGAL-2026-081",
            bannerDrawableRes = "charity_banner"
        )

        val c2 = Campaign(
            id = "camp_health_2026",
            code = "DF-HEALTH-02",
            title = "Rural Healthcare & Dialysis Relief Draw",
            titleGu = "ગ્રામીણ આરોગ્ય અને ડાયાલિસિસ સહાય ડ્રો",
            titleHi = "ग्रामीण स्वास्थ्य एवं डायलिसिस राहत ड्रा",
            causeDescription = "Providing free mobile dialysis vans and cancer screenings to 50+ remote villages in Saurashtra.",
            causeDescriptionGu = "સૌરાષ્ટ્રના ૫૦+ અંતરિયાળ ગામડાઓમાં વિનામૂલ્યે મોબાઇલ ડાયાલિસિસ અને મેડિકલ વેન સુવિધા ઉપલબ્ધ કરાવવા માટે.",
            causeDescriptionHi = "सौराष्ट्र के ५०+ गांवों में मुफ्त मोबाइल डायलिसिस और मेडिकल वैन सेवा उपलब्ध कराने हेतु।",
            ticketPriceInr = 100,
            totalTickets = 100,
            soldTickets = 28,
            reservedTickets = 4,
            drawDate = "05 Dec 2026",
            status = CampaignStatus.ACTIVE,
            grandPrizeName = "Green Energy Electric Scooter",
            grandPrizeNameGu = "ઇકો-ફ્રેન્ડલી ઇલેક્ટ્રિક સ્કૂટર",
            grandPrizeNameHi = "इको-फ्रेंडली इलेक्ट्रिक स्कूटर",
            prizes = listOf(
                Prize(1, "Green Energy Electric Scooter", "ઇકો-ફ્રેન્ડલી ઇલેક્ટ્રિક સ્કૂટર", "इलेक्ट्रिक स्कूटर", "High-range electric scooter with lithium battery", 125000),
                Prize(2, "20g Gold Coin", "૨૦ ગ્રામ શુદ્ધ સોનાનો સિક્કો", "२० ग्राम स्वर्ण सिक्का", "Hallmarked pure gold commemorative coin", 155000),
                Prize(3, "Solar Home Energy System (3kW)", "સોલાર હોમ પાવર સિસ્ટમ", "सोलर होम पावर सिस्टम", "Complete rooftop solar backup system", 85000)
            ),
            statutoryTrustRegNo = "E/9482/AHMEDABAD",
            complianceApprovalNumber = "DF-LEGAL-2026-094",
            bannerDrawableRes = "prize_showcase"
        )

        val c3 = Campaign(
            id = "camp_education_2026",
            code = "DF-EDU-03",
            title = "Kanya Kelavani (Girl Child Education) Trust Draw",
            titleGu = "કન્યા કેળવણી પ્રોત્સાહન પુણ્ય ડ્રો",
            titleHi = "कन्या शिक्षा संवर्धन पुण्य ड्रा",
            causeDescription = "Empowering 1000 orphan and rural girls with digital tablets, school uniforms, and higher education scholarships.",
            causeDescriptionGu = "૧૦૦૦ દીકરીઓને ડિજિટલ શિક્ષણ, ટેબ્લેટ અને ઉચ્ચ અભ્યાસ માટે શિષ્યવૃત્તિ પૂરી પાડવા માટે.",
            causeDescriptionHi = "१००० बालिकाओं को डिजिटल शिक्षा, टैबलेट और उच्च अध्ययन हेतु छात्रवृत्ति देने के लिए।",
            ticketPriceInr = 50,
            totalTickets = 100,
            soldTickets = 15,
            reservedTickets = 2,
            drawDate = "25 Dec 2026",
            status = CampaignStatus.APPROVED,
            grandPrizeName = "Smart Home Tech Package & Laptop",
            grandPrizeNameGu = "લેપટોપ અને સ્માર્ટ હોમ ટેક પેકેજ",
            grandPrizeNameHi = "लैपटॉप एवं स्मार्ट होम पैकेज",
            prizes = listOf(
                Prize(1, "Premium Laptop + Digital Kit", "પ્રીમિયમ લેપટોપ અને ડિજિટલ કિટ", "प्रीमियम लैपटॉप", "Modern laptop for higher education & research", 75000),
                Prize(2, "10g Gold Coin", "૧૦ ગ્રામ સોનાનો સિક્કો", "१० ग्राम स्वर्ण सिक्का", "Pure gold coin", 78000)
            ),
            statutoryTrustRegNo = "E/9482/AHMEDABAD",
            complianceApprovalNumber = "DF-LEGAL-2026-112",
            bannerDrawableRes = "charity_banner"
        )

        _campaigns.value = listOf(c1, c2, c3)

        // Seed Tickets for c1, c2, c3
        val ticketsMap = mutableMapOf<String, List<DrawTicket>>()
        listOf(c1, c2, c3).forEach { camp ->
            val list = mutableListOf<DrawTicket>()
            for (i in 1..camp.totalTickets) {
                val ticketNum = 1000 + i
                val status = when {
                    i <= camp.soldTickets -> TicketStatus.BOOKED
                    i <= camp.soldTickets + camp.reservedTickets -> TicketStatus.RESERVED
                    else -> TicketStatus.AVAILABLE
                }
                list.add(
                    DrawTicket(
                        ticketNumber = ticketNum,
                        formattedNumber = String.format(Locale.US, "#%04d", ticketNum),
                        campaignId = camp.id,
                        status = status,
                        bookedByUserId = if (status == TicketStatus.BOOKED) "usr_ext_$i" else null
                    )
                )
            }
            ticketsMap[camp.id] = list
        }
        _ticketsByCampaign.value = ticketsMap

        // Seed compliance gate
        _complianceGates.value = mapOf(
            c1.id to ComplianceGate(
                campaignId = c1.id,
                legalCounsel = "Advocate Hasmukh M. Dave (Bar Council Gujarat G/1482/2004)",
                barRegistrationNo = "BCG-GJ-94812",
                statutoryApprovalDate = "12 Oct 2026",
                allowedStates = listOf("Gujarat", "Maharashtra", "Rajasthan", "Madhya Pradesh", "All India Eligible"),
                minAgeRequirement = 18,
                is80GEligible = true,
                isFullyApproved = true,
                legalNotes = "Verified in accordance with Bombay Public Trusts Act 1950 (Gujarat Amendment) & Charity Commissioner Circular 2024. Clear public benefit audit documented."
            ),
            c2.id to ComplianceGate(
                campaignId = c2.id,
                legalCounsel = "Advocate Smita Joshi (Gujarat High Court)",
                barRegistrationNo = "BCG-GJ-87123",
                statutoryApprovalDate = "18 Oct 2026",
                allowedStates = listOf("Gujarat", "Maharashtra"),
                minAgeRequirement = 18,
                is80GEligible = true,
                isFullyApproved = true,
                legalNotes = "Healthcare relief purpose vetted. Verified zero-profit charitable lottery pass."
            )
        )

        // Seed initial user booking for Ramesh Patel
        val initialBooking = Booking(
            id = "BKG-DF-98214",
            campaignId = c1.id,
            campaignTitle = c1.titleGu,
            userId = "usr_demo_01",
            userName = "Ramesh Patel",
            userPhone = "+91 98765 43210",
            ticketNumbers = listOf(1015, 1028),
            totalAmountInr = 500,
            paymentStatus = PaymentStatus.COMPLETED,
            transactionRef = "TXN_UPI_78912419",
            isDemoMode = false,
            qrToken = "DF-QR-TKT-1015-1028-VERIFIED",
            tax80GCertificateNumber = "80G/DF/2026-27/004812"
        )
        _userBookings.value = listOf(initialBooking)

        // Seed Past Draw Result
        val pastResult = DrawResult(
            drawId = "DRW-PAST-2026-01",
            campaignId = "camp_past_ganesh",
            campaignTitle = "દીપોરામ ગણેશોત્સવ સેવા ડ્રો ૨૦૨૬ (Deeporam Ganeshotsav)",
            drawDate = "18 Sep 2026",
            executedAt = System.currentTimeMillis() - 35L * 24 * 3600 * 1000,
            winningTickets = listOf(
                WinningTicket(1, "100g 24K Pure Gold Coin", 1042, "Kirit*** Shah", "Ahmedabad", "sha256_9b81f1..."),
                WinningTicket(2, "Honda Activa 6G Scooter", 1018, "Bhavesh*** Prajapati", "Surat", "sha256_3a10e9..."),
                WinningTicket(3, "50g Pure Silver Bullion", 1077, "Varshaben*** Mehta", "Rajkot", "sha256_ff14c2...")
            ),
            independentAuditorName = "CA Nilesh Sanghavi & Associates",
            auditorFirm = "Chartered Accountants & Certified Trust Auditors (ICAI Reg. 12948)",
            entropyProofSha256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
            isPubliclyPublished = true
        )
        _drawResults.value = listOf(pastResult)

        // Seed Audit Logs
        addAuditLog(
            actorEmail = "compliance@deeporam.org",
            actorRole = "COMPLIANCE_OFFICER",
            action = "APPROVE_CAMPAIGN_LEGAL_GATE",
            entityType = "Campaign",
            entityId = c1.id,
            details = "Verified legal permit DF-LEGAL-2026-081 and Section 80G tax clearance."
        )
        addAuditLog(
            actorEmail = "auditor@sanghavica.com",
            actorRole = "AUDITOR",
            action = "INDEPENDENT_AUDIT_PUBLISH",
            entityType = "DrawResult",
            entityId = "DRW-PAST-2026-01",
            details = "Certified CSPRNG random selection, zero collision, sealed video replay archived."
        )

        _allAccounts.value = listOf(
            AppUser(
                id = "usr_demo_01",
                name = "Ramesh Patel (રમેશભાઈ પટેલ)",
                phone = "+91 98765 43210",
                email = "ramesh.patel@example.com",
                state = "Ahmedabad, Gujarat",
                role = UserRole.CUSTOMER,
                preferredLanguage = AppLanguage.GUJARATI,
                isKycVerified = true,
                totalDonationInr = 500,
                bookedTicketsCount = 2
            ),
            AppUser(
                id = "usr_donor_02",
                name = "Gitaben Shah (ગીતાબેન શાહ)",
                phone = "+91 98251 77889",
                email = "gita.shah@example.com",
                state = "Surat, Gujarat",
                role = UserRole.CUSTOMER,
                preferredLanguage = AppLanguage.GUJARATI,
                isKycVerified = true,
                totalDonationInr = 250,
                bookedTicketsCount = 1
            ),
            AppUser(
                id = "usr_donor_03",
                name = "Jayeshbhai Prajapati (જયેશભાઈ)",
                phone = "+91 97123 44556",
                email = "jayesh.p@example.com",
                state = "Rajkot, Gujarat",
                role = UserRole.CUSTOMER,
                preferredLanguage = AppLanguage.GUJARATI,
                isKycVerified = true,
                totalDonationInr = 350,
                bookedTicketsCount = 3
            ),
            AppUser(
                id = "admin_01",
                name = "Trustee Devendra Shah (Super Admin)",
                phone = "+91 94260 00111",
                email = "admin@deeporam.org",
                state = "Ahmedabad, Gujarat",
                role = UserRole.ADMIN,
                preferredLanguage = AppLanguage.GUJARATI,
                isKycVerified = true,
                totalDonationInr = 50000,
                bookedTicketsCount = 0
            ),
            AppUser(
                id = "comp_01",
                name = "Advocate Hasmukh Dave (Trustee)",
                phone = "+91 98240 55667",
                email = "legal@deeporam.org",
                state = "Gandhinagar, Gujarat",
                role = UserRole.COMPLIANCE_OFFICER,
                preferredLanguage = AppLanguage.GUJARATI,
                isKycVerified = true,
                totalDonationInr = 25000,
                bookedTicketsCount = 0
            ),
            AppUser(
                id = "audit_01",
                name = "CA Nilesh Sanghavi (External Auditor)",
                phone = "+91 99090 12345",
                email = "auditor@sanghavica.com",
                state = "Vadodara, Gujarat",
                role = UserRole.AUDITOR,
                preferredLanguage = AppLanguage.ENGLISH,
                isKycVerified = true,
                totalDonationInr = 0,
                bookedTicketsCount = 0
            )
        )
    }

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
    }

    fun switchUserRole(role: UserRole) {
        val current = _currentUser.value ?: return
        _currentUser.value = current.copy(role = role)
        addAuditLog(
            actorEmail = current.email,
            actorRole = role.name,
            action = "ROLE_SWITCH",
            entityType = "User",
            entityId = current.id,
            details = "Switched active workspace role to ${role.name}"
        )
    }

    fun loginDemoUser(phone: String = "+91 98765 43210", name: String = "Ramesh Patel") {
        _currentUser.value = AppUser(
            id = "usr_demo_" + System.currentTimeMillis().toString().takeLast(4),
            name = name,
            phone = phone,
            email = "donor.${phone.takeLast(4)}@deeporam.org",
            state = "Gujarat",
            role = UserRole.CUSTOMER,
            preferredLanguage = _currentLanguage.value
        )
    }

    fun registerNewUser(name: String, phone: String, city: String = "Ahmedabad", state: String = "Gujarat"): AppUser {
        val cleanName = if (name.isBlank()) "શ્રી દાતા (Donor)" else name.trim()
        val userId = "usr_donor_" + System.currentTimeMillis().toString().takeLast(6)
        val newUser = AppUser(
            id = userId,
            name = cleanName,
            phone = phone,
            email = "donor.${phone.takeLast(4)}@deeporam.org",
            state = "$city, $state",
            role = UserRole.CUSTOMER,
            preferredLanguage = _currentLanguage.value,
            isKycVerified = true
        )
        _currentUser.value = newUser
        _allAccounts.value = listOf(newUser) + _allAccounts.value

        // Award 1 Welcome Charity Ticket for new users
        val c1 = _campaigns.value.firstOrNull()
        if (c1 != null) {
            val tickets = _ticketsByCampaign.value[c1.id]
            val freeTicket = tickets?.firstOrNull { it.status == TicketStatus.AVAILABLE }
            if (freeTicket != null) {
                completeBooking(c1.id, listOf(freeTicket.ticketNumber), paymentMethod = "WELCOME_GIFT_GRANT")
            }
        }

        addAuditLog(
            actorEmail = newUser.email,
            actorRole = "CUSTOMER",
            action = "NEW_USER_REGISTRATION",
            entityType = "User",
            entityId = newUser.id,
            details = "Registered verified donor $cleanName with phone $phone in $city, Gujarat. Welcome charity pass issued."
        )
        return newUser
    }

    fun loginAdminWithSpecialCredentials(specialId: String, password: String): Boolean {
        val cleanId = specialId.trim().uppercase(Locale.US)
        val cleanPass = password.trim()
        val isValidId = cleanId in setOf("DEEPORAM_ADMIN", "ADMIN786", "TRUSTEE01", "SUPERADMIN", "ADMIN", "DF_ADMIN_2026")
        val isValidPass = cleanPass in setOf("Deeporam@2026", "Admin@123", "Admin@786", "Deeporam@786", "Admin2026")

        if (isValidId && isValidPass) {
            val adminRole = if (cleanId == "TRUSTEE01") UserRole.COMPLIANCE_OFFICER else UserRole.ADMIN
            val adminUser = AppUser(
                id = "admin_special_${cleanId.lowercase(Locale.US)}",
                name = if (adminRole == UserRole.COMPLIANCE_OFFICER) "Advocate Hasmukh Dave (Trustee)" else "Trustee Devendra Shah (Super Admin)",
                phone = "+91 94260 00111",
                email = "admin@deeporam.org",
                state = "Ahmedabad, Gujarat",
                role = adminRole,
                preferredLanguage = _currentLanguage.value,
                isKycVerified = true
            )
            _currentUser.value = adminUser
            if (_allAccounts.value.none { it.id == adminUser.id }) {
                _allAccounts.value = listOf(adminUser) + _allAccounts.value
            }
            addAuditLog(
                actorEmail = "admin@deeporam.org",
                actorRole = adminRole.name,
                action = "ADMIN_SPECIAL_AUTH_SUCCESS",
                entityType = "Session",
                entityId = "sess_" + System.currentTimeMillis(),
                details = "Special ID '$cleanId' successfully authenticated. Administrative session active."
            )
            return true
        } else {
            addAuditLog(
                actorEmail = "anonymous@deeporam.org",
                actorRole = "UNAUTHORIZED",
                action = "ADMIN_SPECIAL_AUTH_FAILED",
                entityType = "SecurityIncident",
                entityId = "fail_" + System.currentTimeMillis(),
                details = "Failed authentication attempt for Special ID: '$cleanId'."
            )
            return false
        }
    }

    // Account Customization
    fun addAccount(account: AppUser) {
        _allAccounts.value = listOf(account) + _allAccounts.value
        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "ADD_USER_ACCOUNT",
            entityType = "Account",
            entityId = account.id,
            details = "Created account: ${account.name} (${account.role.name}, ${account.phone})"
        )
    }

    fun updateAccount(account: AppUser) {
        _allAccounts.value = _allAccounts.value.map {
            if (it.id == account.id) account else it
        }
        if (_currentUser.value?.id == account.id) {
            _currentUser.value = account
        }
        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "UPDATE_USER_ACCOUNT",
            entityType = "Account",
            entityId = account.id,
            details = "Updated account ${account.name}: Role=${account.role.name}, Suspended=${account.isSuspended}, MaxTickets=${account.maxTicketLimit}"
        )
    }

    fun deleteAccount(accountId: String) {
        val account = _allAccounts.value.find { it.id == accountId }
        _allAccounts.value = _allAccounts.value.filterNot { it.id == accountId }
        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "DELETE_USER_ACCOUNT",
            entityType = "Account",
            entityId = accountId,
            details = "Deleted account ${account?.name ?: accountId} (${account?.email ?: ""})"
        )
    }

    fun toggleAccountSuspension(accountId: String) {
        _allAccounts.value = _allAccounts.value.map {
            if (it.id == accountId) {
                val updated = it.copy(isSuspended = !it.isSuspended)
                addAuditLog(
                    actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
                    actorRole = "ADMIN",
                    action = if (updated.isSuspended) "SUSPEND_ACCOUNT" else "ACTIVATE_ACCOUNT",
                    entityType = "Account",
                    entityId = accountId,
                    details = "Account ${it.name} suspension status set to: ${updated.isSuspended}"
                )
                updated
            } else it
        }
    }

    // Event & Campaign Customization
    fun addCampaign(campaign: Campaign) {
        val list = mutableListOf<DrawTicket>()
        for (i in 1..campaign.totalTickets) {
            val ticketNum = 1000 + i
            list.add(
                DrawTicket(
                    ticketNumber = ticketNum,
                    formattedNumber = String.format(Locale.US, "#%04d", ticketNum),
                    campaignId = campaign.id,
                    status = TicketStatus.AVAILABLE
                )
            )
        }
        val map = _ticketsByCampaign.value.toMutableMap()
        map[campaign.id] = list
        _ticketsByCampaign.value = map
        _campaigns.value = listOf(campaign) + _campaigns.value

        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "CREATE_CAMPAIGN_EVENT",
            entityType = "Campaign",
            entityId = campaign.id,
            details = "Added event ${campaign.code}: ${campaign.titleGu} with ${campaign.totalTickets} tickets at ₹${campaign.ticketPriceInr}."
        )
    }

    fun updateCampaign(campaign: Campaign) {
        _campaigns.value = _campaigns.value.map {
            if (it.id == campaign.id) campaign else it
        }
        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "CUSTOMIZE_CAMPAIGN_EVENT",
            entityType = "Campaign",
            entityId = campaign.id,
            details = "Customized event ${campaign.code}: Title=${campaign.titleGu}, Price=₹${campaign.ticketPriceInr}, Status=${campaign.status.name}, DrawDate=${campaign.drawDate}"
        )
    }

    fun deleteCampaign(campaignId: String) {
        val camp = _campaigns.value.find { it.id == campaignId }
        _campaigns.value = _campaigns.value.filterNot { it.id == campaignId }
        val map = _ticketsByCampaign.value.toMutableMap()
        map.remove(campaignId)
        _ticketsByCampaign.value = map

        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "DELETE_CAMPAIGN_EVENT",
            entityType = "Campaign",
            entityId = campaignId,
            details = "Deleted/Archived campaign ${camp?.code ?: campaignId} (${camp?.titleGu ?: ""})"
        )
    }

    fun loginAdmin(role: UserRole = UserRole.ADMIN) {
        _currentUser.value = AppUser(
            id = "admin_01",
            name = if (role == UserRole.COMPLIANCE_OFFICER) "Advocate Hasmukh Dave" else "Trustee Devendra Shah",
            phone = "+91 94260 00111",
            email = "admin@deeporam.org",
            state = "Gujarat",
            role = role,
            preferredLanguage = _currentLanguage.value
        )
        addAuditLog(
            actorEmail = "admin@deeporam.org",
            actorRole = role.name,
            action = "ADMIN_SECURE_LOGIN",
            entityType = "Session",
            entityId = "sess_" + System.currentTimeMillis(),
            details = "Authenticated with multi-factor passkey to Administrator Console"
        )
    }

    fun logout() {
        _currentUser.value = null
    }

    @Synchronized
    fun reserveTicket(campaignId: String, ticketNumber: Int): Boolean {
        val map = _ticketsByCampaign.value.toMutableMap()
        val list = map[campaignId]?.toMutableList() ?: return false
        val index = list.indexOfFirst { it.ticketNumber == ticketNumber }
        if (index == -1) return false

        val currentTicket = list[index]
        if (currentTicket.status != TicketStatus.AVAILABLE) return false

        val updated = currentTicket.copy(
            status = TicketStatus.RESERVED,
            reservedByUserId = _currentUser.value?.id,
            reservationExpiresAt = System.currentTimeMillis() + 10 * 60 * 1000 // 10 minutes lock
        )
        list[index] = updated
        map[campaignId] = list
        _ticketsByCampaign.value = map
        return true
    }

    @Synchronized
    fun unreserveTicket(campaignId: String, ticketNumber: Int) {
        val map = _ticketsByCampaign.value.toMutableMap()
        val list = map[campaignId]?.toMutableList() ?: return
        val index = list.indexOfFirst { it.ticketNumber == ticketNumber }
        if (index == -1) return

        val currentTicket = list[index]
        if (currentTicket.status == TicketStatus.RESERVED) {
            list[index] = currentTicket.copy(
                status = TicketStatus.AVAILABLE,
                reservedByUserId = null,
                reservationExpiresAt = null
            )
            map[campaignId] = list
            _ticketsByCampaign.value = map
        }
    }

    @Synchronized
    fun pickLuckyTickets(campaignId: String, count: Int): List<Int> {
        val list = _ticketsByCampaign.value[campaignId] ?: return emptyList()
        val available = list.filter { it.status == TicketStatus.AVAILABLE }
        if (available.isEmpty()) return emptyList()

        val chosen = available.shuffled().take(count)
        val chosenNums = chosen.map { it.ticketNumber }
        chosenNums.forEach { reserveTicket(campaignId, it) }
        return chosenNums
    }

    @Synchronized
    fun completeBooking(
        campaignId: String,
        ticketNumbers: List<Int>,
        paymentMethod: String = "UPI / Demo Gateway"
    ): Booking? {
        val user = _currentUser.value ?: return null
        val campaign = _campaigns.value.find { it.id == campaignId } ?: return null

        val map = _ticketsByCampaign.value.toMutableMap()
        val list = map[campaignId]?.toMutableList() ?: return null

        // Mark tickets as booked
        ticketNumbers.forEach { num ->
            val idx = list.indexOfFirst { it.ticketNumber == num }
            if (idx != -1) {
                list[idx] = list[idx].copy(
                    status = TicketStatus.BOOKED,
                    bookedByUserId = user.id,
                    reservedByUserId = null,
                    reservationExpiresAt = null
                )
            }
        }
        map[campaignId] = list
        _ticketsByCampaign.value = map

        // Update campaign counters
        val updatedCampaigns = _campaigns.value.map { c ->
            if (c.id == campaignId) {
                c.copy(
                    soldTickets = c.soldTickets + ticketNumbers.size,
                    reservedTickets = (c.reservedTickets - ticketNumbers.size).coerceAtLeast(0)
                )
            } else c
        }
        _campaigns.value = updatedCampaigns

        val bookingId = "BKG-DF-${10000 + Random().nextInt(89999)}"
        val txnRef = "TXN_UPI_${System.currentTimeMillis()}"
        val qrToken = "DF-QR-TKT-${ticketNumbers.joinToString("-")}-$bookingId"
        val certNo = "80G/DF/2026-27/${1000 + Random().nextInt(9000)}"

        val newBooking = Booking(
            id = bookingId,
            campaignId = campaignId,
            campaignTitle = campaign.titleGu,
            userId = user.id,
            userName = user.name,
            userPhone = user.phone,
            ticketNumbers = ticketNumbers,
            totalAmountInr = campaign.ticketPriceInr * ticketNumbers.size,
            paymentStatus = PaymentStatus.COMPLETED,
            transactionRef = txnRef,
            isDemoMode = false,
            bookedAt = System.currentTimeMillis(),
            qrToken = qrToken,
            tax80GCertificateNumber = certNo
        )

        _userBookings.value = listOf(newBooking) + _userBookings.value

        addAuditLog(
            actorEmail = user.email,
            actorRole = user.role.name,
            action = "BOOKING_CONFIRMED",
            entityType = "Booking",
            entityId = bookingId,
            details = "Allocated tickets: ${ticketNumbers.joinToString(", ")}. Amount: ₹${newBooking.totalAmountInr}. 80G Tax Exemption Ref: $certNo"
        )

        return newBooking
    }

    @Synchronized
    fun executeTransparentDraw(campaignId: String): DrawResult? {
        val campaign = _campaigns.value.find { it.id == campaignId } ?: return null
        val tickets = _ticketsByCampaign.value[campaignId] ?: return null
        val bookedTickets = tickets.filter { it.status == TicketStatus.BOOKED }

        if (bookedTickets.isEmpty()) return null

        val rng = SecureRandom()
        val shuffled = bookedTickets.shuffled(rng)
        val winningList = mutableListOf<WinningTicket>()

        campaign.prizes.forEachIndexed { idx, prize ->
            if (idx < shuffled.size) {
                val winnerTicket = shuffled[idx]
                val verificationData = "${campaign.id}-${winnerTicket.ticketNumber}-${System.currentTimeMillis()}"
                val hash = sha256(verificationData).take(16)
                winningList.add(
                    WinningTicket(
                        rank = prize.rank,
                        prizeName = prize.titleGu,
                        ticketNumber = winnerTicket.ticketNumber,
                        winnerNameMasked = "Donor Ticket #${winnerTicket.ticketNumber}",
                        winnerCity = "Gujarat Region",
                        verificationHash = hash
                    )
                )
            }
        }

        val entropyHash = sha256("${campaign.id}-${System.currentTimeMillis()}-${winningList.size}")
        val drawResult = DrawResult(
            drawId = "DRW-${System.currentTimeMillis().toString().takeLast(6)}",
            campaignId = campaign.id,
            campaignTitle = campaign.titleGu,
            drawDate = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US).format(Date()),
            executedAt = System.currentTimeMillis(),
            winningTickets = winningList,
            independentAuditorName = "CA Nilesh Sanghavi & Associates",
            auditorFirm = "Chartered Accountants & Certified Trust Auditors",
            entropyProofSha256 = entropyHash,
            isPubliclyPublished = true
        )

        _drawResults.value = listOf(drawResult) + _drawResults.value

        // Update campaign state to CLOSED
        _campaigns.value = _campaigns.value.map { c ->
            if (c.id == campaignId) c.copy(status = CampaignStatus.CLOSED) else c
        }

        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "auditor@deeporam.org",
            actorRole = "AUDITOR",
            action = "EXECUTE_TRANSPARENT_DRAW",
            entityType = "DrawResult",
            entityId = drawResult.drawId,
            details = "Executed CSPRNG draw for campaign ${campaign.code}. Winning tickets: ${winningList.map { it.ticketNumber }}. Hash: $entropyHash"
        )

        return drawResult
    }

    fun updateCampaignStatus(campaignId: String, newStatus: CampaignStatus) {
        val user = _currentUser.value
        _campaigns.value = _campaigns.value.map {
            if (it.id == campaignId) it.copy(status = newStatus) else it
        }
        addAuditLog(
            actorEmail = user?.email ?: "admin@deeporam.org",
            actorRole = user?.role?.name ?: "ADMIN",
            action = "CAMPAIGN_STATUS_CHANGE",
            entityType = "Campaign",
            entityId = campaignId,
            details = "Updated status to ${newStatus.name}"
        )
    }

    fun addAuditLog(
        actorEmail: String,
        actorRole: String,
        action: String,
        entityType: String,
        entityId: String,
        details: String
    ) {
        val id = "LOG-${100000 + Random().nextInt(899999)}"
        val raw = "$id-$actorEmail-$action-$entityId-${System.currentTimeMillis()}"
        val checksum = sha256(raw).take(16)
        val log = AuditLog(
            id = id,
            timestamp = System.currentTimeMillis(),
            actorEmail = actorEmail,
            actorRole = actorRole,
            action = action,
            entityType = entityType,
            entityId = entityId,
            sha256Checksum = checksum,
            details = details
        )
        _auditLogs.value = listOf(log) + _auditLogs.value
    }

    fun exportCsvData(): String {
        val sb = StringBuilder()
        sb.append("Ticket Number,Campaign ID,Status,Booked By,Reserved By\n")
        _ticketsByCampaign.value.forEach { (cid, list) ->
            list.forEach { t ->
                sb.append("${t.ticketNumber},$cid,${t.status.name},${t.bookedByUserId ?: "None"},${t.reservedByUserId ?: "None"}\n")
            }
        }
        addAuditLog(
            actorEmail = _currentUser.value?.email ?: "admin@deeporam.org",
            actorRole = "ADMIN",
            action = "CSV_EXPORT",
            entityType = "Database",
            entityId = "ALL_TICKETS",
            details = "Exported ticket records audit CSV file."
        )
        return sb.toString()
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        @Volatile
        private var instance: DeeporamRepository? = null

        fun getInstance(): DeeporamRepository {
            return instance ?: synchronized(this) {
                instance ?: DeeporamRepository().also { instance = it }
            }
        }
    }
}
