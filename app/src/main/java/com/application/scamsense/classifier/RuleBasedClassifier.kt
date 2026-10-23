package com.application.scamsense.classifier

import com.application.scamsense.model.AnalysisResult
import com.application.scamsense.model.RiskLevel
import kotlinx.coroutines.delay
import kotlin.math.min


class RuleBasedClassifier(
    private val detector: IndicatorDetector = IndicatorDetector()
) : FraudClassifier {

    override suspend fun analyse(text: String): Result<AnalysisResult> {
        delay(900) // demo only: lets you see the loading animation. Remove once real.
        val found = detector.detect(text)
        val score = min(100, found.sumOf { it.weight })
        val level = when {
            score >= 60 -> RiskLevel.HIGH
            score >= 25 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }
        val classification =
            if (level == RiskLevel.LOW) "No strong warning signs found"
            else "Potentially fraudulent"

        val explanation = if (found.isEmpty()) {
            "None of the suspicious patterns we check for were found. This does not prove the message is genuine."
        } else {
            "${found.size} suspicious pattern(s) were found. The risk indicator adds up a weight for each one (max 100). " +
                    "It is supporting evidence, not proof."
        } + " (Rule-based check only: the ML model is not connected yet.)"

        val steps = when (level) {
            RiskLevel.HIGH -> listOf(
                "Do not pay any fee or share personal or bank details.",
                "Do not open links in the message.",
                "Verify the company on its official website or LinkedIn page.",
                "Report it, and talk to your placement cell."
            )
            RiskLevel.MEDIUM -> listOf(
                "Verify independently before replying.",
                "Contact the company through its official website, not the message.",
                "Never pay to secure an internship."
            )
            RiskLevel.LOW -> listOf(
                "Still verify the recruiter and company independently.",
                "Be careful if they later ask for money or documents."
            )
        }

        return Result.success(
            AnalysisResult(
                classification = classification,
                riskLevel = level,
                riskScore = score,
                indicators = found.map { it.indicator },
                explanation = explanation,
                nextSteps = steps
            )
        )
    }
}

