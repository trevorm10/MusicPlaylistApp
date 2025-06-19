package com.example.musicplaylistapp
//ST10468338 Poogendran Trevor Moodley
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Song(
    val id: Int,
    val title: String,
    val artist: String,
    val rating: Float,
    val comments: List<String>
)

sealed class Screen {
    object Main : Screen()
    object AddSong : Screen()
    object SecondScreen : Screen()
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppTheme {
                MusicApp()
            }
        }
    }
}

@Composable
fun MusicAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFF6200EE),
            onPrimary = Color.White,
            secondary = Color(0xFF03DAC5),
            onSecondary = Color.Black,
            tertiary = Color(0xFF3700B3),
            onTertiary = Color.White,
            background = Color(0xFFF5F5F5),
            onBackground = Color.Black,
            surface = Color.White,
            onSurface = Color.Black,
        ),
        content = content
    )
}

private fun lightColorScheme(
    primary: Color,
    onPrimary: Color,
    secondary: Color,
    onSecondary: Color,
    tertiary: Color,
    onTertiary: Color,
    background: Color,
    onBackground: Color,
    surface: Color,
    onSurface: Color
): ColorScheme {
    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primary.copy(alpha = 0.2f),
        onPrimaryContainer = onPrimary,
        inversePrimary = primary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondary.copy(alpha = 0.2f),
        onSecondaryContainer = onSecondary,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiary.copy(alpha = 0.2f),
        onTertiaryContainer = onTertiary,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surface,
        onSurfaceVariant = onSurface,
        surfaceTint = primary,
        inverseSurface = background,
        inverseOnSurface = onBackground,
        error = Color(0xFFB00020),
        onError = Color.White,
        errorContainer = Color(0xFFB00020).copy(alpha = 0.2f),
        onErrorContainer = Color(0xFFB00020),
        outline = Color.Gray,
        outlineVariant = Color.LightGray,
        scrim = Color.Black.copy(alpha = 0.5f),
        surfaceBright = surface,
        surfaceDim = surface.copy(alpha = 0.8f),
        surfaceContainer = surface,
        surfaceContainerHigh = surface,
        surfaceContainerHighest = surface,
        surfaceContainerLow = surface,
        surfaceContainerLowest = surface
    )
}

@Composable
fun MusicApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    val songs = remember { mutableStateListOf(
        Song(1, "Bohemian Rhapsody", "Queen", 4.8f, listOf("Masterpiece!", "Best song ever")),
        Song(2, "Imagine", "John Lennon", 4.6f, listOf("Timeless classic")),
        Song(3, "Hotel California", "Eagles", 4.7f, listOf("Iconic guitar solo")),
    )}

    when (currentScreen) {
        Screen.Main -> MainScreen(
            songs = songs,
            onAddClick = { currentScreen = Screen.AddSong },
            onSecondScreenClick = { currentScreen = Screen.SecondScreen },
            onSongClick = { /* Handle song click if needed */ }
        )
        Screen.AddSong -> AddSongScreen(
            onAddSong = { newSong ->
                songs.add(newSong)
                currentScreen = Screen.Main
            },
            onCancel = { currentScreen = Screen.Main }
        )
        Screen.SecondScreen -> SecondScreen(
            onBack = { currentScreen = Screen.Main },
            songs = songs
        )
    }
}

@Composable
fun MainScreen(
    songs: List<Song>,
    onAddClick: () -> Unit,
    onSecondScreenClick: () -> Unit,
    onSongClick: (Song) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Action buttons at top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onAddClick) {
                Text("Add Song")
            }

            Button(onClick = onSecondScreenClick) {
                Text("Analytics")
            }

            Button(onClick = { (context as ComponentActivity).finish() }) {
                Text("Exit")
            }
        }

        //My song list
        if (songs.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("No songs in playlist. Add some songs!", fontSize = 18.sp)
            }
        } else {
            LazyColumn {
                items(songs) { song ->
                    SongItem(song = song, onClick = { onSongClick(song) })
                }
            }
        }
    }
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = song.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Artist: ${song.artist}", fontSize = 16.sp)
            Text(text = "Rating: ${song.rating}", fontSize = 16.sp)
        }
    }
}

@Composable
fun AddSongScreen(
    onAddSong: (Song) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var artist by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(3f) }
    var comment by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Add New Song", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Song Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = artist,
            onValueChange = { artist = it },
            label = { Text("Artist Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Rating: ${"%.1f".format(rating)}")
        Slider(
            value = rating,
            onValueChange = { rating = it },
            valueRange = 1f..5f,
            steps = 8,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = comment,
            onValueChange = { comment = it },
            label = { Text("Comments") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = onCancel) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val newId = (0..1000).random()
                    val newSong = Song(
                        id = newId,
                        title = title,
                        artist = artist,
                        rating = rating,
                        comments = listOf(comment)
                    )
                    onAddSong(newSong)
                },
                enabled = title.isNotBlank() && artist.isNotBlank()
            ) {
                Text("Add Song")
            }
        }
    }
}

@Composable
fun SecondScreen(
    onBack: () -> Unit,
    songs: List<Song>
) {
    var showSongs by remember { mutableStateOf(false) }
    var averageRating by remember { mutableStateOf(0f) }
    var showAverage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Playlist Analytics", style = MaterialTheme.typography.headlineLarge)

        Spacer(modifier = Modifier.height(32.dp))

        // Button to show song list
        Button(
            onClick = { showSongs = !showSongs },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showSongs) "Hide Song Details" else "Show Song Details")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button to calculate average rating
        Button(
            onClick = {
                var total = 0f
                for (song in songs) {
                    total += song.rating
                }
                averageRating = if (songs.isNotEmpty()) total / songs.size else 0f
                showAverage = true
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Calculate Average Rating")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showAverage) {
            Text(
                text = "Average Rating: ${"%.1f".format(averageRating)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showSongs) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                songs.forEach { song ->
                    SongDetailItem(song = song)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Main Screen")
        }
    }
}

@Composable
fun SongDetailItem(song: Song) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = song.title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Artist: ${song.artist}", fontSize = 16.sp)
            Text(text = "Rating: ${song.rating}", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Comments:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            song.comments.forEach { comment ->
                Text(text = "• $comment", fontSize = 14.sp, modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    MusicAppTheme {
        MusicApp()
    }
}