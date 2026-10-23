package com.application.scamsense.ui.Ui.components



import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.application.scamsense.model.RiskLevel
import com.application.scamsense.ui.theme.Indigo
import com.application.scamsense.ui.theme.Violet
import com.application.scamsense.ui.theme.color
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun ScreenHeader(title: String, subtitle: String? = null) {
    Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** Fades + slides content up after index * 80 ms. Gives lists a "cascade" entrance. */
@Composable
fun StaggeredEntrance(index: Int, modifier: Modifier = Modifier.fillMaxWidth(), content: @Composable () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 80L)
        visible = true
    }
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(tween(400)) + slideInVertically(tween(400)) { it / 4 }
    ) { content() }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val pressed by source.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed && onClick != null) 0.98f else 1f, label = "cardScale")
    val shape = RoundedCornerShape(20.dp)
    val clickMod = if (onClick != null) {
        Modifier.clickable(interactionSource = source, indication = null, onClick = onClick)
    } else Modifier
    Column(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.4f), shape)
            .then(clickMod)
            .padding(16.dp),
        content = content
    )
}

@Composable
fun RiskBadge(level: RiskLevel, modifier: Modifier = Modifier) {
    val c = level.color()
    Text(
        text = level.label,
        color = c,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .clip(CircleShape)
            .background(c.copy(alpha = 0.15f))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}

/** Hero logo placeholder: a shield with a breathing glow ring. */
@Composable
fun PulsingShield(modifier: Modifier = Modifier) {
    val t = rememberInfiniteTransition(label = "pulse")
    val ring by t.animateFloat(
        initialValue = 0.9f, targetValue = 1.25f,
        animationSpec = infiniteRepeatable(tween(1800, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "ring"
    )
    val ringAlpha by t.animateFloat(
        initialValue = 0.35f, targetValue = 0.05f,
        animationSpec = infiniteRepeatable(tween(1800), RepeatMode.Reverse),
        label = "ringAlpha"
    )
    Box(modifier.size(120.dp), contentAlignment = Alignment.Center) {
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer { scaleX = ring; scaleY = ring; alpha = ringAlpha }
                .background(Violet, CircleShape)
        )
        Box(
            Modifier
                .size(84.dp)
                .background(Brush.linearGradient(listOf(Indigo, Violet)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Filled.Shield, contentDescription = "App logo", tint = Color.White, modifier = Modifier.size(44.dp))
        }
    }
}

/** Arc gauge that sweeps from 0 to the score when it appears. */
@Composable
fun RiskGauge(score: Int, level: RiskLevel, modifier: Modifier = Modifier.size(190.dp)) {
    var target by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(score) { target = score / 100f }
    val progress by animateFloatAsState(target, tween(1200, easing = FastOutSlowInEasing), label = "gauge")
    val track = MaterialTheme.colorScheme.surfaceVariant
    val color = level.color()
    Box(modifier, contentAlignment = Alignment.Center) {
        Canvas(Modifier.fillMaxSize().padding(14.dp)) {
            val stroke = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
            drawArc(track, 135f, 270f, false, style = stroke)
            drawArc(color, 135f, 270f * progress, false, style = stroke)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${(progress * 100).roundToInt()}", style = MaterialTheme.typography.displayMedium, color = color)
            Text(
                "risk indicator",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
