package com.example.musicplaylistapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
            MusicApp()
        }
    }
}

@Composable
fun MusicApp() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Main) }
    val songs = remember { mutableStateListOf(
        Song(1, "Bohemian Rhapsody", "Queen", 4.8f, listOf("Masterpiece!", "Best song ever")),
        Song(2, "Imagine", "John Lennon", 4.6f, listOf("Timeless classic")),
        Song(3, "Hotel California", "Eagles", 4.7f, listOf("Iconic guitar solo"))
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

@SuppressLint("ModifierFactoryUnreferencedReceiver")
private fun Modifier.padding(
    dp: Dp,
    function: @Composable () -> Unit
): Modifier {


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
            {
                // Action buttons at top
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onAddClick) {
                        Text("Add to Playlist")
                    }

                    Button(onClick = onSecondScreenClick) {
                        Text("Second Screen")
                    }

                    Button(onClick = { (context as ComponentActivity).finish() }) {
                        Text("Exit")
                    }
                }

                // Song list
                LazyColumn {
                    items(songs) { song ->
                        SongItem(song = song, onClick = { onSongClick(song) })
                    }
                }
            },
        verticalArrangement = TODO(),
        horizontalAlignment = TODO(),
        content = TODO()
}

@Composable
fun SongItem(song: Song, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = song.title, fontWeight = FontWeight.Bold)
            Text(text = "Artist: ${song.artist}")
            Text(text = "Rating: ${song.rating}")
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
                // Calculate average using loop
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

        // Display average rating when calculated
        if (showAverage) {
            Text(
                text = "Average Rating: ${"%.1f".format(averageRating)}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display song list when visible
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

        // Back button at bottom
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
    MusicApp()
}
