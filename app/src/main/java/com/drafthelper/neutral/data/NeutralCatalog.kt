package com.drafthelper.neutral.data

import com.drafthelper.neutral.model.LaneRole
import com.drafthelper.neutral.model.NeutralHero
import com.drafthelper.neutral.model.ThreatTag

object NeutralCatalog {
    val heroes: List<NeutralHero> = listOf(
        NeutralHero("Hirara", LaneRole.EXP, setOf(ThreatTag.SUSTAIN, ThreatTag.FAST_SPIKE), 7, 8, 7),
        NeutralHero("Marcel", LaneRole.ROAM, setOf(ThreatTag.CROWD_CONTROL, ThreatTag.SUSTAIN), 7, 8, 7),
        NeutralHero("Sora", LaneRole.JUNGLE, setOf(ThreatTag.BURST, ThreatTag.INVADE), 8, 8, 6),
        NeutralHero("Obsidia", LaneRole.GOLD, setOf(ThreatTag.BASIC_ATK, ThreatTag.FAST_FARM), 4, 7, 9),
        NeutralHero("Zetian", LaneRole.MID, setOf(ThreatTag.MAGIC, ThreatTag.CROWD_CONTROL), 6, 8, 8),
        NeutralHero("Kalea", LaneRole.ROAM, setOf(ThreatTag.CROWD_CONTROL, ThreatTag.FAST_SPIKE), 8, 7, 6),
        NeutralHero("Lukas", LaneRole.EXP, setOf(ThreatTag.SUSTAIN, ThreatTag.BURST), 7, 8, 7),
        NeutralHero("Suyou", LaneRole.JUNGLE, setOf(ThreatTag.BURST, ThreatTag.FAST_SPIKE), 8, 8, 6),
        NeutralHero("Zhuxin", LaneRole.MID, setOf(ThreatTag.MAGIC, ThreatTag.CROWD_CONTROL), 6, 8, 8),
        NeutralHero("Chip", LaneRole.ROAM, setOf(ThreatTag.CROWD_CONTROL, ThreatTag.FAST_SPIKE), 7, 8, 7),
        NeutralHero("Cici", LaneRole.EXP, setOf(ThreatTag.SUSTAIN, ThreatTag.FAST_SPIKE), 8, 7, 6),
        NeutralHero("Nolan", LaneRole.JUNGLE, setOf(ThreatTag.BURST, ThreatTag.INVADE), 8, 8, 6),
        NeutralHero("Ixia", LaneRole.GOLD, setOf(ThreatTag.BASIC_ATK, ThreatTag.CROWD_CONTROL), 5, 7, 9),
        NeutralHero("Arlott", LaneRole.EXP, setOf(ThreatTag.BURST, ThreatTag.CROWD_CONTROL), 7, 8, 7),
        NeutralHero("Novaria", LaneRole.MID, setOf(ThreatTag.MAGIC, ThreatTag.FAST_FARM), 6, 7, 8),
        NeutralHero("Joy", LaneRole.JUNGLE, setOf(ThreatTag.MAGIC, ThreatTag.FAST_SPIKE), 8, 8, 6),
        NeutralHero("Fredrinn", LaneRole.JUNGLE, setOf(ThreatTag.SUSTAIN, ThreatTag.CROWD_CONTROL), 6, 8, 8),
        NeutralHero("Julian", LaneRole.JUNGLE, setOf(ThreatTag.MAGIC, ThreatTag.FAST_SPIKE), 8, 8, 6),
        NeutralHero("Xavier", LaneRole.MID, setOf(ThreatTag.MAGIC, ThreatTag.FAST_FARM), 4, 7, 9),
        NeutralHero("Melissa", LaneRole.GOLD, setOf(ThreatTag.BASIC_ATK, ThreatTag.FAST_FARM), 5, 8, 9),
        NeutralHero("Yin", LaneRole.JUNGLE, setOf(ThreatTag.BURST, ThreatTag.FAST_SPIKE), 7, 8, 6)
    )
}
