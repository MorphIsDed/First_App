package com.example.mynavigationapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

class ClosingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize MediaPlayer to play closing sound
        val mediaPlayer = MediaPlayer.create(this, R.raw.closing_sound)
        mediaPlayer.start()

        setContent {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Goodbye!",
                    fontSize = 28.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Release MediaPlayer when this composable leaves
            DisposableEffect(Unit) {
                onDispose {
                    mediaPlayer.release()
                }
            }
        }

        // Delay 2 seconds before closing the app completely
        Handler(Looper.getMainLooper()).postDelayed({
            finishAffinity() // Closes all activities and exits the app
        }, 2000)
    }
}