# દીપોરામ ફાઉન્ડેશન — Deeporam Foundation Draw Ticket Booking

Production-oriented architecture for **Deeporam Foundation (દીપોરામ ફાઉન્ડેશન)**: a transparent, legal, and charitable draw ticket booking platform built with native Android Jetpack Compose, Firebase Cloud Firestore, TypeScript Cloud Functions, and an Administrative Compliance Control Portal.

---

## 🏛️ Foundation Information
- **Trust Name**: Deeporam Charitable Foundation (દીપોરામ ચેરિટેબલ ફાઉન્ડેશન)
- **Trust Registration**: `E/9482/AHMEDABAD` (Bombay Public Trusts Act 1950, Gujarat Amendment)
- **Legal Compliance Approval**: `DF-LEGAL-2026-081`
- **Tax Exemption**: Registered under Section 80G of the Indian Income Tax Act (50% Tax Exemption on contributions)
- **Legal Counsel**: Advocate Hasmukh M. Dave (Bar Council of Gujarat: BCG-GJ-94812)
- **Independent Auditor**: CA Nilesh Sanghavi & Associates (Chartered Accountants & Certified Trust Auditors)

---

## 🎨 Branding & Visual Identity
- **Primary Palette**: Premium Navy Blue (`#0B192C` / `#1E3E62`), Rich Metallic Gold (`#FFD700` / `#E5A93C` / `#D4AF37`), Crisp White, and Amber Flame (`#FF9F1C`).
- **Motif**: The Diya (lamp of hope and service) integrated with the golden raffle ticket emblem.
- **Design Guidelines**: Material Design 3, fluid adaptive layouts, high contrast accessibility, minimum 48dp touch targets, semantic tokens.

---

## 🌐 Multilingual Localization Architecture
Supports Gujarati (ગુજરાતી), Hindi (हिंदी), and English (English):
- Full dictionary covering navigation, headers, labels, campaigns, legal compliance gates, ticket status, prizes, empty states, and errors (`AppStrings.kt`).
- Dynamic real-time switching without app restart via `DeeporamTopBar` or `LanguageSelectorDialog`.
- Local language preference persistence.

---

## 📱 Customer App Features
1. **Splash Screen & Language Selector**: Branded Diya logo, foundation motto ("આશાનો દીવો પ્રગટાવો, જીવન ઉજાળો"), and quick language switch.
2. **Mobile OTP Registration & Login**: 10-digit mobile input with simulated 6-digit OTP verification + 1-tap quick Demo Customer login.
3. **Home Dashboard & Campaign Discovery**: Hero charity banner, impact metrics (total tickets, 80G validity, 100% audit check), active campaigns with live progress bar.
4. **Campaign Detail & Prize Gallery**: High-resolution prize showcase (Grand Prize: Luxury Electric SUV ₹25,00,000, 100g 24K Gold Coin, Pure Gold Bullion, Agro Drones, Student Scholarships).
5. **Interactive Ticket Number Selection**:
   - 4-column grid with live status indicators (Available, Reserved, Booked, Selected).
   - Instant 10-minute atomic hold lock.
   - Quick "Lucky Pick" randomizer (Random 1 or Random 3 lucky numbers).
   - Real-time ticket search filter.
6. **Booking Summary & Checkout**:
   - Ticket numbers, donation amount breakdown, Section 80G tax benefit calculation.
   - Legal Safety Gate terms & age (18+) verification checkbox.
7. **Legal Safety Gate Notice**:
   - Explicit disclaimer that real-money payments remain disabled until formal statutory authorizations are active; simulated demo payment is utilized.
8. **My Tickets & Digital Passes**:
   - Ticket pass cards with unique QR code token, Section 80G certificate number, draw countdown, and share/download actions.
9. **Official Draw Results & Live Simulator**:
   - Real-time animated number spinner simulating transparent CSPRNG live draw.
   - "Verify Your Ticket" tool to check winning status against official draws with cryptographic proof hash (SHA-256).
10. **Customer Support & Grievance Desk**:
    - Helpline numbers, Ahmedabad office address, email, expandable FAQs, and support ticket submission form.
11. **Profile, Consents & Privacy**:
    - Donor KYC status, SMS alerts toggle, cryptographic audit log consent, Section 80G receipts, and account deletion request with grace period.

---

## 🛡️ Administrative & Compliance Features
Accessible via the Role Menu in `DeeporamTopBar`:
1. **Role-Based Access Control**:
   - Administrator (Trustee)
   - Compliance Officer (Legal Counsel)
   - Independent Auditor (Chartered Accountant)
2. **Management Dashboard & Analytics**:
   - Total tickets sold, collection amounts, donor velocity.
3. **Campaign Lifecycle CRUD**:
   - State machine: `DRAFT` ➔ `REVIEW` ➔ `APPROVED` ➔ `ACTIVE` ➔ `PAUSED` ➔ `CLOSED`.
4. **Legal Safety Gate Sign-Off**:
   - Documented legal review records, bar registration details, geographic restrictions (Gujarat & eligible Indian states), and statutory permit status.
5. **Draw Scheduler & RNG Execution**:
   - Cryptographically Secure Pseudo-Random Number Generator (CSPRNG).
   - Immutable audit trail recording every state transition with SHA-256 checksums.
6. **CSV Export Engine**:
   - Export full tickets, bookings, and audit records with permission checks.

---

## 📂 Project Structure
```
├── app/
│   ├── src/main/java/com/example/
│   │   ├── MainActivity.kt
│   │   ├── localization/
│   │   │   └── AppStrings.kt
│   │   ├── model/
│   │   │   └── Models.kt
│   │   ├── data/
│   │   │   └── DeeporamRepository.kt
│   │   ├── ui/
│   │   │   ├── DeeporamViewModel.kt
│   │   │   ├── components/
│   │   │   │   └── DeeporamComponents.kt
│   │   │   ├── screens/
│   │   │   │   ├── AuthAndSplashScreen.kt
│   │   │   │   ├── HomeAndDetailScreens.kt
│   │   │   │   ├── TicketSelectionAndCheckoutScreens.kt
│   │   │   │   ├── MyTicketsAndResultsScreens.kt
│   │   │   │   ├── SupportAndProfileScreens.kt
│   │   │   │   └── AdminPortalScreen.kt
│   │   │   └── theme/
│   │   │       ├── Color.kt
│   │   │       ├── Theme.kt
│   │   │       └── Type.kt
│   │   └── res/
│   │       ├── drawable/
│   │       │   ├── ic_launcher_background.xml
│   │       │   ├── ic_launcher_foreground.xml
│   │       │   ├── charity_banner.jpg
│   │       │   ├── prize_showcase.jpg
│   │       │   └── deeporam_logo.jpg
│   │       └── values/
│   │           └── strings.xml
├── firestore.rules
├── firebase-blueprint.json
├── functions/
│   ├── package.json
│   ├── tsconfig.json
│   └── src/
│       └── index.ts
└── README.md
```

---

## 🚀 Build & Compilation Instructions
To compile and test the Android application:
```bash
gradle :app:assembleDebug
gradle :app:testDebugUnitTest
```
