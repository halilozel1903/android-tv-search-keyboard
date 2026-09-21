# Contributing

Thanks for looking at the keyboard. Issues and pull requests are welcome.

## Build

You need JDK 17 or newer and the Android SDK. `compileSdk` is 37.

```bash
./gradlew :tv-search-keyboard:assembleRelease :sample:assembleDebug
```

Unit tests for the key arrangements:

```bash
./gradlew :tv-search-keyboard:test
```

The sample module is an Android TV app. Install `sample/build/outputs/apk/debug/sample-debug.apk` on a television or an Android TV emulator. Touch is not required.

## Changes

- Keep the public API in `tv-search-keyboard` small and documented with KDoc.
- Do not intercept the system Back key. Delete stays a key on the keyboard.
- UI strings, docs, and commit messages are English.
- The Turkish layout is a character set. Its API name and on-screen label stay English (`Turkish`).
- Add a `CHANGELOG.md` note for user-visible changes.

## Pull requests

Open a pull request against `main`. Describe what changed and how you checked it. GitHub Actions assembles the library release and the sample debug build.
