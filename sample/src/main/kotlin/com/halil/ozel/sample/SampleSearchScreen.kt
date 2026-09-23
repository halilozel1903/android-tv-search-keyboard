package com.halil.ozel.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.halil.ozel.TvSearchKeyboard
import com.halil.ozel.TvSearchKeyboardLayout

private val Background = Color(0xFF0F0F0F)
private val Muted = Color(0xFFAAAAAA)
private val TitleInk = Color(0xFFF1F1F1)

private val SearchGlyph: ImageVector = ImageVector.Builder(
    name = "Search",
    defaultWidth = 24.dp,
    defaultHeight = 24.dp,
    viewportWidth = 24f,
    viewportHeight = 24f,
).addPath(
    pathData = addPathNodes(
        "M15.5,14h-0.79l-0.28,-0.27C15.41,12.59 16,11.11 16,9.5 16,5.91 13.09,3 9.5,3S3,5.91 3,9.5 " +
            "5.91,16 9.5,16c1.61,0 3.09,-0.59 4.23,-1.57l0.27,0.28v0.79l5,4.99L20.49,19l-4.99,-5z" +
            "M9.5,14C7.01,14 5,11.99 5,9.5S7.01,5 9.5,5 14,7.01 14,9.5 11.99,14 9.5,14z",
    ),
    fill = SolidColor(Color.Black),
).build()

private data class Title(
    val name: String,
    val detail: String,
)

private val Catalog = listOf(
    Title("The Bear", "Series, 2022"),
    Title("Severance", "Series, 2022"),
    Title("Shogun", "Series, 2024"),
    Title("Andor", "Series, 2022"),
    Title("Slow Horses", "Series, 2022"),
    Title("The Night Agent", "Series, 2023"),
    Title("Fallout", "Series, 2024"),
    Title("Ripley", "Series, 2024"),
    Title("True Detective", "Series, 2014"),
    Title("The Crown", "Series, 2016"),
    Title("Wednesday", "Series, 2022"),
    Title("Blue Eye Samurai", "Series, 2023"),
    Title("Dune: Part Two", "Movie, 2024"),
    Title("Oppenheimer", "Movie, 2023"),
    Title("Past Lives", "Movie, 2023"),
    Title("The Holdovers", "Movie, 2023"),
    Title("Poor Things", "Movie, 2023"),
    Title("Anatomy of a Fall", "Movie, 2023"),
    Title("Challengers", "Movie, 2024"),
    Title("Conclave", "Movie, 2024"),
    Title("Anora", "Movie, 2024"),
    Title("The Substance", "Movie, 2024"),
    Title("Nosferatu", "Movie, 2024"),
    Title("Wicked", "Movie, 2024"),
    Title("Civil War", "Movie, 2024"),
    Title("Hit Man", "Movie, 2023"),
    Title("All of Us Strangers", "Movie, 2023"),
    Title("Perfect Days", "Movie, 2023"),
)

@Composable
fun SampleSearchScreen(
    initialQuery: String = "",
    initialLayout: TvSearchKeyboardLayout = TvSearchKeyboardLayout.Alphabetical,
) {
    var query by remember { mutableStateOf(initialQuery) }
    var submitted by remember { mutableStateOf<String?>(null) }
    var voiceNote by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 48.dp, vertical = 20.dp),
    ) {
        TvSearchKeyboard(
            layout = initialLayout,
            query = query,
            onQueryChange = { next ->
                query = next
                voiceNote = false
            },
            onSearch = { submitted = it.trim() },
            placeholder = "Search",
            onVoiceSearch = { voiceNote = true },
            suggestions = {
                Suggestions(
                    query = query,
                    submitted = submitted,
                    voiceNote = voiceNote,
                    onPick = { name ->
                        query = name
                        submitted = name
                        voiceNote = false
                    },
                )
            },
        )
    }
}

@Composable
private fun Suggestions(
    query: String,
    submitted: String?,
    voiceNote: Boolean,
    onPick: (String) -> Unit,
) {
    val trimmed = query.trim()
    val matches = if (trimmed.isEmpty()) {
        Catalog
    } else {
        Catalog.filter { it.name.contains(trimmed, ignoreCase = true) }
    }
    val heading = when {
        trimmed.isEmpty() -> "Popular searches"
        submitted == trimmed && matches.isNotEmpty() -> "Results"
        else -> "Suggestions"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = heading,
            color = Muted,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 20.dp, top = 18.dp),
        )
        if (voiceNote && trimmed.isEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Voice search is not available in this sample.",
                color = Muted,
                fontSize = 15.sp,
                modifier = Modifier.padding(start = 20.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        if (matches.isEmpty()) {
            EmptyMatches()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                matches.forEach { title ->
                    SuggestionRow(title = title, query = trimmed, onPick = { onPick(title.name) })
                }
            }
        }
    }
}

@Composable
private fun EmptyMatches() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = SearchGlyph,
            contentDescription = null,
            tint = Muted,
            modifier = Modifier.size(56.dp),
        )
        Spacer(Modifier.height(20.dp))
        Text(
            text = "No matching titles",
            color = TitleInk,
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Try a different spelling or fewer letters.",
            color = Muted,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun SuggestionRow(
    title: Title,
    query: String,
    onPick: () -> Unit,
) {
    val shape = RoundedCornerShape(percent = 50)
    Surface(
        onClick = onPick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = ClickableSurfaceDefaults.shape(shape = shape),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color.Transparent,
            contentColor = TitleInk,
            focusedContainerColor = Color.White,
            focusedContentColor = Background,
            pressedContainerColor = Color.White,
            pressedContentColor = Background,
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.02f, pressedScale = 1f),
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
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val secondary = LocalContentColor.current.copy(alpha = 0.6f)
            Icon(
                imageVector = SearchGlyph,
                contentDescription = null,
                tint = secondary,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(20.dp))
            Text(
                text = highlightCompletion(title.name, query),
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(16.dp))
            Text(
                text = title.detail,
                color = secondary,
                fontSize = 14.sp,
                maxLines = 1,
            )
        }
    }
}

/** Like YouTube, the typed part stays regular and the rest of the suggestion is bold. */
private fun highlightCompletion(name: String, query: String): AnnotatedString {
    val start = if (query.isEmpty()) -1 else name.indexOf(query, ignoreCase = true)
    if (start < 0) {
        return AnnotatedString(name)
    }
    val end = start + query.length
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(name.substring(0, start)) }
        append(name.substring(start, end))
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(name.substring(end)) }
    }
}
