package com.application.scamsense.classifier

import com.application.scamsense.model.Indicator

data class DetectedIndicator(val indicator: Indicator, val weight: Int)

private class Rule(
    val title: String,
    val explanation: String,
    val weight: Int,
    val pattern: Regex
)

/** Feature D. Modular on purpose: edit/add rules here without touching the ML model.
 *  These patterns are a STARTING POINT, not a validated rule set. */
class IndicatorDetector {

    private val i = RegexOption.IGNORE_CASE

    private val rules = listOf(
        Rule("Fee or payment request",
            "Genuine internships rarely ask students to pay registration, training or security fees.",
            35, Regex("""(registration|processing|security|training|kit|refundable)\s+(fee|deposit|charges?)|pay\s+(rs\.?|₹|inr)\s*\d+""", i)),
        Rule("Urgent deadline",
            "Pressure to act within hours is a common way to stop you from checking.",
            15, Regex("""\burgent(ly)?\b|\bimmediately\b|within\s+\d+\s*(hours|hrs|minutes)|last\s+date\s+today|limited\s+(seats|slots)|\bhurry\b""", i)),
        Rule("Unrealistic earnings",
            "Very high pay for little or no work or experience is a red flag.",
            20, Regex("""(₹|rs\.?|inr)\s?\d{1,3}(,?\d{3})+\s*(per|/|a)\s*(day|week)|no\s+experience\s+(needed|required)|work\s+from\s+home.{0,40}(earn|income)""", i)),
        Rule("Guaranteed selection",
            "Real recruitment has a process. 'Guaranteed' selection or 'no interview' is suspicious.",
            25, Regex("""(guaranteed|assured)\s+(selection|placement|job|internship|offer)|no\s+interview""", i)),
        Rule("Sensitive information request",
            "Recruiters should not ask for ID numbers, bank details, OTPs or passwords this early.",
            25, Regex("""aadhaar|aadhar|pan\s*card|passport|bank\s+account|\botp\b|\bcvv\b|card\s+number|password""", i)),
        Rule("Shortened link",
            "Shortened URLs hide the real destination. Do not open them without checking.",
            15, Regex("""bit\.ly|tinyurl\.com|cutt\.ly|rb\.gy|\bt\.co\b|is\.gd""", i)),
        Rule("Pressure or misleading claim",
            "Unexpected 'you are selected' messages and secrecy requests are typical manipulation tactics.",
            10, Regex("""congratulations!?\s+you\s+(are|have\s+been)\s+selected|don'?t\s+tell\s+anyone|only\s+for\s+selected""", i))
    )

    fun detect(text: String): List<DetectedIndicator> =
        rules.filter { it.pattern.containsMatchIn(text) }
            .map { DetectedIndicator(Indicator(it.title, it.explanation), it.weight) }
}
