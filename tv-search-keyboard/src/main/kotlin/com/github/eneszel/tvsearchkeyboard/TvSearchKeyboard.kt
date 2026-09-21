package com.github.eneszel.tvsearchkeyboard

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.SelectableSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import androidx.tv.material3.darkColorScheme

private val QueryHeight = 60.dp
private val ChipHeight = 32.dp
private val SectionGap = 8.dp
private val FocusedScale = 1.06f
private val PressedScale = 1.02f

/**
 * Sizes the key grid for the height that is left after the query field, layout switcher, and
 * the action row. Keys stay inside 52–64dp. The gap shrinks only when a short window would
 * otherwise push a key under 52dp.
 */
internal fun keyboardMetrics(
    columnHeight: Dp,
    glyphRows: Int,
    showLayouts: Boolean,
): KeyboardMetrics {
    val layoutBlock = if (showLayouts) ChipHeight + SectionGap else 0.dp
    val fixed = QueryHeight + layoutBlock + SectionGap + SectionGap
    val available = (columnHeight - fixed).coerceAtLeast(0.dp)
    val bands = glyphRows + 1
    for (gap in listOf(8.dp, 6.dp, 4.dp)) {
        val gaps = gap * (glyphRows - 1).coerceAtLeast(0)
        val key = if (bands == 0) 0.dp else (available - gaps) / bands
        if (key >= 52.dp) {
            return KeyboardMetrics(keyHeight = key.coerceAtMost(64.dp), rowGap = gap)
        }
    }
    val gap = 4.dp
    val gaps = gap * (glyphRows - 1).coerceAtLeast(0)
    val key = if (bands == 0) 52.dp else ((available - gaps) / bands).coerceAtLeast(48.dp)
    return KeyboardMetrics(keyHeight = key.coerceAtMost(64.dp), rowGap = gap)
}

internal data class KeyboardMetrics(
    val keyHeight: Dp,
    val rowGap: Dp,
)

/**
 * 10-foot search keyboard for Android TV.
 *
 * The [query] is controlled by the caller. D-pad focus moves across the grid; the focused key
 * scales slightly and inverts to a light surface with dark text. Delete is its own key. This
 * composable does not handle the system Back key.
 *
 * Pass [onVoiceSearch] to show a voice button. The library does not record or transcribe speech.
 * Pass [suggestions] to place a results column to the right of the keys.
 *
 * @param query Current query text.
 * @param onQueryChange Called when a key, delete, clear, or space changes the query.
 * @param onSearch Called with the current query when the search key is pressed.
 * @param modifier Modifier for the keyboard, including the suggestions column when present.
 * @param placeholder Shown inside the query field while [query] is empty.
 * @param layout Key arrangement. The selector updates an internal copy unless you also pass
 * [onLayoutChange] and keep passing the new value back in.
 * @param onLayoutChange Notified when the viewer picks another layout.
 * @param onVoiceSearch When non-null, a voice button is shown and this is invoked on press.
 * @param voiceSearchLabel Accessibility label for the voice button.
 * @param searchLabel Label for the primary search key.
 * @param spaceLabel Label for the space key.
 * @param deleteLabel Label for the delete key.
 * @param clearLabel Label for the clear key and the control inside the query field.
 * @param shiftLabel Label for the shift key. Shift stays on until it is pressed again.
 * @param layoutLabels English labels for the layout switcher.
 * @param showLayoutSelector When false, [layout] is fixed and the switcher is hidden.
 * @param suggestions Optional content for the column to the right of the keyboard.
 * @param colors Key, field, and accent colors. Defaults suit a dark television UI.
 * @param shapes Corner shapes for keys, the field, and the search key.
 * @param requestInitialFocus When true, focus starts on the first letter key.
 */
@Composable
fun TvSearchKeyboard(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search",
    layout: TvSearchKeyboardLayout = TvSearchKeyboardLayout.Alphabetical,
    onLayoutChange: ((TvSearchKeyboardLayout) -> Unit)? = null,
    onVoiceSearch: (() -> Unit)? = null,
    voiceSearchLabel: String = "Voice search",
    searchLabel: String = "Search",
    spaceLabel: String = "Space",
    deleteLabel: String = "Delete",
    clearLabel: String = "Clear",
    shiftLabel: String = "Shift",
    layoutLabels: TvSearchKeyboardLayoutLabels = TvSearchKeyboardLayoutLabels(),
    showLayoutSelector: Boolean = true,
    suggestions: (@Composable () -> Unit)? = null,
    colors: TvSearchKeyboardColors = TvSearchKeyboardDefaults.colors(),
    shapes: TvSearchKeyboardShapes = TvSearchKeyboardDefaults.shapes(),
    requestInitialFocus: Boolean = true,
) {
    var currentLayout by remember { mutableStateOf(layout) }
    var shifted by remember { mutableStateOf(false) }
    LaunchedEffect(layout) { currentLayout = layout }
    val keyRows = rowsFor(currentLayout)
    val firstKey = remember { FocusRequester() }

    MaterialTheme(
        colorScheme = darkColorScheme(
            surface = Color(0xFF0E1118),
            onSurface = colors.keyContent,
            inverseSurface = colors.keyFocusedContainer,
            inverseOnSurface = colors.keyFocusedContent,
            border = colors.actionBorder,
        ),
    ) {
        Row(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val metrics = keyboardMetrics(
                        columnHeight = maxHeight,
                        glyphRows = keyRows.rows.size,
                        showLayouts = showLayoutSelector,
                    )
                    Column(Modifier.fillMaxSize()) {
                        QueryRow(
                            query = query,
                            placeholder = placeholder,
                            onQueryChange = onQueryChange,
                            onSearch = onSearch,
                            onVoiceSearch = onVoiceSearch,
                            voiceSearchLabel = voiceSearchLabel,
                            searchLabel = searchLabel,
                            clearLabel = clearLabel,
                            colors = colors,
                            shapes = shapes,
                        )
                        Spacer(Modifier.height(SectionGap))
                        if (showLayoutSelector) {
                            LayoutSelector(
                                current = currentLayout,
                                labels = layoutLabels,
                                colors = colors,
                                shapes = shapes,
                                onSelect = { next ->
                                    currentLayout = next
                                    onLayoutChange?.invoke(next)
                                },
                            )
                            Spacer(Modifier.height(SectionGap))
                        }
                        GlyphGrid(
                            keyRows = keyRows,
                            shifted = shifted,
                            metrics = metrics,
                            firstKey = firstKey,
                            colors = colors,
                            shapes = shapes,
                            onGlyph = { glyph ->
                                onQueryChange(query + glyph.rendered(shifted))
                            },
                        )
                        Spacer(Modifier.height(SectionGap))
                        ActionRow(
                            height = metrics.keyHeight,
                            shifted = shifted,
                            onShift = { shifted = !shifted },
                            onSpace = {
                                if (query.isNotEmpty() && !query.endsWith(" ")) {
                                    onQueryChange("$query ")
                                }
                            },
                            onDelete = { onQueryChange(deleteLastCodePoint(query)) },
                            onClear = { onQueryChange("") },
                            shiftLabel = shiftLabel,
                            spaceLabel = spaceLabel,
                            deleteLabel = deleteLabel,
                            clearLabel = clearLabel,
                            colors = colors,
                            shapes = shapes,
                        )
                    }
                }
            }
            if (suggestions != null) {
                Spacer(Modifier.width(32.dp))
                Box(
                    modifier = Modifier
                        .weight(1.22f)
                        .fillMaxHeight(),
                ) {
                    suggestions()
                }
            }
        }
    }

    if (requestInitialFocus) {
        LaunchedEffect(Unit) {
            firstKey.requestFocus()
        }
    }
}

@Composable
private fun QueryRow(
    query: String,
    placeholder: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    onVoiceSearch: (() -> Unit)?,
    voiceSearchLabel: String,
    searchLabel: String,
    clearLabel: String,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(QueryHeight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        if (onVoiceSearch != null) {
            ActionKey(
                label = "",
                onClick = onVoiceSearch,
                modifier = Modifier
                    .size(QueryHeight)
                    .semantics { contentDescription = voiceSearchLabel },
                colors = colors,
                shapes = shapes,
                shape = shapes.field,
            ) {
                MicrophoneIcon(Modifier.size(22.dp))
            }
        }
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            shape = shapes.field,
            colors = SurfaceDefaults.colors(
                containerColor = colors.fieldContainer,
                contentColor = colors.fieldContent,
            ),
            border = Border(
                border = BorderStroke(1.dp, colors.fieldBorder),
                shape = shapes.field,
            ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                QueryText(
                    query = query,
                    placeholder = placeholder,
                    colors = colors,
                    modifier = Modifier.weight(1f),
                )
                if (query.isNotEmpty()) {
                    Spacer(Modifier.width(12.dp))
                    ActionKey(
                        label = clearLabel,
                        onClick = { onQueryChange("") },
                        modifier = Modifier
                            .height(36.dp)
                            .width(88.dp),
                        colors = colors,
                        shapes = shapes,
                        shape = RoundedCornerShape(8.dp),
                        fontSizeSp = 14,
                    )
                }
            }
        }
        PrimaryKey(
            label = searchLabel,
            onClick = { onSearch(query) },
            modifier = Modifier
                .width(132.dp)
                .fillMaxHeight(),
            colors = colors,
            shapes = shapes,
        )
    }
}

@Composable
private fun QueryText(
    query: String,
    placeholder: String,
    colors: TvSearchKeyboardColors,
    modifier: Modifier = Modifier,
) {
    val scroll = rememberScrollState()
    LaunchedEffect(query) {
        scroll.scrollTo(scroll.maxValue)
    }
    Box(modifier = modifier.fillMaxHeight(), contentAlignment = Alignment.CenterStart) {
        if (query.isEmpty()) {
            Text(
                text = placeholder,
                color = colors.fieldPlaceholder,
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scroll),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (query.isNotEmpty()) {
                Text(
                    text = query,
                    color = colors.fieldContent,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1,
                )
            }
            Caret(color = colors.caret)
        }
    }
}

@Composable
private fun Caret(color: Color) {
    val transition = rememberInfiniteTransition(label = "caret")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 520),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "caretAlpha",
    )
    Canvas(
        modifier = Modifier
            .padding(start = 2.dp)
            .size(width = 2.dp, height = 28.dp),
    ) {
        drawRect(color = color.copy(alpha = alpha))
    }
}

@Composable
private fun LayoutSelector(
    current: TvSearchKeyboardLayout,
    labels: TvSearchKeyboardLayoutLabels,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
    onSelect: (TvSearchKeyboardLayout) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TvSearchKeyboardLayout.entries.forEach { option ->
            val selected = option == current
            Surface(
                selected = selected,
                onClick = { onSelect(option) },
                modifier = Modifier.height(ChipHeight),
                shape = SelectableSurfaceDefaults.shape(shape = shapes.chip),
                colors = SelectableSurfaceDefaults.colors(
                    containerColor = colors.chipContainer,
                    contentColor = colors.chipContent,
                    focusedContainerColor = colors.keyFocusedContainer,
                    focusedContentColor = colors.keyFocusedContent,
                    pressedContainerColor = colors.keyFocusedContainer,
                    pressedContentColor = colors.keyFocusedContent,
                    selectedContainerColor = colors.chipSelectedContainer,
                    selectedContentColor = colors.keyContent,
                    focusedSelectedContainerColor = colors.keyFocusedContainer,
                    focusedSelectedContentColor = colors.keyFocusedContent,
                    pressedSelectedContainerColor = colors.keyFocusedContainer,
                    pressedSelectedContentColor = colors.keyFocusedContent,
                ),
                scale = SelectableSurfaceDefaults.scale(
                    focusedScale = 1.04f,
                    focusedSelectedScale = 1.04f,
                    pressedScale = 1.02f,
                ),
                border = SelectableSurfaceDefaults.border(
                    border = Border.None,
                    focusedBorder = Border.None,
                    selectedBorder = Border(
                        border = BorderStroke(1.5.dp, colors.chipSelectedBorder),
                        shape = shapes.chip,
                    ),
                    focusedSelectedBorder = Border.None,
                ),
                glow = SelectableSurfaceDefaults.glow(
                    glow = Glow.None,
                    focusedGlow = Glow.None,
                    pressedGlow = Glow.None,
                    selectedGlow = Glow.None,
                    focusedSelectedGlow = Glow.None,
                    pressedSelectedGlow = Glow.None,
                ),
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = labels.labelFor(option),
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        fontFamily = FontFamily.SansSerif,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}

@Composable
private fun GlyphGrid(
    keyRows: KeyRows,
    shifted: Boolean,
    metrics: KeyboardMetrics,
    firstKey: FocusRequester,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
    onGlyph: (Glyph) -> Unit,
) {
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val gap = 8.dp
        val keyWidth = (maxWidth - gap * (keyRows.columns - 1)) / keyRows.columns
        Column(verticalArrangement = Arrangement.spacedBy(metrics.rowGap)) {
            keyRows.rows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(gap, Alignment.CenterHorizontally),
                ) {
                    row.forEachIndexed { columnIndex, glyph ->
                        val focusModifier = if (rowIndex == 0 && columnIndex == 0) {
                            Modifier.focusRequester(firstKey)
                        } else {
                            Modifier
                        }
                        LetterKey(
                            label = glyph.rendered(shifted),
                            onClick = { onGlyph(glyph) },
                            modifier = focusModifier
                                .width(keyWidth)
                                .height(metrics.keyHeight),
                            colors = colors,
                            shapes = shapes,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionRow(
    height: Dp,
    shifted: Boolean,
    onShift: () -> Unit,
    onSpace: () -> Unit,
    onDelete: () -> Unit,
    onClear: () -> Unit,
    shiftLabel: String,
    spaceLabel: String,
    deleteLabel: String,
    clearLabel: String,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        SelectableActionKey(
            label = shiftLabel,
            selected = shifted,
            onClick = onShift,
            modifier = Modifier
                .weight(1.15f)
                .fillMaxHeight(),
            colors = colors,
            shapes = shapes,
        )
        ActionKey(
            label = spaceLabel,
            onClick = onSpace,
            modifier = Modifier
                .weight(2.4f)
                .fillMaxHeight(),
            colors = colors,
            shapes = shapes,
        )
        ActionKey(
            label = deleteLabel,
            onClick = onDelete,
            modifier = Modifier
                .weight(1.25f)
                .fillMaxHeight(),
            colors = colors,
            shapes = shapes,
        )
        ActionKey(
            label = clearLabel,
            onClick = onClear,
            modifier = Modifier
                .weight(1.15f)
                .fillMaxHeight(),
            colors = colors,
            shapes = shapes,
        )
    }
}

@Composable
private fun LetterKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(shape = shapes.key),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = colors.keyContainer,
            contentColor = colors.keyContent,
            focusedContainerColor = colors.keyFocusedContainer,
            focusedContentColor = colors.keyFocusedContent,
            pressedContainerColor = colors.keyFocusedContainer,
            pressedContentColor = colors.keyFocusedContent,
        ),
        scale = ClickableSurfaceDefaults.scale(
            focusedScale = FocusedScale,
            pressedScale = PressedScale,
        ),
        border = ClickableSurfaceDefaults.border(
            border = Border.None,
            focusedBorder = Border.None,
            pressedBorder = Border.None,
            disabledBorder = Border.None,
            focusedDisabledBorder = Border.None,
        ),
        glow = ClickableSurfaceDefaults.glow(
            glow = Glow.None,
            focusedGlow = Glow.None,
            pressedGlow = Glow.None,
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
            )
        }
    }
}

@Composable
private fun ActionKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
    shape: androidx.compose.ui.graphics.Shape = shapes.key,
    fontSizeSp: Int = 16,
    icon: (@Composable () -> Unit)? = null,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(shape = shape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = colors.actionContainer,
            contentColor = colors.actionContent,
            focusedContainerColor = colors.keyFocusedContainer,
            focusedContentColor = colors.keyFocusedContent,
            pressedContainerColor = colors.keyFocusedContainer,
            pressedContentColor = colors.keyFocusedContent,
        ),
        scale = ClickableSurfaceDefaults.scale(
            focusedScale = FocusedScale,
            pressedScale = PressedScale,
        ),
        border = ClickableSurfaceDefaults.border(
            border = Border(
                border = BorderStroke(1.dp, colors.actionBorder),
                shape = shape,
            ),
            focusedBorder = Border.None,
            pressedBorder = Border.None,
            disabledBorder = Border.None,
            focusedDisabledBorder = Border.None,
        ),
        glow = ClickableSurfaceDefaults.glow(
            glow = Glow.None,
            focusedGlow = Glow.None,
            pressedGlow = Glow.None,
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            if (icon != null) {
                icon()
            } else {
                Text(
                    text = label,
                    fontSize = fontSizeSp.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = FontFamily.SansSerif,
                    maxLines = 1,
                )
            }
        }
    }
}

@Composable
private fun SelectableActionKey(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
) {
    Surface(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        shape = SelectableSurfaceDefaults.shape(shape = shapes.key),
        colors = SelectableSurfaceDefaults.colors(
            containerColor = colors.actionContainer,
            contentColor = colors.actionContent,
            focusedContainerColor = colors.keyFocusedContainer,
            focusedContentColor = colors.keyFocusedContent,
            pressedContainerColor = colors.keyFocusedContainer,
            pressedContentColor = colors.keyFocusedContent,
            selectedContainerColor = colors.chipSelectedContainer,
            selectedContentColor = colors.keyContent,
            focusedSelectedContainerColor = colors.keyFocusedContainer,
            focusedSelectedContentColor = colors.keyFocusedContent,
            pressedSelectedContainerColor = colors.keyFocusedContainer,
            pressedSelectedContentColor = colors.keyFocusedContent,
        ),
        scale = SelectableSurfaceDefaults.scale(
            focusedScale = FocusedScale,
            focusedSelectedScale = FocusedScale,
            pressedScale = PressedScale,
        ),
        border = SelectableSurfaceDefaults.border(
            border = Border(
                border = BorderStroke(1.dp, colors.actionBorder),
                shape = shapes.key,
            ),
            focusedBorder = Border.None,
            selectedBorder = Border(
                border = BorderStroke(1.5.dp, colors.chipSelectedBorder),
                shape = shapes.key,
            ),
            focusedSelectedBorder = Border.None,
        ),
        glow = SelectableSurfaceDefaults.glow(
            glow = Glow.None,
            focusedGlow = Glow.None,
            pressedGlow = Glow.None,
            selectedGlow = Glow.None,
            focusedSelectedGlow = Glow.None,
            pressedSelectedGlow = Glow.None,
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun PrimaryKey(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier,
    colors: TvSearchKeyboardColors,
    shapes: TvSearchKeyboardShapes,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = ClickableSurfaceDefaults.shape(shape = shapes.primary),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = colors.primaryContainer,
            contentColor = colors.primaryContent,
            focusedContainerColor = colors.primaryFocusedContainer,
            focusedContentColor = colors.primaryContent,
            pressedContainerColor = colors.primaryFocusedContainer,
            pressedContentColor = colors.primaryContent,
        ),
        scale = ClickableSurfaceDefaults.scale(
            focusedScale = FocusedScale,
            pressedScale = PressedScale,
        ),
        border = ClickableSurfaceDefaults.border(
            border = Border.None,
            focusedBorder = Border.None,
            pressedBorder = Border.None,
            disabledBorder = Border.None,
            focusedDisabledBorder = Border.None,
        ),
        glow = ClickableSurfaceDefaults.glow(
            glow = Glow.None,
            focusedGlow = Glow.None,
            pressedGlow = Glow.None,
        ),
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun MicrophoneIcon(modifier: Modifier = Modifier) {
    val color = LocalContentColor.current
    Canvas(modifier) {
        val stroke = size.minDimension * 0.08f
        val headWidth = size.width * 0.38f
        val headHeight = size.height * 0.46f
        drawRoundRect(
            color = color,
            topLeft = Offset((size.width - headWidth) / 2f, size.height * 0.06f),
            size = Size(headWidth, headHeight),
            cornerRadius = CornerRadius(headWidth / 2f, headWidth / 2f),
        )
        drawArc(
            color = color,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset(size.width * 0.16f, size.height * 0.30f),
            size = Size(size.width * 0.68f, size.height * 0.42f),
            style = Stroke(width = stroke, cap = StrokeCap.Round),
        )
        drawLine(
            color = color,
            start = Offset(size.width / 2f, size.height * 0.70f),
            end = Offset(size.width / 2f, size.height * 0.86f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = color,
            start = Offset(size.width * 0.32f, size.height * 0.86f),
            end = Offset(size.width * 0.68f, size.height * 0.86f),
            strokeWidth = stroke,
            cap = StrokeCap.Round,
        )
    }
}
