package com.application.scamsense.ui.Ui.screens



import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.AppCard
import com.application.scamsense.ui.Ui.components.GradientButton
import com.application.scamsense.ui.Ui.components.RiskBadge
import com.application.scamsense.ui.Ui.components.RiskGauge
import com.application.scamsense.ui.Ui.components.SoftButton
import com.application.scamsense.ui.Ui.components.StaggeredEntrance
import com.application.scamsense.ui.theme.Amber
import com.application.scamsense.ui.theme.Rose

private val REPORT_REASONS = listOf(
    "Asked me to pay a fee",
    "Suspicious link",
    "Asked for sensitive information",
    "Fake company or recruiter",
    "Other"
)

@Composable
fun ResultScreen(vm: AppViewModel, onBack: () -> Unit) {
    val result = vm.currentResult
    if (result == null) {
        LaunchedEffect(Unit) { onBack() }
        return
    }
    var showReport by remember { mutableStateOf(false) }

    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(horizontal = 20.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, "Back") }
            Text("Result", style = MaterialTheme.typography.titleLarge)
        }

        RiskGauge(result.riskScore, result.riskLevel)
        RiskBadge(result.riskLevel)
        Text(result.classification, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(
            "Verify independently. This is a risk indicator, not proof.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        StaggeredEntrance(0) {
            AppCard(Modifier.fillMaxWidth()) {
                Text("Why this result", style = MaterialTheme.typography.titleMedium)
                Text(result.explanation, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
            }
        }

        if (result.indicators.isNotEmpty()) {
            Text("Suspicious indicators", style = MaterialTheme.typography.titleMedium, modifier = Modifier.fillMaxWidth())
            result.indicators.forEachIndexed { i, ind ->
                StaggeredEntrance(i + 1) {
                    AppCard(Modifier.fillMaxWidth()) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(Icons.Filled.Warning, null, tint = Amber, modifier = Modifier.size(22.dp))
                            Column {
                                Text(ind.title, style = MaterialTheme.typography.titleMedium)
                                Text(ind.explanation, style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }

        StaggeredEntrance(result.indicators.size + 1) {
            AppCard(Modifier.fillMaxWidth()) {
                Text("Suggested next steps", style = MaterialTheme.typography.titleMedium)
                result.nextSteps.forEach {
                    Text("•  $it", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 6.dp))
                }
            }
        }

        GradientButton(
            text = if (vm.currentSaved) "Saved to history" else "Save to history",
            onClick = { vm.saveCurrent() },
            modifier = Modifier.fillMaxWidth(),
            icon = if (vm.currentSaved) Icons.Filled.Check else Icons.Filled.Save,
            enabled = !vm.currentSaved
        )
        SoftButton(
            text = if (vm.currentReported) "Reported" else "Report as suspected scam",
            onClick = { showReport = true },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Flag,
            tint = Rose,
            enabled = !vm.currentReported
        )
    }

    if (showReport) {
        var reason by remember { mutableStateOf(REPORT_REASONS[0]) }
        AlertDialog(
            onDismissRequest = { showReport = false },
            title = { Text("Report this message") },
            text = {
                Column {
                    Text(
                        "Reports are stored on this device and are not treated as confirmed scams.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    REPORT_REASONS.forEach { r ->
                        Row(
                            Modifier.fillMaxWidth().selectable(r == reason, onClick = { reason = r }, role = Role.RadioButton),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = r == reason, onClick = null, modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp))
                            Text(r, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { vm.reportCurrent(reason); showReport = false }) { Text("Submit") } },
            dismissButton = { TextButton(onClick = { showReport = false }) { Text("Cancel") } }
        )
    }
}
