package com.github.eneszel.tvsearchkeyboard.sample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.Glow
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.github.eneszel.tvsearchkeyboard.TvSearchKeyboard

private val Ink = Color(0xFF07090E)
private val Muted = Color(0xFF9AA3B5)
private val TitleInk = Color(0xFFF4F6FB)

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
fun SampleSearchScreen() {
    var query by remember { mutableStateOf("") }
    var submitted by remember { mutableStateOf<String?>(null) }
    var voiceNote by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Ink),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFF1A2740), Color(0xFF0A0E16), Ink),
                        radius = 1400f,
                    ),
                ),
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 48.dp, vertical = 20.dp),
        ) {
            TvSearchKeyboard(
                query = query,
                onQueryChange = { next ->
                    query = next
                    voiceNote = false
                },
                onSearch = { submitted = it.trim() },
                placeholder = "Search movies and shows",
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
        trimmed.isEmpty() -> "Popular"
        submitted == trimmed && matches.isNotEmpty() -> "Results"
        else -> "Suggestions"
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = heading,
            color = TitleInk,
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
        )
        if (voiceNote && trimmed.isEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                text = "Voice search is not available in this sample.",
                color = Muted,
                fontSize = 15.sp,
            )
        }
        Spacer(Modifier.height(14.dp))
        if (matches.isEmpty()) {
            EmptyMatches()
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                matches.forEach { title ->
                    SuggestionRow(title = title, onPick = { onPick(title.name) })
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
            .padding(top = 36.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "No matching titles",
            color = TitleInk,
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Nothing in this catalog matches that spelling.",
            color = Muted,
            fontSize = 16.sp,
        )
    }
}

@Composable
private fun SuggestionRow(
    title: Title,
    onPick: () -> Unit,
) {
    Surface(
        onClick = onPick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp),
        shape = ClickableSurfaceDefaults.shape(shape = RoundedCornerShape(10.dp)),
        colors = ClickableSurfaceDefaults.colors(
            containerColor = Color(0xFF141821),
            contentColor = TitleInk,
            focusedContainerColor = Color(0xFFF4F6FB),
            focusedContentColor = Color(0xFF12141A),
            pressedContainerColor = Color(0xFFF4F6FB),
            pressedContentColor = Color(0xFF12141A),
        ),
        scale = ClickableSurfaceDefaults.scale(focusedScale = 1.03f, pressedScale = 1.01f),
        border = ClickableSurfaceDefaults.border(
            border = androidx.tv.material3.Border.None,
            focusedBorder = androidx.tv.material3.Border.None,
            pressedBorder = androidx.tv.material3.Border.None,
            disabledBorder = androidx.tv.material3.Border.None,
            focusedDisabledBorder = androidx.tv.material3.Border.None,
        ),
        glow = ClickableSurfaceDefaults.glow(
            glow = Glow.None,
            focusedGlow = Glow.None,
            pressedGlow = Glow.None,
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
            )
            val meta = LocalContentColor.current.copy(alpha = 0.62f)
            Text(
                text = title.detail,
                color = meta,
                fontSize = 13.sp,
            )
        }
    }
}
