package com.application.scamsense.classifier


import com.application.scamsense.model.AnalysisResult

/**
 * The UI only knows about this interface. Today it is backed by rule-based
 * indicators; later you swap in the TF-IDF + Logistic Regression model
 * (on-device or FastAPI) without touching any screen.
 * Result.failure = classifier unavailable.
 */
interface FraudClassifier {
    suspend fun analyse(text: String): Result<AnalysisResult>
}

