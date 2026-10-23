package com.application.scamsense.ui.Ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.AppCard
import com.application.scamsense.ui.Ui.components.ScreenHeader
import com.application.scamsense.ui.Ui.components.SoftButton
import com.application.scamsense.ui.Ui.components.StaggeredEntrance
import com.application.scamsense.ui.theme.Rose

@Composable
fun SettingsScreen(vm: AppViewModel) {
    var confirmDelete by remember { mutableStateOf(false) }
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader("Settings & privacy")

        StaggeredEntrance(0) {
            SettingCard(
                "Notification monitoring",
                "UI preview only. The real notification listener is added in a later phase and will need your explicit permission in Android settings.",
                vm.monitoringEnabled, vm::setMonitoring
            )
        }
        StaggeredEntrance(1) {
            SettingCard(
                "Save message text in history",
                "Off by default. When off, only the result and indicators are saved, not the original message.",
                vm.saveMessageText, onChange = vm::updateSaveMessageText
            )
        }
        StaggeredEntrance(2) {
            AppCard(Modifier.fillMaxWidth()) {
                Text("About this app", style = MaterialTheme.typography.titleMedium)
                Text(
                    "Results are risk indicators from text patterns, not proof of fraud. Always verify a company and recruiter independently.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
        StaggeredEntrance(3) {
            SoftButton("Delete all saved data", { confirmDelete = true }, Modifier.fillMaxWidth(), Icons.Filled.Delete, tint = Rose)
        }
    }

    if (confirmDelete) {
        AlertDialog(
            onDismissRequest = { confirmDelete = false },
            title = { Text("Delete all data?") },
            text = { Text("This removes your history and reports from this device.") },
            confirmButton = { TextButton(onClick = { vm.clearAllData(); confirmDelete = false }) { Text("Delete") } },
            dismissButton = { TextButton(onClick = { confirmDelete = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun SettingCard(title: String, body: String, checked: Boolean, onChange: (Boolean) -> Unit) {
    AppCard(Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(body, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Switch(checked = checked, onCheckedChange = onChange)
        }
    }
}
