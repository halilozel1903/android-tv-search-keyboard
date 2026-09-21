package com.halil.ozel

/**
 * Key arrangement shown by [TvSearchKeyboard].
 *
 * [Alphabetical] is the default and follows the six-column order used by YouTube on Android TV.
 * [Qwerty] is the English typewriter order. [Turkish] is the Turkish alphabet, with Ğ, Ü, Ş, İ,
 * Ö, Ç and both dotted and dotless i in their dictionary positions.
 */
enum class TvSearchKeyboardLayout {
    Alphabetical,
    Qwerty,
    Turkish,
}

/**
 * English labels for the layout switcher.
 *
 * @param alphabetical Label for [TvSearchKeyboardLayout.Alphabetical].
 * @param qwerty Label for [TvSearchKeyboardLayout.Qwerty].
 * @param turkish Label for [TvSearchKeyboardLayout.Turkish].
 */
class TvSearchKeyboardLayoutLabels(
    val alphabetical: String = "Alphabetical",
    val qwerty: String = "QWERTY",
    val turkish: String = "Turkish",
) {
    /** Label for [layout]. */
    fun labelFor(layout: TvSearchKeyboardLayout): String = when (layout) {
        TvSearchKeyboardLayout.Alphabetical -> alphabetical
        TvSearchKeyboardLayout.Qwerty -> qwerty
        TvSearchKeyboardLayout.Turkish -> turkish
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TvSearchKeyboardLayoutLabels) return false
        return alphabetical == other.alphabetical &&
            qwerty == other.qwerty &&
            turkish == other.turkish
    }

    override fun hashCode(): Int {
        var result = alphabetical.hashCode()
        result = 31 * result + qwerty.hashCode()
        result = 31 * result + turkish.hashCode()
        return result
    }
}
