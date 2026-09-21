package com.github.eneszel.tvsearchkeyboard

/**
 * One letter or digit. [upper] is the glyph inserted while shift is on.
 * English "i" uppercases to "I". The Turkish dotted key uppercases to "İ", and dotless "ı" to "I".
 */
internal data class Glyph(
    val lower: String,
    val upper: String,
) {
    fun rendered(shifted: Boolean): String = if (shifted) upper else lower
}

internal data class KeyRows(
    val columns: Int,
    val rows: List<List<Glyph>>,
)

internal fun rowsFor(layout: TvSearchKeyboardLayout): KeyRows = when (layout) {
    TvSearchKeyboardLayout.Alphabetical -> KeyRows(
        columns = 6,
        rows = listOf(
            latinRow("abcdef"),
            latinRow("ghijkl"),
            latinRow("mnopqr"),
            latinRow("stuvwx"),
            latinRow("yz1234"),
            latinRow("567890"),
        ),
    )

    TvSearchKeyboardLayout.Qwerty -> KeyRows(
        columns = 10,
        rows = listOf(
            latinRow("qwertyuiop"),
            latinRow("asdfghjkl"),
            latinRow("zxcvbnm"),
            latinRow("1234567890"),
        ),
    )

    // Seven columns keep the full alphabet and digits in six rows, the same height as
    // the alphabetical layout, so the action row still fits a 1080p television.
    TvSearchKeyboardLayout.Turkish -> KeyRows(
        columns = 7,
        rows = listOf(
            listOf(latin("a"), latin("b"), latin("c"), tr("ç", "Ç"), latin("d"), latin("e"), latin("f")),
            listOf(latin("g"), tr("ğ", "Ğ"), latin("h"), tr("ı", "I"), tr("i", "İ"), latin("j"), latin("k")),
            listOf(latin("l"), latin("m"), latin("n"), latin("o"), tr("ö", "Ö"), latin("p"), latin("r")),
            listOf(latin("s"), tr("ş", "Ş"), latin("t"), latin("u"), tr("ü", "Ü"), latin("v"), latin("y")),
            listOf(latin("z"), latin("1"), latin("2"), latin("3"), latin("4"), latin("5"), latin("6")),
            latinRow("7890"),
        ),
    )
}

internal fun deleteLastCodePoint(value: String): String {
    if (value.isEmpty()) return value
    val end = value.offsetByCodePoints(value.length, -1)
    return value.substring(0, end)
}

private fun latinRow(letters: String): List<Glyph> = letters.map { latin(it.toString()) }

private fun latin(letter: String): Glyph {
    val upper = when (letter) {
        "i" -> "I"
        else -> letter.uppercase()
    }
    return Glyph(lower = letter, upper = upper)
}

private fun tr(lower: String, upper: String): Glyph = Glyph(lower = lower, upper = upper)
