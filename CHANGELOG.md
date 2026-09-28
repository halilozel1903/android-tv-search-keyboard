# Changelog

## 1.1.0

### Added

- `&123` page with digits and punctuation, for titles such as "Spider-Man" or "Grey's Anatomy". Set its labels with `symbolsLabel` and `lettersLabel`.
- Hardware keyboard input. Printable keys and Backspace from a USB or Bluetooth keyboard, or the number pad on a remote, edit the query. Turn it off with `acceptHardwareKeyboard = false`.
- A long press on Delete clears the query.

### Changed

- YouTube-style redesign: bare glyph keys, pill-shaped field and controls, and a neutral dark palette.
- Icon buttons for voice, search, clear, shift, space, and delete, with content descriptions for screen readers.
- Segmented layout switcher.
- The search button is grey until it has focus, so focus on it is easy to see.
- Compose UI and Foundation are `api` dependencies, and the library is built in explicit API mode.

### Fixed

- JitPack builds. Earlier tags failed because JitPack injected a second `maven-publish` plugin, so the published coordinate could not be resolved.
- Key presses that arrive faster than the caller recomposes are no longer dropped.

## 1.0.0

- Publish the library from the root project. The JitPack coordinate is `com.github.halilozel1903:android-tv-search-keyboard:1.0.0`.
- README screenshots of the alphabetical search screen, the QWERTY layout, and the empty result state.

## 0.1.0

- First public release of the Android TV search keyboard.
- Alphabetical, QWERTY, and Turkish key arrangements.
- Controlled query field, search, delete, clear, space, and shift.
- Optional voice-search callback and suggestions slot.
- Dark 10-foot defaults for color and shape.
- Sample Android TV search screen.
