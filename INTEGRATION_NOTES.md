# NIT3213 API Integration — what changed

Your original project was a static photography-themed UI (Login -> Main -> Home,
wired with hardcoded Intents). It has been repurposed into the real assignment flow:

    LoginActivity  ->  MainActivity (Dashboard, RecyclerView)  ->  DetailsActivity

## Architecture (MVVM + Repository + Hilt)
- data/model        : LoginRequest, LoginResponse, Entity, DashboardResponse
- data/remote       : ApiService (Retrofit interface)
- data/repository   : AuthRepository (wraps calls into Resource success/error)
- di/NetworkModule  : Hilt module providing OkHttp, Retrofit, ApiService
- ui/login          : LoginViewModel + LoginUiState
- ui/dashboard      : DashboardViewModel, DashboardUiState, EntityAdapter
- util/Resource     : sealed success/error wrapper (keeps Retrofit out of UI)
- StudioXApp        : @HiltAndroidApp Application (registered in manifest)

## Before you run
1. Open in Android Studio and let it Gradle-sync (new plugins: Kotlin, KSP, Hilt).
2. Set your campus in LoginActivity.kt -> `private val campus = "sydney"`
   (options: "footscray", "sydney", "ort").
3. Log in with username = your student id (s8140991), password = your first name.

## Why these choices
- Hilt: compile-time-checked DI expected by the marking rubric; @HiltViewModel +
  by viewModels() removes boilerplate factories.
- Gson: flat models, no codegen needed.
- Entity is Serializable so the full object (incl. description) is passed to
  DetailsActivity via Intent — the list screen shows only property1/property2.
- StateFlow + sealed UiState makes ViewModels pure Kotlin and unit-testable
  (no Android dependencies), see app/src/test/.../ui/.

## Tests
app/src/test/java/.../ui/
- LoginViewModelTest: success, failure, blank-input
- DashboardViewModelTest: success, error
Run: `./gradlew testDebugUnitTest` (or right-click the ui package in Studio).

## Fix: "Cannot add extension with name 'kotlin'" (AGP 9 built-in Kotlin)
AGP 9 enables built-in Kotlin by default; applying org.jetbrains.kotlin.android
on top of it registers a duplicate `kotlin` extension and the build fails.
KSP (needed by Hilt) is also not compatible with built-in Kotlin yet.

Resolution applied:
1. gradle.properties -> `android.builtInKotlin=false`  (opt out of built-in Kotlin)
2. Keep the classic `org.jetbrains.kotlin.android` plugin + KSP + Hilt.
3. Bump versions to the AGP-9 floor so KGP/KSP align:
   kotlin = 2.2.10, ksp = 2.2.10-2.0.2
After this, `kotlinOptions { jvmTarget = "11" }` works as normal.
Do a Gradle sync (and File > Invalidate Caches if it lingers).
