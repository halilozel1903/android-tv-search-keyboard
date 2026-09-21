package com.halil.ozel.sample

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.requestFocus
import com.halil.ozel.TvSearchKeyboardLayout
import com.github.takahirom.roborazzi.captureRoboImage
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [35], qualifiers = "w960dp-h540dp-land-xhdpi")
class SearchKeyboardScreenshotTest {
    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun alphabeticalSearchForDune() {
        capture("search-alphabetical.png", "Dune", TvSearchKeyboardLayout.Alphabetical, "a")
    }

    @Test
    fun qwertySearchForAndor() {
        capture("search-qwerty.png", "Andor", TvSearchKeyboardLayout.Qwerty, "q")
    }

    @Test
    fun emptySearchForInception() {
        capture("search-empty.png", "Inception", TvSearchKeyboardLayout.Alphabetical, "a")
    }

    private fun capture(
        name: String,
        query: String,
        layout: TvSearchKeyboardLayout,
        focusedKey: String,
    ) {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            SampleSearchScreen(initialQuery = query, initialLayout = layout)
        }
        rule.mainClock.advanceTimeBy(500)
        rule.onNodeWithText(focusedKey).requestFocus()
        rule.mainClock.advanceTimeBy(32)
        val dir = File("..", "docs/images")
        dir.mkdirs()
        rule.onRoot().captureRoboImage(File(dir, name).path)
    }
}
