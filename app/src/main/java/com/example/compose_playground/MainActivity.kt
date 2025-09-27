package com.example.compose_playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose_playground.ui.theme.ComposeplaygroundTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeplaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreenContent(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Main Screen

@Composable
fun MainScreenContent(modifier: Modifier = Modifier) {

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Calling your custom Dual Ring Composable with example data
        DualRingProgressUI(
            currentSteps = 5310,
            targetSteps = 8000,
            darkRingProgressFraction = 0.7f
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    ComposeplaygroundTheme {
        MainScreenContent()
    }
}

//DUAL RING UI

// Define standard colors and values
val ColorDark = Color(0xFF1B4F48)
val ColorLight = Color(0xFF8BC34A)
val ColorTrack = Color(0xFFE0E0E0).copy(alpha = 0.5f)

val MAX_ANGLE = 360f
val START_ANGLE = 270f
val STROKE_WIDTH = 18.dp
val RING_GAP = 6.dp

@Composable
fun DualRingProgressUI(
    currentSteps: Int,
    targetSteps: Int,
    darkRingProgressFraction: Float
) {
    // --- ANIMATION & PROGRESS CALCULATIONS ---
    val mainProgressFraction = (currentSteps.toFloat() / targetSteps).coerceIn(0f, 1f)

    // Animate the main progress for a smooth visual effect
    val innerSweepAngle: Float by animateFloatAsState(
        targetValue = mainProgressFraction * MAX_ANGLE,
        animationSpec = tween(durationMillis = 800)
    )

    val outerSweepAngle: Float = darkRingProgressFraction.coerceIn(0f, 1f) * MAX_ANGLE

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(240.dp)
    ) {
        // --- CANVAS: Draw the rings ---
        Canvas(modifier = Modifier.matchParentSize()) {
            val canvasSize = size.minDimension
            val strokePx = STROKE_WIDTH.toPx()
            val gapPx = RING_GAP.toPx()

            // CALCULATE RADII
            val outerRadius = (canvasSize - strokePx) / 2f
            val innerRadius = outerRadius - strokePx - gapPx

            val outerStroke = Stroke(width = strokePx, cap = StrokeCap.Round)
            val innerStroke = Stroke(width = strokePx, cap = StrokeCap.Round)

            // ------------------------------------
            // A. Outer Ring Track (Dark Track)
            drawArc(
                color = ColorDark.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = MAX_ANGLE,
                useCenter = false,
                style = outerStroke,
                size = Size(outerRadius * 2, outerRadius * 2),
                topLeft = Offset(strokePx / 2, strokePx / 2)
            )

            // B. Outer Ring Progress (Dark Green Progress)
            drawArc(
                color = ColorDark,
                startAngle = START_ANGLE,
                sweepAngle = outerSweepAngle,
                useCenter = false,
                style = outerStroke,
                size = Size(outerRadius * 2, outerRadius * 2),
                topLeft = Offset(strokePx / 2, strokePx / 2)
            )

            // ------------------------------------
            // C. Inner Ring Track (Light Gray Track)
            drawArc(
                color = ColorTrack,
                startAngle = 0f,
                sweepAngle = MAX_ANGLE,
                useCenter = false,
                style = innerStroke,
                size = Size(innerRadius * 2, innerRadius * 2),
                topLeft = Offset(
                    x = canvasSize / 2 - innerRadius,
                    y = canvasSize / 2 - innerRadius
                )
            )

            // D. Inner Ring Progress (Light Green Progress)
            drawArc(
                color = ColorLight,
                startAngle = START_ANGLE,
                sweepAngle = innerSweepAngle,
                useCenter = false,
                style = innerStroke,
                size = Size(innerRadius * 2, innerRadius * 2),
                topLeft = Offset(
                    x = canvasSize / 2 - innerRadius,
                    y = canvasSize / 2 - innerRadius
                )
            )
        } // End Canvas

        // --- CENTER CONTENT: Text and Icons ---
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Begin",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentSteps.toString(),
                fontSize = 50.sp,
                fontWeight = FontWeight.Black,
                color = ColorDark
            )
            Text(
                text = "Target: $targetSteps steps",
                fontSize = 16.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Shoe Icon (e.g., DirectionsRun)
                Icon(
                    imageVector = Icons.Filled.DirectionsRun,
                    contentDescription = "Steps",
                    tint = ColorLight, // Light green color
                    modifier = Modifier.size(20.dp)
                )
                // Star Icon
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Goal Achieved",
                    tint = ColorDark, // Dark color
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewProgressUI() {

    DualRingProgressUI(
        currentSteps = 6000,
        targetSteps = 10000,
        darkRingProgressFraction = 0.7f
    )
}