package com.example

import com.example.data.DeeporamRepository
import com.example.localization.AppLanguage
import com.example.localization.AppStrings
import com.example.model.TicketStatus
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun testMultilingualTranslations() {
    val guTitle = AppStrings.get("app_title", AppLanguage.GUJARATI)
    val hiTitle = AppStrings.get("app_title", AppLanguage.HINDI)
    val enTitle = AppStrings.get("app_title", AppLanguage.ENGLISH)

    assertEquals("દીપોરામ ફાઉન્ડેશન", guTitle)
    assertEquals("दीपोराम फाउंडेशन", hiTitle)
    assertEquals("Deeporam Foundation", enTitle)
  }

  @Test
  fun testAtomicTicketReservationAndBooking() {
    val repo = DeeporamRepository.getInstance()
    val campaign = repo.campaigns.value.first()

    // Reserve an available ticket
    val availableTicket = repo.ticketsByCampaign.value[campaign.id]!!.first { it.status == TicketStatus.AVAILABLE }
    val success = repo.reserveTicket(campaign.id, availableTicket.ticketNumber)
    assertTrue("Ticket reservation should succeed", success)

    // Verify booking allocation
    val booking = repo.completeBooking(campaign.id, listOf(availableTicket.ticketNumber))
    assertNotNull("Booking should be created", booking)
    assertEquals(1, booking!!.ticketNumbers.size)
    assertTrue(booking.tax80GCertificateNumber.startsWith("80G/DF/"))
  }

  @Test
  fun testNewUserRegistrationAndWelcomeGift() {
    val repo = DeeporamRepository.getInstance()
    val newUser = repo.registerNewUser("Arjun Solanki", "+91 94260 12345", "Ahmedabad")
    assertEquals("Arjun Solanki", newUser.name)
    assertEquals("+91 94260 12345", newUser.phone)
    assertTrue(newUser.isKycVerified)

    val bookings = repo.userBookings.value
    assertTrue("Should have welcome gift booking", bookings.isNotEmpty())
  }
}
