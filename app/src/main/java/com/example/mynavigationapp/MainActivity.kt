import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val haptic = LocalHapticFeedback.current
            var currentPage by remember { mutableStateOf(0) }

            val texts = listOf(
                "Welcome to Page 1",
                "Here is Page 2",
                "This is the final Page 3"
            )

            val startColors = listOf(
                Color(0xFF2193b0), Color(0xFFee9ca7), Color(0xFFcc2b5e)
            )
            val endColors = listOf(
                Color(0xFF6dd5ed), Color(0xFFffdde1), Color(0xFF753a88)
            )

            val animatedProgress by animateFloatAsState(
                targetValue = currentPage.toFloat(),
                animationSpec = tween(durationMillis = 700)
            )

            val lowerIndex = animatedProgress.toInt().coerceIn(0, texts.size - 1)
            val upperIndex = (lowerIndex + 1).coerceAtMost(texts.size - 1)
            val progressFraction = animatedProgress - lowerIndex

            val gradientBrush = Brush.verticalGradient(
                colors = listOf(
                    lerpColor(startColors[lowerIndex], startColors[upperIndex], progressFraction),
                    lerpColor(endColors[lowerIndex], endColors[upperIndex], progressFraction)
                )
            )

            val displayedText = if (progressFraction < 0.5f) texts[lowerIndex] else texts[upperIndex]

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(gradientBrush)
                    .padding(16.dp)
            ) {
                // Power button at top-right
                IconButton(
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        finish()
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PowerSettingsNew,
                        contentDescription = "Power",
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
                        fontSize = 26.sp,
                        color = Color.White
                    )
                }

                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            if (currentPage > 0) {
                                currentPage--
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        },
                        enabled = currentPage > 0
                    ) {
                        Text("Prev")
                    }

                    Button(
                        onClick = {
                            if (currentPage < texts.size - 1) {
                                currentPage++
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            }
                        },
                        enabled = currentPage < texts.size - 1
                    ) {
                        Text("Next")
                    }
                }
            }
        }
    }
}

@Composable
fun lerpColor(start: Color, end: Color, progress: Float): Color {
    return Color(
        red = start.red + (end.red - start.red) * progress,
        green = start.green + (end.green - start.green) * progress,
        blue = start.blue + (end.blue - start.blue) * progress,
        alpha = 1f
    )
}