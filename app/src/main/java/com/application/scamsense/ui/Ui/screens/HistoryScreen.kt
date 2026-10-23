package com.application.scamsense.ui.Ui.screens


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.scamsense.model.AnalysisResult
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.AppCard
import com.application.scamsense.ui.Ui.components.RiskBadge
import com.application.scamsense.ui.Ui.components.ScreenHeader
import com.application.scamsense.ui.Ui.components.SoftButton
import com.application.scamsense.ui.theme.Rose
import com.application.scamsense.util.formatDate

@Composable
fun HistoryScreen(vm: AppViewModel) {
    LazyColumn(
        Modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { ScreenHeader("History", "Your saved analyses. Tap one to expand.") }
        if (vm.history.isEmpty()) {
            item { EmptyState("No saved analyses yet", "Scan a message and tap Save to history.") }
        }
        items(vm.history, key = { it.id }) { item ->
            HistoryCard(item, onNote = { vm.updateNote(item.id, it) }, onDelete = { vm.deleteHistory(item.id) },
                modifier = Modifier.animateItem())
        }
    }
}

@Composable
private fun HistoryCard(item: AnalysisResult, onNote: (String) -> Unit, onDelete: () -> Unit, modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    var note by remember(item.id) { mutableStateOf(item.note) }
    AppCard(modifier.fillMaxWidth(), onClick = { expanded = !expanded }) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween) {
            RiskBadge(item.riskLevel)
            Text(formatDate(item.timestamp), style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(item.classification, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 8.dp))
        Text("${item.indicators.size} indicator(s) • score ${item.riskScore}/100",
            style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

        AnimatedVisibility(expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Column(Modifier.padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                item.indicators.forEach { Text("• ${it.title}", style = MaterialTheme.typography.bodyMedium) }
                item.messageText?.let {
                    Text("Saved message:", style = MaterialTheme.typography.labelSmall)
                    Text(it, style = MaterialTheme.typography.bodyMedium, maxLines = 6)
                }
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it; onNote(it) },
                    label = { Text("Your note (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
                SoftButton("Delete", onDelete, Modifier.fillMaxWidth(), Icons.Filled.Delete, tint = Rose)
            }
        }
    }
}

@Composable
fun EmptyState(title: String, body: String) {
    Column(Modifier.fillMaxWidth().padding(vertical = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Filled.History, null, tint = MaterialTheme.colorScheme.outline, modifier = Modifier.padding(bottom = 8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
        Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
