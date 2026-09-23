# Android TV Search Keyboard

A Jetpack Compose keyboard for a 10-foot search screen. It follows the shape of the YouTube and Netflix keyboards on Android TV: a large query field, a D-pad grid, and room for suggestions beside the keys.

The default look follows YouTube on Android TV: bare glyphs on a neutral dark background, pill-shaped controls with icons for voice, search, shift, space, and delete, and a white pill on the focused key. The system Back key is left alone.

![Alphabetical search for Dune, with A focused and Dune: Part Two beside the keys](docs/images/search-alphabetical.png)

![QWERTY search for Andor, with Q focused](docs/images/search-qwerty.png)

![Inception matches nothing in the sample catalog](docs/images/search-empty.png)

## Install

The library is published with [JitPack](https://jitpack.io) from the root project. Add the repository, then depend on the repository coordinate.

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

```kotlin
dependencies {
    implementation("com.github.halilozel1903:android-tv-search-keyboard:1.0.0")
}
```

The keyboard types are in the `com.halil.ozel` package. The sample application id is `com.halil.ozel.sample`.

The sample module in this repository depends on the root project, which is the setup to use while you are changing the keyboard itself.

Requirements: `minSdk` 23, Jetpack Compose (BOM `2026.09.00` or newer), and `androidx.tv:tv-material`.

## Command line

JDK 17 or newer, and an Android SDK. If `ANDROID_HOME` is unset, the build uses `~/Library/Android/sdk` or `~/Android/Sdk` when that directory exists.

```bash
git clone https://github.com/halilozel1903/android-tv-search-keyboard.git
cd android-tv-search-keyboard
./gradlew :assembleRelease
./gradlew :sample:assembleDebug
./gradlew :test
```

`:assembleRelease` builds the library. `:sample:assembleDebug` builds the sample television app. `:test` runs the layout unit tests. The debug APK is `sample/build/outputs/apk/debug/sample-debug.apk`.

## Use

`query` is controlled by the caller. `onSearch` runs when the viewer presses Search.

```kotlin
import com.halil.ozel.TvSearchKeyboard

var query by remember { mutableStateOf("") }

TvSearchKeyboard(
    query = query,
    onQueryChange = { query = it },
    onSearch = { submitted -> /* open results for submitted */ },
    placeholder = "Search movies and shows",
    onVoiceSearch = { /* start your own recognizer */ },
    suggestions = {
        Suggestions(query)
    },
)
```

`onVoiceSearch` only shows a button and invokes your callback. This library does not record audio or transcribe speech.

Hide the layout switcher and lock the arrangement with `showLayoutSelector = false` and a `layout` value. To own the selection yourself, pass `layout` and `onLayoutChange` and write the new value back.

## Layouts

| Layout | What it is |
| --- | --- |
| `Alphabetical` | Default. A–Z and digits, six columns, in the order YouTube uses on TV. |
| `Qwerty` | English typewriter rows. |
| `Turkish` | Turkish alphabet. Ğ, Ü, Ş, İ, Ö, and Ç sit in dictionary order, and dotted `i` / `İ` is a different key from dotless `ı` / `I`. |

The on-screen labels are English: Alphabetical, QWERTY, and Turkish. Shift latches until it is pressed again. On the Turkish layout, shift turns `i` into `İ` and `ı` into `I`. On the English layouts, `i` becomes `I`.

## Theme

`TvSearchKeyboardDefaults.colors()` and `TvSearchKeyboardDefaults.shapes()` are the YouTube-style dark look. Replace any role you need:

```kotlin
TvSearchKeyboard(
    query = query,
    onQueryChange = { query = it },
    onSearch = { },
    colors = TvSearchKeyboardDefaults.colors(
        keyFocusedContainer = Color(0xFFE8EEF9),
        primaryContainer = Color(0xFFE8EEF9),
    ),
    shapes = TvSearchKeyboardDefaults.shapes(
        key = RoundedCornerShape(10.dp),
    ),
)
```

## Sample

The `sample` module is an Android TV app with a leanback launcher intent filter. Touch is not required. It shows a mock catalog, filters as you type, and has an empty state when nothing matches. Build it with `./gradlew :sample:assembleDebug`.

## License

[MIT](LICENSE)
