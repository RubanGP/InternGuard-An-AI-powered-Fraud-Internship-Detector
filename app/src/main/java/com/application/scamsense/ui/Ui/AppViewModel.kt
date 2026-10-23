package com.application.scamsense.ui.Ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.scamsense.classifier.FraudClassifier
import com.application.scamsense.classifier.RuleBasedClassifier
import com.application.scamsense.model.AnalysisResult
import com.application.scamsense.model.ScamReport
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val classifier: FraudClassifier = RuleBasedClassifier() // swap for the ML-backed one later

    var isAnalysing by mutableStateOf(false); private set
    var scanError by mutableStateOf<String?>(null); private set
    var currentResult by mutableStateOf<AnalysisResult?>(null); private set
    var currentSaved by mutableStateOf(false); private set

    val history = mutableStateListOf<AnalysisResult>()
    val reports = mutableStateListOf<ScamReport>()

    // UI-only for now; the real NotificationListenerService comes in a later phase.
    var monitoringEnabled by mutableStateOf(false); private set
    // Privacy: original text is NOT stored in history unless the user opts in.
    var saveMessageText by mutableStateOf(false); private set

    val currentReported: Boolean
        get() = currentResult?.let { r -> reports.any { it.analysisId == r.id } } == true

    fun analyse(text: String, onDone: () -> Unit) {
        val trimmed = text.trim()
        if (trimmed.isEmpty()) { scanError = "Please paste a message first."; return }
        if (trimmed.length < 15) { scanError = "That message is very short. Paste the full text for a better check."; return }
        scanError = null
        isAnalysing = true
        viewModelScope.launch {
            classifier.analyse(trimmed)
                .onSuccess {
                    currentResult = it.copy(messageText = trimmed)
                    currentSaved = false
                    onDone()
                }
                .onFailure { scanError = "The analyser is unavailable right now. Please try again." }
            isAnalysing = false
        }
    }

    fun clearScanError() { scanError = null }

    fun saveCurrent() {
        val r = currentResult ?: return
        if (currentSaved) return
        history.add(0, r.copy(messageText = if (saveMessageText) r.messageText else null))
        currentSaved = true
    }

    fun reportCurrent(reason: String) {
        val r = currentResult ?: return
        if (currentReported) return
        reports.add(0, ScamReport(analysisId = r.id, reason = reason))
    }

    fun deleteHistory(id: Long) { history.removeAll { it.id == id } }
    fun deleteReport(id: Long) { reports.removeAll { it.id == id } }

    fun updateNote(id: Long, note: String) {
        val idx = history.indexOfFirst { it.id == id }
        if (idx >= 0) history[idx] = history[idx].copy(note = note)
    }

    fun setMonitoring(enabled: Boolean) { monitoringEnabled = enabled }
    fun updateSaveMessageText(enabled: Boolean) { saveMessageText = enabled }

    fun clearAllData() {
        history.clear()
        reports.clear()
        currentResult = null
        currentSaved = false
    }
}
