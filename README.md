# 墨メモ

Android native Java offline notes app. Package: `app.sumimemo.offline`. Android 8+ (minSdk 26), targetSdk 36, version 1.0.0 (1).

Create and edit notes, literal title/body search, pinning, recoverable trash, lifecycle save and predictive back on API 33+. No network permissions, analytics, advertisements or accounts. Title limit: 200 UTF-16 units; body limit: 100,000. No export, cloud sync or permanent per-note deletion.

## Build

Java 21 and Android SDK 36 are required. Set `ANDROID_HOME` to the installed SDK, then run:

```powershell
.\gradlew.bat assembleDebug lint
.\gradlew.bat connectedDebugAndroidTest
```

The wrapper pins Gradle 9.3.1 and validates its distribution SHA-256. Tests require a dedicated emulator, install a test app, and create/delete their own test notes. Do not run against an installation containing personal notes.

## Release signing

Set `SUMI_STORE_FILE`, `SUMI_STORE_PASSWORD`, `SUMI_KEY_ALIAS`, and `SUMI_KEY_PASSWORD` in a secure local environment, then run `gradlew.bat playBundle`. This task requires all signing inputs. Without these variables, `bundleRelease` produces an unsigned bundle suitable only for build checks.

On the configured Windows workstation, `scripts/build-play.ps1` loads the upload password from the current Windows user's DPAPI-protected file under `.android/signing/sumi-memo` and builds the signed AAB. No signing credentials are tracked in Git. Back up the upload keystore and its password securely; the DPAPI file alone is not portable to another Windows account or computer.

## Release status

See [the local release record](docs/release-2026-11-12.md) and [the current Play Console submission record](docs/play-submission-2026-11-12.md). The Play app is registered and 10/11 setup tasks are complete, including IARC rating and data safety. Store text is saved as a draft. Image and AAB uploads are blocked by the browser extension file-access setting; no review or release is submitted yet.

The app-specific Console gate requires 12 testers continuously opted in to a closed test for at least 14 days before production-access application. Local emulator tests do not satisfy that gate.

Public privacy policy: https://msandroid.github.io/sumi-memo/ . Owner-provided public support: Criotion@proton.me.

Store listing text, graphics and a draft privacy policy are under `docs/`. Actual emulator screenshots and build artifacts are local-only under `artifacts/` and `app/build/`.

No license is specified.
