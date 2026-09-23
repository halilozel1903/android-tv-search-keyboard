package com.halil.ozel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/**
 * 24dp glyphs drawn by the keyboard. Kept internal so the library does not need the Material
 * icons artifact. Paths follow the Material Symbols grid.
 */
internal object KeyboardIcons {
    val Search: ImageVector = icon(
        "Search",
        "M15.5,14h-0.79l-0.28,-0.27C15.41,12.59 16,11.11 16,9.5 16,5.91 13.09,3 9.5,3S3,5.91 3,9.5 " +
            "5.91,16 9.5,16c1.61,0 3.09,-0.59 4.23,-1.57l0.27,0.28v0.79l5,4.99L20.49,19l-4.99,-5z" +
            "M9.5,14C7.01,14 5,11.99 5,9.5S7.01,5 9.5,5 14,7.01 14,9.5 11.99,14 9.5,14z",
    )

    val Mic: ImageVector = icon(
        "Mic",
        "M12,14c1.66,0 2.99,-1.34 2.99,-3L15,5c0,-1.66 -1.34,-3 -3,-3S9,3.34 9,5v6c0,1.66 1.34,3 3,3z" +
            "M17.3,11c0,3 -2.54,5.1 -5.3,5.1S6.7,14 6.7,11L5,11c0,3.41 2.72,6.23 6,6.72L11,21h2v-3.28" +
            "c3.28,-0.48 6,-3.3 6,-6.72h-1.7z",
    )

    val Backspace: ImageVector = icon(
        "Backspace",
        "M22,3H7C6.31,3 5.77,3.35 5.41,3.88L0,12l5.41,8.11C5.77,20.64 6.31,21 7,21h15c1.1,0 2,-0.9 2,-2V5" +
            "C24,3.9 23.1,3 22,3zM22,19H7.07L2.4,12l4.66,-7H22V19z" +
            "M10.41,17L14,13.41 17.59,17 19,15.59 15.41,12 19,8.41 17.59,7 14,10.59 10.41,7 9,8.41 " +
            "12.59,12 9,15.59z",
    )

    val Close: ImageVector = icon(
        "Close",
        "M19,6.41L17.59,5 12,10.59 6.41,5 5,6.41 10.59,12 5,17.59 6.41,19 12,13.41 17.59,19 19,17.59 13.41,12z",
    )

    val Shift: ImageVector = icon(
        "Shift",
        "M12,3.5L3,13.5h5.5v7h7v-7H21L12,3.5z" +
            "M13.5,11.5v7h-3v-7H7.5L12,6.5l4.5,5H13.5z",
    )

    val ShiftOn: ImageVector = icon(
        "ShiftOn",
        "M12,3.5L3,13.5h5.5v7h7v-7H21L12,3.5z",
    )

    val Space: ImageVector = icon(
        "Space",
        "M18,9v4H6V9H4v6h16V9h-2z",
    )

    private fun icon(name: String, pathData: String): ImageVector =
        ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).addPath(
            pathData = addPathNodes(pathData),
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd,
        ).build()
}
