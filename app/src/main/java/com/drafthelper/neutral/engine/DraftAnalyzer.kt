package com.drafthelper.neutral.engine

import com.drafthelper.neutral.model.*

object DraftAnalyzer {

    fun analyze(
        selectedLane: LaneRole,
        allies: List<NeutralHero?>,
        enemies: List<NeutralHero?>,
        catalog: List<NeutralHero>
    ): DraftInsight {
        val safeAllies = allies.take(5).filterNotNull()
        val safeEnemies = enemies.take(5).filterNotNull()
        val safeCatalog = catalog.filter { it.name.isNotBlank() }

        val allyEarly = safeAllies.sumOf { it.earlyScore.coerceIn(0, 10) }
        val allyMid = safeAllies.sumOf { it.midScore.coerceIn(0, 10) }
        val allyLate = safeAllies.sumOf { it.lateScore.coerceIn(0, 10) }

        val enemyEarly = safeEnemies.sumOf { it.earlyScore.coerceIn(0, 10) }
        val enemyMid = safeEnemies.sumOf { it.midScore.coerceIn(0, 10) }
        val enemyLate = safeEnemies.sumOf { it.lateScore.coerceIn(0, 10) }

        val rawAllyPower = (allyEarly + allyMid + allyLate + 15).coerceAtLeast(10)
        val rawEnemyPower = (enemyEarly + enemyMid + enemyLate + 15).coerceAtLeast(10)
        val total = (rawAllyPower + rawEnemyPower).coerceAtLeast(1)
        val allyPercent = ((rawAllyPower * 100.0) / total).toInt().coerceIn(20, 80)
        val enemyPercent = (100 - allyPercent).coerceIn(20, 80)

        val enemyThreatCounts: Map<ThreatTag, Int> = ThreatTag.entries.associateWith { tag ->
            safeEnemies.count { tag in it.threats }
        }

        val banSuggestions = safeCatalog
            .filter { hero -> hero !in safeAllies && hero !in safeEnemies }
            .sortedByDescending { it.earlyScore + it.midScore }
            .take(3)
            .map { hero ->
                val mainTag = hero.threats.firstOrNull()?.label ?: "Tempo tinggi"
                BanRecommendation(
                    heroName = hero.name,
                    reason = "$mainTag • tekanan fasa awal/tengah"
                )
            }

        val counterNotes = buildList {
            if ((enemyThreatCounts[ThreatTag.CROWD_CONTROL] ?: 0) >= 2) {
                add("CC berlapis — masuk selepas skill lock/stun utama keluar.")
            }
            if ((enemyThreatCounts[ThreatTag.INVADE] ?: 0) >= 1) {
                add("Potensi Jungle Invade awal — jaga buff & vision awal.")
            }
            if ((enemyThreatCounts[ThreatTag.FAST_SPIKE] ?: 0) >= 2) {
                add("Fast power spike — elak trade lama di early jika belum siap.")
            }
            if ((enemyThreatCounts[ThreatTag.FAST_FARM] ?: 0) >= 2) {
                add("Fast farming — tekan objektif sebelum core item lawan siap.")
            }
            if ((enemyThreatCounts[ThreatTag.BASIC_ATK] ?: 0) >= 2) {
                add("Basic attack kuat — utamakan pertahanan anti-basic/sustain.")
            }
            if (isEmpty()) {
                add("Pilih hero ally/enemy untuk semak counter & ancaman spesifik.")
            }
        }

        val allySpikePhase = when {
            safeAllies.isEmpty() -> "Belum ada pick ally"
            allyEarly >= allyMid && allyEarly >= allyLate -> "Strongest at Early Game"
            allyLate >= allyMid && allyLate >= allyEarly -> "Strongest at Late Game"
            else -> "Strongest at Mid Game"
        }

        val enemySpikePhase = when {
            safeEnemies.isEmpty() -> "Belum ada pick enemy"
            enemyEarly >= enemyMid && enemyEarly >= enemyLate -> "Strongest at Early Game"
            enemyLate >= enemyMid && enemyLate >= enemyEarly -> "Strongest at Late Game"
            else -> "Strongest at Mid Game"
        }

        val allyStrengths = buildList {
            val coveredLanes = safeAllies.map { it.primaryLane }.distinct().size
            if (coveredLanes >= 3) add("Lane coverage tersusun ($coveredLanes lane berbeza)")
            if (allyEarly > enemyEarly) add("Tekanan awal lebih kemas")
            if (allyLate > enemyLate) add("Potensi scaling late game kuat")
            if (isEmpty()) add("Keseimbangan draft masih boleh dibentuk")
        }

        val allyWeaknesses = buildList {
            if (safeAllies.none { it.primaryLane == LaneRole.ROAM }) add("Belum ada Roam yang jelas")
            if (safeAllies.none { it.primaryLane == LaneRole.JUNGLE }) add("Belum ada Jungle yang jelas")
            if (enemyEarly > allyEarly + 3) add("Early game mudah ditekan musuh")
            if (isEmpty()) add("Jaga posisi & komunikasi objektif")
        }

        val gamePlan = when {
            allyEarly > enemyEarly + 3 -> "Buka tempo awal & objektif sebelum musuh capai fasa kuat mereka."
            allyLate > enemyLate + 3 -> "Main selamat di awal, elak war tak seimbang, dan capai scaling."
            else -> "Kawal wave & objektif ikut peluang lane yang paling terbuka."
        }

        val buildItems = buildList {
            add("Kasut Situasional")
            if ((enemyThreatCounts[ThreatTag.BASIC_ATK] ?: 0) >= 1) add("Chastise Pauldron / Anti Basic Atk")
            if ((enemyThreatCounts[ThreatTag.MAGIC] ?: 0) >= 1) add("Magic Defense / Shield")
            if ((enemyThreatCounts[ThreatTag.SUSTAIN] ?: 0) >= 1) add("Anti-Heal / Penetration")
            if ((enemyThreatCounts[ThreatTag.BURST] ?: 0) >= 1) add("Sustain / Physical Defense")
            add("Item Teras Mengikut Role")
        }.distinct().take(6)

        val emblemSuggestion = when {
            (enemyThreatCounts[ThreatTag.CROWD_CONTROL] ?: 0) >= 2 -> "Fighter / Tank Emblem — fokus Tenacity & Durabiliti"
            (enemyThreatCounts[ThreatTag.BASIC_ATK] ?: 0) >= 2 -> "Emblem Fokus Sustain / Hybrid Defense"
            else -> "Emblem Teras Mengikut Lane (${selectedLane.label})"
        }

        val spellSuggestion = when {
            selectedLane == LaneRole.JUNGLE -> "Retribution"
            (enemyThreatCounts[ThreatTag.CROWD_CONTROL] ?: 0) >= 2 -> "Purify"
            (enemyThreatCounts[ThreatTag.BASIC_ATK] ?: 0) >= 2 || (enemyThreatCounts[ThreatTag.BURST] ?: 0) >= 2 -> "Vengeance / Flicker"
            else -> "Flicker / Vengeance"
        }

        val rotationSuggestion = when (selectedLane) {
            LaneRole.JUNGLE -> if ((enemyThreatCounts[ThreatTag.INVADE] ?: 0) >= 1)
                "Jaga buff awal, baca pergerakan mid/roam, gank lane terbuka."
            else
                "Clear pantas, gank lane wave terbuka atau objektif terdekat."
            LaneRole.GOLD -> "Farm & posisi selamat di early; rotate bila wave clear/objektif terbuka."
            LaneRole.EXP -> "Kawal wave & elak gank awal; bantu mid/turtle bila lane stabil."
            LaneRole.MID -> "Clear wave tengah pantas & ikut rotasi ke Gold/EXP."
            LaneRole.ROAM -> "Buka vision awal, lindungi lane berisiko & pimpin rotasi gank."
            LaneRole.AUTO -> "Ikut lane paling terbuka untuk gank/objektif & kawal vision."
        }

        return DraftInsight(
            allyWinPercent = allyPercent,
            enemyWinPercent = enemyPercent,
            banSuggestions = banSuggestions,
            counterNotes = counterNotes,
            allySpikePhase = allySpikePhase,
            enemySpikePhase = enemySpikePhase,
            allyStrengths = allyStrengths,
            allyWeaknesses = allyWeaknesses,
            gamePlan = gamePlan,
            buildItems = buildItems,
            emblemSuggestion = emblemSuggestion,
            spellSuggestion = spellSuggestion,
            rotationSuggestion = rotationSuggestion
        )
    }
}
