package com.github.eneszel.tvsearchkeyboard

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * Colors for [TvSearchKeyboard]. Defaults are a dark 10-foot palette: charcoal keys, light labels,
 * and a near-white key when it has focus.
 */
@Immutable
class TvSearchKeyboardColors(
    val keyContainer: Color,
    val keyContent: Color,
    val keyFocusedContainer: Color,
    val keyFocusedContent: Color,
    val actionContainer: Color,
    val actionContent: Color,
    val actionBorder: Color,
    val fieldContainer: Color,
    val fieldContent: Color,
    val fieldPlaceholder: Color,
    val fieldBorder: Color,
    val caret: Color,
    val primaryContainer: Color,
    val primaryContent: Color,
    val primaryFocusedContainer: Color,
    val chipContainer: Color,
    val chipContent: Color,
    val chipSelectedContainer: Color,
    val chipSelectedBorder: Color,
) {
    /**
     * Returns a copy of this palette with the given roles replaced.
     */
    fun copy(
        keyContainer: Color = this.keyContainer,
        keyContent: Color = this.keyContent,
        keyFocusedContainer: Color = this.keyFocusedContainer,
        keyFocusedContent: Color = this.keyFocusedContent,
        actionContainer: Color = this.actionContainer,
        actionContent: Color = this.actionContent,
        actionBorder: Color = this.actionBorder,
        fieldContainer: Color = this.fieldContainer,
        fieldContent: Color = this.fieldContent,
        fieldPlaceholder: Color = this.fieldPlaceholder,
        fieldBorder: Color = this.fieldBorder,
        caret: Color = this.caret,
        primaryContainer: Color = this.primaryContainer,
        primaryContent: Color = this.primaryContent,
        primaryFocusedContainer: Color = this.primaryFocusedContainer,
        chipContainer: Color = this.chipContainer,
        chipContent: Color = this.chipContent,
        chipSelectedContainer: Color = this.chipSelectedContainer,
        chipSelectedBorder: Color = this.chipSelectedBorder,
    ): TvSearchKeyboardColors = TvSearchKeyboardColors(
        keyContainer = keyContainer,
        keyContent = keyContent,
        keyFocusedContainer = keyFocusedContainer,
        keyFocusedContent = keyFocusedContent,
        actionContainer = actionContainer,
        actionContent = actionContent,
        actionBorder = actionBorder,
        fieldContainer = fieldContainer,
        fieldContent = fieldContent,
        fieldPlaceholder = fieldPlaceholder,
        fieldBorder = fieldBorder,
        caret = caret,
        primaryContainer = primaryContainer,
        primaryContent = primaryContent,
        primaryFocusedContainer = primaryFocusedContainer,
        chipContainer = chipContainer,
        chipContent = chipContent,
        chipSelectedContainer = chipSelectedContainer,
        chipSelectedBorder = chipSelectedBorder,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TvSearchKeyboardColors) return false
        return keyContainer == other.keyContainer &&
            keyContent == other.keyContent &&
            keyFocusedContainer == other.keyFocusedContainer &&
            keyFocusedContent == other.keyFocusedContent &&
            actionContainer == other.actionContainer &&
            actionContent == other.actionContent &&
            actionBorder == other.actionBorder &&
            fieldContainer == other.fieldContainer &&
            fieldContent == other.fieldContent &&
            fieldPlaceholder == other.fieldPlaceholder &&
            fieldBorder == other.fieldBorder &&
            caret == other.caret &&
            primaryContainer == other.primaryContainer &&
            primaryContent == other.primaryContent &&
            primaryFocusedContainer == other.primaryFocusedContainer &&
            chipContainer == other.chipContainer &&
            chipContent == other.chipContent &&
            chipSelectedContainer == other.chipSelectedContainer &&
            chipSelectedBorder == other.chipSelectedBorder
    }

    override fun hashCode(): Int {
        var result = keyContainer.hashCode()
        result = 31 * result + keyContent.hashCode()
        result = 31 * result + keyFocusedContainer.hashCode()
        result = 31 * result + keyFocusedContent.hashCode()
        result = 31 * result + actionContainer.hashCode()
        result = 31 * result + actionContent.hashCode()
        result = 31 * result + actionBorder.hashCode()
        result = 31 * result + fieldContainer.hashCode()
        result = 31 * result + fieldContent.hashCode()
        result = 31 * result + fieldPlaceholder.hashCode()
        result = 31 * result + fieldBorder.hashCode()
        result = 31 * result + caret.hashCode()
        result = 31 * result + primaryContainer.hashCode()
        result = 31 * result + primaryContent.hashCode()
        result = 31 * result + primaryFocusedContainer.hashCode()
        result = 31 * result + chipContainer.hashCode()
        result = 31 * result + chipContent.hashCode()
        result = 31 * result + chipSelectedContainer.hashCode()
        result = 31 * result + chipSelectedBorder.hashCode()
        return result
    }
}

/**
 * Corner shapes for [TvSearchKeyboard].
 */
@Immutable
class TvSearchKeyboardShapes(
    val key: Shape,
    val field: Shape,
    val primary: Shape,
    val chip: Shape,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TvSearchKeyboardShapes) return false
        return key == other.key &&
            field == other.field &&
            primary == other.primary &&
            chip == other.chip
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + field.hashCode()
        result = 31 * result + primary.hashCode()
        result = 31 * result + chip.hashCode()
        return result
    }
}

/**
 * Dark television defaults for [TvSearchKeyboard].
 */
object TvSearchKeyboardDefaults {
    /**
     * Dark key surfaces with a light focused key. Pass individual colors to recolor a role.
     */
    fun colors(
        keyContainer: Color = Color(0xFF1A1E29),
        keyContent: Color = Color(0xFFF3F5F8),
        keyFocusedContainer: Color = Color(0xFFF4F6FB),
        keyFocusedContent: Color = Color(0xFF12141A),
        actionContainer: Color = Color(0xFF12151E),
        actionContent: Color = Color(0xFFE7EAF1),
        actionBorder: Color = Color(0xFF343C50),
        fieldContainer: Color = Color(0xFF10131B),
        fieldContent: Color = Color(0xFFF7F8FB),
        fieldPlaceholder: Color = Color(0xFF8E97A8),
        fieldBorder: Color = Color(0xFF2C3448),
        caret: Color = Color(0xFFF7F8FB),
        primaryContainer: Color = Color(0xFFF4F6FB),
        primaryContent: Color = Color(0xFF12141A),
        primaryFocusedContainer: Color = Color(0xFFFFFFFF),
        chipContainer: Color = Color(0xFF161A24),
        chipContent: Color = Color(0xFFD5DAE6),
        chipSelectedContainer: Color = Color(0xFF2A3346),
        chipSelectedBorder: Color = Color(0xFFE6EAF3),
    ): TvSearchKeyboardColors = TvSearchKeyboardColors(
        keyContainer = keyContainer,
        keyContent = keyContent,
        keyFocusedContainer = keyFocusedContainer,
        keyFocusedContent = keyFocusedContent,
        actionContainer = actionContainer,
        actionContent = actionContent,
        actionBorder = actionBorder,
        fieldContainer = fieldContainer,
        fieldContent = fieldContent,
        fieldPlaceholder = fieldPlaceholder,
        fieldBorder = fieldBorder,
        caret = caret,
        primaryContainer = primaryContainer,
        primaryContent = primaryContent,
        primaryFocusedContainer = primaryFocusedContainer,
        chipContainer = chipContainer,
        chipContent = chipContent,
        chipSelectedContainer = chipSelectedContainer,
        chipSelectedBorder = chipSelectedBorder,
    )

    /** Rounded rectangles sized for a 10-foot keyboard. */
    fun shapes(
        key: Shape = RoundedCornerShape(8.dp),
        field: Shape = RoundedCornerShape(12.dp),
        primary: Shape = RoundedCornerShape(12.dp),
        chip: Shape = RoundedCornerShape(8.dp),
    ): TvSearchKeyboardShapes = TvSearchKeyboardShapes(
        key = key,
        field = field,
        primary = primary,
        chip = chip,
    )
}
