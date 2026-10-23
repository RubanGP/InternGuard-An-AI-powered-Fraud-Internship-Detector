package com.application.scamsense.model


enum class RiskLevel(val label: String) {
    LOW("Low Risk"),
    MEDIUM("Medium Risk"),
    HIGH("High Risk")
}

data class Indicator(val title: String, val explanation: String)


data class AnalysisResult(
    val id: Long = System.currentTimeMillis(),
    val classification: String,
    val riskLevel: RiskLevel,
    val riskScore: Int,                 // 0..100 risk INDICATOR, not a probability
    val indicators: List<Indicator>,
    val explanation: String,
    val nextSteps: List<String>,
    val timestamp: Long = System.currentTimeMillis(),
    val messageText: String? = null,
    val note: String = ""
)

/** Stored separately from the prediction. A report is NOT a confirmed scam. */
data class ScamReport(
    val id: Long = System.currentTimeMillis(),
    val analysisId: Long,
    val reason: String,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Unreviewed"
)
