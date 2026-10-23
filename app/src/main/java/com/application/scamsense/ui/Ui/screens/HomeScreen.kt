package com.application.scamsense.ui.Ui.screens


import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.AppCard
import com.application.scamsense.ui.Ui.components.GradientButton
import com.application.scamsense.ui.Ui.components.PulsingShield
import com.application.scamsense.ui.Ui.components.SoftButton
import com.application.scamsense.ui.Ui.components.StaggeredEntrance
import com.application.scamsense.ui.theme.Mint

@Composable
fun HomeScreen(
    vm: AppViewModel,
    onScan: () -> Unit,
    onHistory: () -> Unit,
    onReports: () -> Unit,
    onSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StaggeredEntrance(0) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                PulsingShield()
                Spacer(Modifier.height(12.dp))
                Text("InternGuard", style = MaterialTheme.typography.displayMedium)
                Text(
                    "Check internship offers and recruiter messages for scam warning signs before you pay or share anything.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }

        StaggeredEntrance(1) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                MonitoringChip(vm.monitoringEnabled)
            }
        }

        StaggeredEntrance(2) {
            GradientButton("Scan a message", onScan, Modifier.fillMaxWidth(), icon = Icons.Filled.Search)
        }

        StaggeredEntrance(3) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SoftButton("History", onHistory, Modifier.weight(1f), Icons.Filled.History)
                SoftButton("Reported", onReports, Modifier.weight(1f), Icons.Filled.Flag)
            }
        }

        StaggeredEntrance(4) {
            AppCard(Modifier.fillMaxWidth(), onClick = onSettings) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(Icons.Filled.Lock, null, tint = MaterialTheme.colorScheme.primary)
                    Column {
                        Text("Privacy & settings", style = MaterialTheme.typography.titleMedium)
                        Text(
                            "In this version, analysis runs on your device. Tap to manage what is saved.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MonitoringChip(enabled: Boolean) {
    val dot by animateColorAsState(
        if (enabled) Mint else MaterialTheme.colorScheme.onSurfaceVariant, label = "dot"
    )
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier
            .size(10.dp)
            .background(dot, CircleShape))
        Text(
            "Notification monitoring: ${if (enabled) "On" else "Off"}",
            style = MaterialTheme.typography.labelSmall
        )
    }
}
