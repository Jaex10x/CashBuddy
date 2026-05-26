# 🎬 Video Presentation Script — CashBuddy Android App (MVC Architecture)

---

## PART 1: Brief Self-Introduction

> *(On camera, looking natural and professional)*

**"Good day! I am [Your Name], from [Your Section / Class Code]. Today, I will be presenting my Android application project called **CashBuddy** — a personal finance management app built for the Android platform using the **MVC Architecture** pattern.

In this video, I'll walk you through the purpose of the application, its main features, how the system components interact, and finally, a live demonstration of the app in action."**

---

## PART 2: Introduction of the Android Application Project

> *(Switch to screen recording or stay on camera)*

**"So, what is CashBuddy?**

CashBuddy is an Android application designed to help users manage their personal finances in a simple and intuitive way. Think of it as a digital piggy bank combined with a spending tracker — all in one place.

**Who are the intended users?** The app is built for anyone who wants to keep track of their cash — students, young professionals, or anyone looking for a lightweight budgeting tool without the complexity of full-blown finance apps.

**What problem does it solve?** Many people struggle with tracking where their money goes. CashBuddy solves this by giving users a dedicated **Piggy Bank** where they can add and deduct cash, a **Spending List** where they can log what they spent on, and a **Dashboard** that shows their balance at a glance — all stored locally on their device.

**Why did I choose to develop this application?** I wanted to build something practical and relatable — a tool people can actually use in their daily lives. Plus, it gave me the perfect opportunity to implement the **MVC (Model-View-Controller)** architecture in a real Android project."**

---

## PART 3: Main Features of the Application

> *(Screen recording showing each screen)*

**"Let me walk you through the main features of CashBuddy."**

### 1. User Registration & Login
"The app starts with a **Registration screen** where new users create an account by entering a username and password. Once registered, they go to the **Login screen** to sign in. The app remembers the login session using SharedPreferences, so users don't have to log in every time."

### 2. Dashboard — Home Screen
"The **Dashboard** is the home screen. It greets you with a personalized **'Welcome back, [username]!'** message, displays your current **Piggy Bank balance**, and lists your **recent spending items**. This gives users an immediate snapshot of their financial status."

### 3. Piggy Bank
"The **Piggy Bank** is the core feature. Users can:
- **Add cash** to their balance
- **Deduct cash** when they spend — with a dialog asking for description and amount
- **View or hide** their balance with a toggle button
- See a complete **transaction history** with timestamps"

### 4. Spending Tracker (List)
"The **List screen** shows all spending items logged. Users tap the **'Add Spend'** button to log a new expense, entering a description and amount."

### 5. Profile (Personal Details)
"The **Profile screen** lets users view and update their personal details like full name, address, and contact information — all saved locally."

### 6. Settings
"The **Settings screen** provides options to change password, switch between light and dark themes, change language, and view app information."

### 7. Bottom Navigation & Menu
"Every screen has a **bottom navigation bar** with Home, List, Profile, Piggy Bank, and Settings icons — so users can jump between sections instantly. There's also a **menu button** in the top corner with the same options plus a **Log out** feature."

---

## PART 4: System Components and Interaction

> *(Screen recording showing the app flow OR a diagram overlay)*

**"Now let me explain how the different parts of CashBuddy work together."**

### Activities (Screens)
"Each screen in CashBuddy is an **Activity**. We have:
- `LoginActivity` and `RegisterActivity` — for authentication
- `DashboardActivity` — the home screen
- `DashboardListActivity` — the spending list
- `DashboardProfileActivity` — user profile
- `PiggyActivity` — the piggy bank
- `SettingsActivity` — app settings"

### Navigation Flow
"Here's how the user moves through the app:
1. **App launch** → Splash screen checks if the user is already logged in
2. If **not logged in** → `LoginActivity`
3. From Login → either **Register** (to create an account) or **Dashboard** (after successful login)
4. From **Dashboard**, users access all other screens via the **bottom navigation bar** or the **menu button**
5. **Log out** returns the user to the Login screen and clears their session"

### Data Flow (SharedPreferences)
"All data is stored locally using **SharedPreferences**. This is Android's simple key-value storage system. Here's what we store:
- **User credentials** — username and password
- **Login session** — whether the user is logged in
- **Piggy Bank balance** — the current balance amount
- **Transaction history** — a JSON array of all add/deduct transactions with timestamps
- **Spending items** — descriptions and amounts of expenses
- **Profile details** — name, address, contact info
- **Theme preference** — light or dark mode
- **Balance visibility** — whether the balance is shown or hidden"

### Theme System
"The app supports both **Light** and **Dark** themes. The user's choice is saved in SharedPreferences, and when they return to the app, their preference is applied automatically via the `applyCurrentTheme()` utility function called in every Activity's `onCreate()`."

### Authentication
"Authentication is handled through the **Login Presenter** (`LoginPresenter`). When a user logs in, the Presenter checks the credentials against stored data using the **Model** (`LoginModel`), and returns whether the login was successful or not — following the MVC pattern."

---

## PART 5: Application Architecture (MVC)

> *(Show a diagram: **View ↔ Controller ↔ Model** with arrows)*

**"CashBuddy follows the **MVC — Model-View-Controller** architecture. Let me explain how each layer is implemented in the project."**

### The Three Layers

**1. Model** — `LoginModel.kt`, `DashboardActivityModel.kt`
"The **Model** handles all data logic. For example, `LoginModel` checks user credentials against stored SharedPreferences data and returns whether authentication is valid. The Model doesn't know anything about the UI — it just processes data."

**2. View** — `LoginActivity.kt`, `DashboardActivity.kt`, etc.
"The **View** is the UI layer — our Activities. They handle what the user sees and interacts with. The View implements a **Contract interface** (like `LoginContract.View`) which defines methods the Presenter can call, such as `showLoginSuccess()` or `showLoginFailed()`."

**3. Controller (Presenter)** — `LoginPresenter.kt`, `DashboardActivityPresenter.kt`
"The **Controller** (called **Presenter** in our code) sits between the Model and the View. It listens for user actions from the View, asks the Model to process data, and tells the View what to display. For example:
1. User taps **Login** → View notifies the Presenter
2. Presenter calls `LoginModel.checkCredentials()`
3. Model returns success or failure
4. Presenter calls `view.showLoginSuccess()` or `view.showLoginFailed()`"

### Contract Interfaces
"To connect these layers, we use **Contract interfaces** like `LoginContract` and `DashboardActivityContract`. These define:
- What the **View** must implement (e.g., `showLoginSuccess()`, `showError()`)
- What the **Presenter** must implement (e.g., `login()`, `getUsername()`)

This makes our code clean, testable, and easy to maintain."

### Real Example — Login Flow
"Let me trace a real example: when a user taps the Login button:
1. `LoginActivity` (View) captures the username and password
2. It calls `loginPresenter.login(username, password)`
3. `LoginPresenter` (Controller) receives the data
4. Presenter calls `model.checkCredentials(username, password, callback)`
5. `LoginModel` (Model) looks up the credentials in SharedPreferences
6. Model returns the result through the callback
7. Presenter calls `view.showLoginSuccess()` or `view.showLoginFailed()`
8. View updates the UI — either navigates to Dashboard or shows an error toast"

---

## PART 6: System Demo with Voice-Over

> *(Full screen recording of the emulator or phone)*

**"Now let me demonstrate CashBuddy in action."**

### Step 1: Registration
*(Show the emulator/phone screen)*

"I'll start by opening the app. Since I'm not logged in, it takes me to the **Login screen**. I'll tap **'Create Account'** to go to Registration.

Here I'll enter a username — let's say **'user123'** — and a password. I'll tap **Register**. A toast confirms success, and I'm taken back to the Login screen."

### Step 2: Login
"Now I'll log in with the credentials I just created. I enter **'user123'** and my password, then tap **Login**.

I'm now on the **Dashboard**. Notice the message says **'Welcome back, user123!'** — personalized greeting. The Piggy Bank balance shows **P 0.00**, and since I haven't added any spending yet, the list is empty."

### Step 3: Piggy Bank — Adding Cash
"I'll tap the **Piggy Bank** icon in the bottom navigation bar. The balance shows **P 0.00**. I'll tap **Add Cash**, enter **1,000**, and confirm.

The balance now shows **P 1,000.00**, and the transaction history shows **'Cash Added'** with a timestamp. I can also toggle the **eye icon** to hide or show the balance."

### Step 4: Adding a Spend
"I'll switch to the **List screen** using the bottom nav, then tap the **Add Spend** button. A dialog appears asking for a description and amount. I'll enter **'Groceries'** and **'250'**, then confirm.

The spending item appears in the list. Back on the Dashboard, I can also see this spending reflected."

### Step 5: Profile
"I'll go to the **Profile screen** by tapping the profile icon. I can edit my name, address, and contact info. Let me enter **'Juan Dela Cruz'** as my name and tap **Update**. A toast confirms the profile was updated."

### Step 6: Settings
"Next, the **Settings screen**. I can change my password, toggle between **Light** and **Dark mode**, change language, and see app info. Let me switch to **Dark mode** — the UI changes instantly to a darker color scheme."

### Step 7: Navigation & Logout
"Finally, I'll demonstrate the navigation. I'll tap the **menu button** in the top corner — a popup menu shows all screens plus **Log out**. I'll tap **Log out**, confirm in the dialog, and I'm taken back to the Login screen."

---

## PART 7: Closing

> *(Back on camera)*

**"And that concludes my presentation of **CashBuddy** — an Android personal finance app built with the **MVC architecture**.

To summarize: we have a fully functional app with registration and login, a piggy bank with transaction history, a spending tracker, user profile management, settings with theme support, and seamless navigation between all screens — all following the **Model-View-Controller** pattern for clean, maintainable code.

Thank you for watching!"**

---

## 📝 Speaker Notes

- **Speak clearly and at a moderate pace** — don't rush through the demo
- **Use pauses** between sections to let information sink in
- **Show the code briefly** when explaining MVC — highlight the Contract interfaces, Model, Presenter, and View files
- **For the demo**, use an Android emulator or connect your phone via USB — make sure the screen recording is clear
- **Record in a quiet environment** with good lighting if you're on camera
- **Practice the flow** a few times before recording to make it smooth
- **Include a simple diagram** for Part 5 — draw it on a slide showing:
  - **View** ↔ **Controller (Presenter)** ↔ **Model**
  - Arrows showing the flow of user actions and data

---

## ⏱ Timing Guide

| Time | Section | Visual |
|------|---------|--------|
| 0:00–0:30 | Self-introduction | On camera |
| 0:30–2:00 | Project introduction | Slides / Screen |
| 2:00–4:00 | Feature overview | Screen recording |
| 4:00–6:30 | System interaction + MVC | Diagram / Code |
| 6:30–10:00 | Live demo | Emulator / Phone |
| 10:00–end | Closing | On camera |

---

## ✅ Pre-Recording Checklist

- [ ] Charge your phone / start the emulator
- [ ] Close all other apps to avoid notifications
- [ ] Reset app data (clear SharedPreferences) for a fresh demo
- [ ] Have sample credentials ready (e.g., "user123" / "pass123")
- [ ] Practice the demo flow 2–3 times before recording
- [ ] Test screen recording with audio
- [ ] Check lighting and background if on camera
