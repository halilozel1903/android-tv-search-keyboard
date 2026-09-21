package com.github.eneszel.tvsearchkeyboard

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TvKeyboardLayoutsTest {
    @Test
    fun alphabeticalIsSixColumnsStartingWithA() {
        val rows = rowsFor(TvSearchKeyboardLayout.Alphabetical)
        assertEquals(6, rows.columns)
        assertEquals("abcdef", rows.rows.first().joinToString("") { it.lower })
        assertEquals("567890", rows.rows.last().joinToString("") { it.lower })
    }

    @Test
    fun qwertyUsesTheTypewriterOrder() {
        val rows = rowsFor(TvSearchKeyboardLayout.Qwerty)
        assertEquals("qwertyuiop", rows.rows[0].joinToString("") { it.lower })
        assertEquals("asdfghjkl", rows.rows[1].joinToString("") { it.lower })
        assertEquals("zxcvbnm", rows.rows[2].joinToString("") { it.lower })
        val i = rows.rows.flatten().single { it.lower == "i" }
        assertEquals("I", i.upper)
    }

    @Test
    fun turkishAlphabetKeepsDottedAndDotlessIInDictionaryOrder() {
        val rows = rowsFor(TvSearchKeyboardLayout.Turkish)
        assertEquals(7, rows.columns)
        assertEquals(6, rows.rows.size)
        val flat = rows.rows.flatten()
        val letters = flat.take(29).joinToString("") { it.lower }
        assertEquals("abcçdefgğhıijklmnoöprsştuüvyz", letters)
        assertEquals("İ", flat.single { it.lower == "i" }.upper)
        assertEquals("I", flat.single { it.lower == "ı" }.upper)
        assertEquals("Ğ", flat.single { it.lower == "ğ" }.upper)
        assertEquals("Ü", flat.single { it.lower == "ü" }.upper)
        assertEquals("Ş", flat.single { it.lower == "ş" }.upper)
        assertEquals("Ö", flat.single { it.lower == "ö" }.upper)
        assertEquals("Ç", flat.single { it.lower == "ç" }.upper)
    }

    @Test
    fun deleteRemovesOneCodePoint() {
        assertEquals("An", deleteLastCodePoint("Ana"))
        assertEquals("", deleteLastCodePoint("İ"))
        assertEquals("", deleteLastCodePoint(""))
    }

    @Test
    fun keyHeightStaysInsideTheTelevisionRange() {
        val metrics = keyboardMetrics(columnHeight = 520.dp, glyphRows = 6, showLayouts = true)
        assertTrue(metrics.keyHeight >= 52.dp)
        assertTrue(metrics.keyHeight <= 64.dp)
        assertTrue(metrics.rowGap >= 4.dp)
    }
}
