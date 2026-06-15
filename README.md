# NIT3213 Final Assignment — Android Application

API Integration, Dependency Injection & Unit Testing
**Student ID:** s8140991

## Overview

An Android application built for the NIT3213 final assignment. The app authenticates a user against the VU NIT3213 REST API, retrieves a list of data entities, and presents them across three screens:

- **Login** — the user enters their credentials; the app authenticates and receives a `keypass`.
- **Dashboard** — a RecyclerView lists all entities (summary view); tapping a row opens its details.
- **Details** — shows every field of the selected entity, including the full description.

The dataset for this build is a finance / assets topic, so each entity has the fields `assetType`, `ticker`, `currentPrice`, `dividendYield`, and `description`.

## Architecture

The project follows the **MVVM** pattern with a Repository layer and **Hilt** dependency injection, keeping UI, business logic, and networking cleanly separated.

| Layer | Responsibility |
|-------|----------------|
| `data/model` | Kotlin data classes for API requests/responses (`LoginRequest`, `LoginResponse`, `Entity`, `DashboardResponse`) |
| `data/remote` | Retrofit `ApiService` interface describing the auth and dashboard endpoints |
| `data/repository` | `AuthRepository` wraps API calls and maps them to a success/error `Resource` type |
| `di` | `NetworkModule` — Hilt module providing OkHttp, Retrofit, and `ApiService` as singletons |
| `ui/login`, `ui/dashboard` | ViewModels and UI-state holders for each screen, plus the RecyclerView adapter |
| `util` | `Resource` — a sealed wrapper that keeps Retrofit types out of the UI layer |

Activities (`LoginActivity`, `MainActivity` as the dashboard, `DetailsActivity`) observe `StateFlow` from their ViewModels and never call the network directly.

## Tech Stack

- **Language:** Kotlin
- **Networking:** Retrofit + Gson + OkHttp logging interceptor
- **Async:** Kotlin Coroutines + StateFlow
- **Dependency Injection:** Hilt
- **UI:** Activities + RecyclerView (View system)
- **Testing:** JUnit4 + MockK + kotlinx-coroutines-test

## Requirements

| Tool | Version |
|------|---------|
| Android Studio | Latest stable (Otter or newer) |
| JDK | 17 or higher |
| Gradle | 9.4.1 (via wrapper) |
| Android Gradle Plugin | 9.2.1 |
| compileSdk / targetSdk | 36 |
| minSdk | 24 |

> **Note:** This project opts out of AGP 9's built-in Kotlin (`android.builtInKotlin=false` in `gradle.properties`) so that the Kotlin Android plugin works together with KSP and Hilt.

## Build and Run

1. **Open the project** in Android Studio and let it complete a Gradle sync. All dependencies are declared in the version catalog (`gradle/libs.versions.toml`) and download automatically.

2. **Set your campus.** The login endpoint depends on your class location. Open `LoginActivity.kt` and set the campus to `"footscray"`, `"sydney"`, or `"ort"`:

   ```kotlin
   private val campus = "sydney"
   ```

3. **Run the app** on an emulator or device running Android 7.0 (API 24) or higher. An internet connection is required.

   > The API is hosted on a free tier, so the first request after a period of inactivity may take 30–60 seconds while the server wakes up.

## Login Credentials

Per the assignment specification, the **first name is the username** and the **student ID is the password**:

| Field | Value |
|-------|-------|
| Username | Your first name (e.g. `Aayush`) |
| Password | Your student ID (e.g. `s8140991`) |

On a successful login the app stores the returned `keypass` and uses it to load the dashboard. On failure, an error message is displayed.

## Testing

Unit tests cover the critical components — the **Login** and **Dashboard** ViewModels — using MockK to fake the repository and a test dispatcher rule for coroutines. They verify success, error, and validation paths.

Run from the command line:

```bash
./gradlew testDebugUnitTest
```

Or right-click the `app/src/test` package in Android Studio and choose **Run**.

## Project Structure

```
app/src/main/java/com/example/assignment1/
├── data/
│   ├── model/        (LoginRequest, LoginResponse, Entity, DashboardResponse)
│   ├── remote/       (ApiService)
│   └── repository/   (AuthRepository)
├── di/               (NetworkModule)
├── ui/
│   ├── login/        (LoginViewModel)
│   └── dashboard/    (DashboardViewModel, EntityAdapter)
├── util/             (Resource)
├── StudioXApp.kt     (@HiltAndroidApp Application)
├── LoginActivity.kt
├── MainActivity.kt   (Dashboard)
└── DetailsActivity.kt
```

## API Reference

Base URL: `https://nit3213api.onrender.com/`

- **Login** — `POST /{campus}/auth` with a JSON body of `username` and `password`; returns a `keypass`.
- **Dashboard** — `GET /dashboard/{keypass}`; returns an `entities` array and an `entityTotal`.
