package com.drafthelper.neutral.model

enum class LaneRole(val label: String) {
    AUTO("AUTO"),
    EXP("EXP"),
    JUNGLE("JUNGLE"),
    GOLD("GOLD"),
    ROAM("ROAM"),
    MID("MID")
}

enum class ThreatTag(val label: String) {
    BURST("Burst"),
    MAGIC("Magic Damage"),
    BASIC_ATK("Basic Attack / Atk Speed"),
    CROWD_CONTROL("Crowd Control"),
    SUSTAIN("Sustain / Heal"),
    INVADE("Jungle Invade"),
    FAST_FARM("Fast Farming"),
    FAST_SPIKE("Fast Power Spike")
}

data class NeutralHero(
    val name: String,
    val primaryLane: LaneRole,
    val threats: Set<ThreatTag>,
    val earlyScore: Int,
    val midScore: Int,
    val lateScore: Int
)

data class BanRecommendation(
    val heroName: String,
    val reason: String
)

data class DraftInsight(
    val allyWinPercent: Int,
    val enemyWinPercent: Int,
    val banSuggestions: List<BanRecommendation>,
    val counterNotes: List<String>,
    val allySpikePhase: String,
    val enemySpikePhase: String,
    val allyStrengths: List<String>,
    val allyWeaknesses: List<String>,
    val gamePlan: String,
    val buildItems: List<String>,
    val emblemSuggestion: String,
    val spellSuggestion: String,
    val rotationSuggestion: String
)
