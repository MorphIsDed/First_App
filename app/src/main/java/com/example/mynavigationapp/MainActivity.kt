package com.example.mynavigationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val haptic = LocalHapticFeedback.current
            var currentPage by remember { mutableStateOf(0) }

            val texts = listOf(
                "Page 1",
                "Page 2",
                "Page 3"
            )

            val startColors = listOf(
                Color(0xFF2193b0),
                Color(0xFFee9ca7),
                Color(0xFFcc2b5e)
            )

            val endColors = listOf(
                Color(0xFF6dd5ed),
                Color(0xFFffdde1),
                Color(0xFF753a88)
            )

            val animatedProgress by animateFloatAsState(
                targetValue = currentPage.toFloat(),
                animationSpec = tween(durationMillis = 700)
            )

            val lowerIndex = animatedProgress.toInt().coerceIn(0, texts.lastIndex)
            val upperIndex = (lowerIndex + 1).coerceAtMost(texts.lastIndex)
            val progressFraction = animatedProgress - lowerIndex

            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    lerp(startColors[lowerIndex], startColors[upperIndex], progressFraction),
                    lerp(endColors[lowerIndex], endColors[upperIndex], progressFraction)
                )
            )

            val displayedText = if (progressFraction < 0.5f) texts[lowerIndex] else texts[upperIndex]

            val infiniteTransition = rememberInfiniteTransition()
            val buttonScale by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = EaseInOutCubic),
                    repeatMode = RepeatMode.Reverse
                )
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
                    .padding(16.dp)
            ) {
                val screenWidth = this.maxWidth
                val screenHeight = this.maxHeight

                // Power Button with Haptics
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        finish()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(
                            end = (screenWidth.value * 0.05f).dp,
                            top = (screenHeight.value * 0.05f).dp
                        )
                        .scale(buttonScale)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PowerSettingsNew,
                        contentDescription = "Exit App",
                        tint = Color.White
                    )
                }

                // Center Text
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = displayedText,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                }

                // Navigation Buttons with Haptics
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (currentPage > 0) currentPage--
                        },
                        enabled = currentPage > 0
                    ) {
                        Text("Previous")
                    }

                    Button(
                        onClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            if (currentPage < texts.lastIndex) currentPage++
                        },
                        enabled = currentPage < texts.lastIndex
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}