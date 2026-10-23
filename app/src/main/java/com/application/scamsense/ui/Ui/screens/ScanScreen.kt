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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import com.application.scamsense.ui.Ui.AppViewModel
import com.application.scamsense.ui.Ui.components.GradientButton
import com.application.scamsense.ui.Ui.components.ScreenHeader
import com.application.scamsense.ui.Ui.components.SoftButton

// Clearly fake demo text for testing the UI. NOT real data.
private const val SAMPLE =
    "Congratulations! You have been selected for our Work From Home internship. " +
            "Stipend Rs 40,000 per week, no interview needed. Pay a refundable registration fee " +
            "within 2 hours to confirm your seat. Send your Aadhaar and bank account details at bit.ly/demo-offer"

@Composable
fun ScanScreen(vm: AppViewModel, onResult: () -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    // LocalClipboardManager is deprecated in newer Compose (warning only). Fine for now.
    val clipboard = LocalClipboardManager.current

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        ScreenHeader("Scan a message", "Paste an internship or recruiter message below.")

        OutlinedTextField(
            value = text,
            onValueChange = { text = it; vm.clearScanError() },
            modifier = Modifier.fillMaxWidth(),
            minLines = 7,
            maxLines = 12,
            shape = RoundedCornerShape(16.dp),
            placeholder = { Text("Paste the message here...") },
            isError = vm.scanError != null,
            supportingText = { Text("${text.length} characters") }
        )

        AnimatedVisibility(
            visible = vm.scanError != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(vm.scanError.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodyMedium)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SoftButton("Paste", { clipboard.getText()?.text?.let { text = it; vm.clearScanError() } },
                Modifier.weight(1f), Icons.Filled.ContentPaste)
            SoftButton("Clear", { text = ""; vm.clearScanError() }, Modifier.weight(1f), Icons.Filled.Delete)
        }

        SoftButton("Try a demo message", { text = SAMPLE; vm.clearScanError() }, Modifier.fillMaxWidth())

        GradientButton(
            text = if (vm.isAnalysing) "Analysing..." else "Analyse",
            onClick = { vm.analyse(text, onResult) },
            modifier = Modifier.fillMaxWidth(),
            icon = Icons.Filled.Check,
            loading = vm.isAnalysing
        )
    }
}
