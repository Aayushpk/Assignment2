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

## Environment Requirements

| Tool | Version |
|------|---------|
| Android Studio | Latest stable (Otter or newer) |
| JDK | 17 or higher |
| Gradle | 9.4.1 (via wrapper) |
| Android Gradle Plugin | 9.2.1 |
| Kotlin | 2.2.10 |
| KSP | 2.2.10-2.0.2 |
| compileSdk / targetSdk | 36 |
| minSdk | 24 |

> **Note:** This project opts out of AGP 9's built-in Kotlin (`android.builtInKotlin=false` in `gradle.properties`) so that the Kotlin Android plugin works together with KSP and Hilt.

## Dependencies

All dependencies are managed through the Gradle version catalog (`gradle/libs.versions.toml`) and download automatically on Gradle sync. No manual installation is required.

### Gradle Plugins

| Plugin | Version | Purpose |
|--------|---------|---------|
| `com.android.application` | 9.2.1 | Android application build plugin (AGP) |
| `org.jetbrains.kotlin.android` | 2.2.10 | Kotlin language support |
| `com.google.devtools.ksp` | 2.2.10-2.0.2 | Kotlin Symbol Processing (annotation processing for Hilt) |
| `com.google.dagger.hilt.android` | 2.59.2 | Hilt dependency injection |

### Libraries

| Library | Version | Purpose |
|---------|---------|---------|
| `androidx.core:core-ktx` | 1.18.0 | Kotlin extensions for core Android APIs |
| `androidx.appcompat:appcompat` | 1.7.1 | Backwards-compatible Activity support |
| `com.google.android.material:material` | 1.14.0 | Material Design UI components |
| `androidx.activity:activity-ktx` | 1.13.0 | `by viewModels()` and Activity KTX helpers |
| `androidx.constraintlayout:constraintlayout` | 2.2.1 | Flexible UI layouts |
| `androidx.recyclerview:recyclerview` | 1.3.2 | Scrolling list for the dashboard |
| `androidx.lifecycle:lifecycle-viewmodel-ktx` | 2.8.7 | ViewModel + `viewModelScope` |
| `androidx.lifecycle:lifecycle-runtime-ktx` | 2.8.7 | Lifecycle-aware coroutine scopes |
| `org.jetbrains.kotlinx:kotlinx-coroutines-android` | 1.9.0 | Coroutines for background work |
| `com.squareup.retrofit2:retrofit` | 2.11.0 | Type-safe HTTP client |
| `com.squareup.retrofit2:converter-gson` | 2.11.0 | Gson JSON (de)serialization for Retrofit |
| `com.squareup.okhttp3:logging-interceptor` | 4.12.0 | Logs network requests during development |
| `com.google.dagger:hilt-android` | 2.59.2 | Hilt runtime |
| `com.google.dagger:hilt-android-compiler` | 2.59.2 | Hilt annotation processor (run via KSP) |

### Test Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| `junit:junit` | 4.13.2 | Unit testing framework |
| `io.mockk:mockk` | 1.13.13 | Mocking the repository in ViewModel tests |
| `org.jetbrains.kotlinx:kotlinx-coroutines-test` | 1.9.0 | Testing coroutine-based code |
| `androidx.arch.core:core-testing` | 2.2.0 | Synchronous execution of architecture components in tests |
| `androidx.test.ext:junit` | 1.3.0 | AndroidX instrumented test support |
| `androidx.test.espresso:espresso-core` | 3.7.0 | UI / instrumented testing |

## Build and Run

1. **Open the project** in Android Studio and let it complete a Gradle sync. All dependencies listed above download automatically from the version catalog (`gradle/libs.versions.toml`).

2. **Set your campus.** The login endpoint depends on your class location. Open `LoginActivity.kt` and set the campus to `"footscray"`, `"sydney"`, or `"ort"`:

   ```kotlin
   private val campus = "sydney"
   ```

3. **Run the app** on an emulator or device running Android 7.0 (API 24) or higher. An internet connection is required.

   > The API is hosted on a free tier, so the first request after a period of inactivity may take 30–60 seconds while the server wakes up.

## Login Credentials

Per the assignment specification, the **student ID is the username** and the **first name  is the password**: I found out, if the user has middle name, the password would be **first name(space)middle name** in my case **Aayush Pratap**

| Field | Value |
|-------|-------|
| Username | Your student ID (e.g. `s8140991`) |
| Password | Your first name (e.g. `Aayush Pratap`) |

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
