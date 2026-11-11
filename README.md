# すみメモ
Android native Java offline notes app. Package: app.sumimemo.offline (confirm availability/ownership in Play Console). minSdk 26 / targetSdk 36 / version 1.0.0 (1).

## Features
SQLite storage; create/edit; literal title/body search; pinning; recoverable trash; lifecycle save; platform predictive-back callback on API 33+. No network permissions, analytics, advertisements or accounts. Title limit: 200 UTF-16 units. Body limit: 100,000. No per-note permanent deletion or export.

## Build
Java 21, Gradle 9.3.1, Android SDK 36. Run: gradle assembleDebug bundleRelease lint.
No release signing configuration is included. The AAB is UNSIGNED. Do not upload the debug APK as a production artifact. Store/release signing must be configured securely by the account owner.

## Verified on 2026-11-10
Gradle debug APK and release AAB builds passed. Full rerun of Android lint: No issues found. Debug APK signature verification and bundletool AAB validation passed.
Android 16 emulator: creation, save, pin, reopen, navigation, search, no-match search, process restart persistence, delete cancellation, trash, restore and pause autosave passed. Final Gradle APK: rotation draft persistence, 200% font size, visible keyboard, save and Japanese content display checked.

## Remaining release work
Confirm final package and developer identity; supply verified developer support contact; host the completed privacy policy; configure secure production upload signing; review app access, ads, data safety, audience, content-rating and distribution declarations in Play Console; satisfy any account-specific testing/verification requirements. Android 8 and physical devices were not tested. No app was submitted or published by this task.

## Repository contents
This repository contains source code only. Build artifacts, screenshots, release documents, signing credentials and local settings are excluded. No license is specified.
