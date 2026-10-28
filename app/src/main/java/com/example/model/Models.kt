package com.example.model

import com.example.localization.AppLanguage

enum class UserRole {
    CUSTOMER,
    ADMIN,
    COMPLIANCE_OFFICER,
    AUDITOR
}

data class AppUser(
    val id: String,
    val name: String,
    val phone: String,
    val email: String,
    val state: String = "Gujarat",
    val role: UserRole = UserRole.CUSTOMER,
    val preferredLanguage: AppLanguage = AppLanguage.GUJARATI,
    val isKycVerified: Boolean = true,
    val consentAuditLogs: Boolean = true,
    val isSuspended: Boolean = false,
    val maxTicketLimit: Int = 10,
    val registeredAt: Long = System.currentTimeMillis(),
    val totalDonationInr: Long = 0,
    val bookedTicketsCount: Int = 0
)

enum class CampaignStatus {
    DRAFT,
    REVIEW,
    APPROVED,
    ACTIVE,
    PAUSED,
    CLOSED
}

data class Prize(
    val rank: Int,
    val title: String,
    val titleGu: String,
    val titleHi: String,
    val description: String,
    val valuationInr: Long,
    val quantity: Int = 1,
    val isClaimed: Boolean = false
)

data class Campaign(
    val id: String,
    val code: String,
    val title: String,
    val titleGu: String,
    val titleHi: String,
    val causeDescription: String,
    val causeDescriptionGu: String,
    val causeDescriptionHi: String,
    val ticketPriceInr: Long,
    val totalTickets: Int,
    val soldTickets: Int,
    val reservedTickets: Int,
    val drawDate: String,
    val status: CampaignStatus,
    val grandPrizeName: String,
    val grandPrizeNameGu: String,
    val grandPrizeNameHi: String,
    val prizes: List<Prize>,
    val statutoryTrustRegNo: String = "E/9482/AHMEDABAD",
    val complianceApprovalNumber: String = "DF-LEGAL-2026-081",
    val termsUrl: String = "https://deeporam.org/terms-charity-draw",
    val bannerDrawableRes: String = "charity_banner"
)

enum class TicketStatus {
    AVAILABLE,
    RESERVED,
    BOOKED
}

data class DrawTicket(
    val ticketNumber: Int,
    val formattedNumber: String,
    val campaignId: String,
    val status: TicketStatus,
    val reservedByUserId: String? = null,
    val bookedByUserId: String? = null,
    val reservationExpiresAt: Long? = null
)

enum class PaymentStatus {
    PENDING,
    VERIFIED_DEMO,
    COMPLETED,
    FAILED,
    REFUNDED
}

data class Booking(
    val id: String,
    val campaignId: String,
    val campaignTitle: String,
    val userId: String,
    val userName: String,
    val userPhone: String,
    val ticketNumbers: List<Int>,
    val totalAmountInr: Long,
    val paymentStatus: PaymentStatus,
    val transactionRef: String,
    val isDemoMode: Boolean = true,
    val bookedAt: Long = System.currentTimeMillis(),
    val qrToken: String,
    val tax80GCertificateNumber: String
)

data class WinningTicket(
    val rank: Int,
    val prizeName: String,
    val ticketNumber: Int,
    val winnerNameMasked: String,
    val winnerCity: String,
    val verificationHash: String
)

data class DrawResult(
    val drawId: String,
    val campaignId: String,
    val campaignTitle: String,
    val drawDate: String,
    val executedAt: Long,
    val winningTickets: List<WinningTicket>,
    val independentAuditorName: String,
    val auditorFirm: String,
    val entropyProofSha256: String,
    val isPubliclyPublished: Boolean = true
)

data class AuditLog(
    val id: String,
    val timestamp: Long = System.currentTimeMillis(),
    val actorEmail: String,
    val actorRole: String,
    val action: String,
    val entityType: String,
    val entityId: String,
    val sha256Checksum: String,
    val details: String
)

data class ComplianceGate(
    val campaignId: String,
    val legalCounsel: String,
    val barRegistrationNo: String,
    val statutoryApprovalDate: String,
    val allowedStates: List<String> = listOf("Gujarat", "Maharashtra", "Rajasthan", "Madhya Pradesh", "All India Eligible"),
    val minAgeRequirement: Int = 18,
    val is80GEligible: Boolean = true,
    val isFullyApproved: Boolean = true,
    val legalNotes: String
)
