# AutoChecker Android

Android client for the AutoChecker vehicle history platform.
Built with Kotlin, Jetpack Compose, Hilt, and Retrofit.

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK: compile SDK 35, min SDK 24 (Android 7.0+)
- A running AutoChecker backend (see `back/README.md`)

---

## Setup

### 1. Clone and open

Open the `front/` folder in Android Studio as the project root.

### 2. Configure the backend URL

Find the Retrofit base URL in the DI/network module and set it to your backend address:

- **Local emulator**: `http://10.0.2.2:8000/`
- **Physical device on same network**: `http://<your-machine-ip>:8000/`
- **Production**: your deployed API URL

### 3. Sync Gradle

Android Studio will prompt to sync automatically. If not:

```
File → Sync Project with Gradle Files
```

### 4. Run

Connect a device or start an emulator, then click **Run** (Shift+F10), or from the terminal:

```bash
# Windows
gradlew.bat installDebug

# macOS/Linux
./gradlew installDebug
```

---

## Build variants

| Variant | Description |
|---|---|
| `debug` | Development build with logging enabled |
| `release` | Minified + ProGuard, requires signing config |

To build a release APK:

```bash
# Windows
gradlew.bat assembleRelease

# macOS/Linux
./gradlew assembleRelease
```

---

## Project structure

```
front/app/src/main/
├── data/
│   ├── api/          # Retrofit service interfaces
│   ├── model/        # Data classes (DTOs)
│   └── repository/   # Repository implementations
├── di/               # Hilt modules
├── ui/
│   ├── navigation/   # Screen.kt, AppNavGraph.kt
│   ├── screens/      # One folder per screen
│   └── theme/        # Colors, typography, shapes
└── MainActivity.kt
```

---

## Tech stack

| Library | Purpose |
|---|---|
| Jetpack Compose | UI |
| Hilt | Dependency injection |
| Retrofit + OkHttp | HTTP client |
| Gson | JSON serialization |
| Coil | Image loading |
| DataStore | Local preferences / token storage |
| Navigation Compose | In-app navigation |

---

## Localization

String resources are in three language folders:

- `res/values/strings.xml` — Uzbek (default)
- `res/values-ru/strings.xml` — Russian
- `res/values-en/strings.xml` — English

Add new strings to all three files to keep languages in sync.

---

## Running tests

```bash
# Unit tests
./gradlew test

# Instrumentation tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```
