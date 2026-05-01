# Kotlin Android Project (VS Code Setup)

## Prerequisites

1. **Java JDK 17+** — [Download](https://adoptium.net/)
2. **Android SDK** — Install via [Android command-line tools](https://developer.android.com/studio#command-line-tools-only) (no Android Studio needed)
3. **VS Code** — [Download](https://code.visualstudio.com/)
4. **Kotlin** — Install via `brew install kotlin` (macOS) or `sdk install kotlin` (SDKMAN)

## Setup Android SDK (without Android Studio)

```bash
# macOS/Linux
mkdir -p ~/Android/Sdk/cmdline-tools
cd ~/Android/Sdk/cmdline-tools
# Download and unzip cmdline-tools from https://developer.android.com/studio#command-line-tools-only
# Then rename the folder:
mv cmdline-tools latest

# Set environment variables (add to ~/.bashrc or ~/.zshrc)
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Install required SDK components
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

## Opening in VS Code

```bash
code .
```

Install recommended extensions when prompted (`.vscode/extensions.json`).

## Building the App

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug

# Run all tests
./gradlew test
```

## Working with Pencil (.pen) Design Files

Pencil Project `.pen` files are ZIP archives containing XML and SVG assets.

To extract and inspect your design:
```bash
# Rename and unzip
cp your-design.pen your-design.zip
unzip your-design.zip -d pencil-assets/
# Your SVG screens will be in pencil-assets/
```

Then you can upload individual SVGs here and I'll convert them into Android XML layouts!

## Project Structure

```
app/
├── src/main/
│   ├── kotlin/com/myapp/    ← Kotlin source files
│   ├── res/
│   │   ├── layout/          ← XML layouts
│   │   ├── values/          ← colors, strings, themes
│   │   └── drawable/        ← icons and images
│   └── AndroidManifest.xml
└── build.gradle.kts
```
