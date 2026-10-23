package com.application.scamsense.ui.Ui.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.AppCard
import com.application.scamsense.ui.Ui.components.ScreenHeader
import com.application.scamsense.ui.theme.Amber
import com.application.scamsense.util.formatDate

@Composable
fun ReportsScreen(vm: AppViewModel) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            ScreenHeader(
                "Reported messages",
                "Reports stay on this device. A report is not a confirmed scam and is not used as a training label."
            )
        }
        if (vm.reports.isEmpty()) {
            item { EmptyState("No reports yet", "Use \"Report as suspected scam\" on a result screen.") }
        }
        items(vm.reports, key = { it.id }) { r ->
            AppCard(Modifier.fillMaxWidth().animateItem()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        r.status,
                        style = MaterialTheme.typography.labelSmall,
                        color = Amber,
                        modifier = Modifier.clip(CircleShape).background(Amber.copy(alpha = 0.15f))
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    )
                    IconButton(onClick = { vm.deleteReport(r.id) }) { Icon(Icons.Filled.Delete, "Delete report") }
                }
                Text(r.reason, style = MaterialTheme.typography.titleMedium)
                Text(formatDate(r.timestamp), style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
