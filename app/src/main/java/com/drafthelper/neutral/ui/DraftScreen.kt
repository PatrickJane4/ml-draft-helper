package com.drafthelper.neutral.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drafthelper.neutral.data.NeutralCatalog
import com.drafthelper.neutral.engine.DraftAnalyzer
import com.drafthelper.neutral.model.LaneRole
import com.drafthelper.neutral.model.NeutralHero

@Composable
fun DraftScreen() {
    var selectedLane by remember { mutableStateOf(LaneRole.AUTO) }
    val allies = remember { mutableStateListOf<NeutralHero?>(null, null, null, null, null) }
    val enemies = remember { mutableStateListOf<NeutralHero?>(null, null, null, null, null) }
    var activeSlot by remember { mutableStateOf<Pair<Boolean, Int>?>(null) }

    val insight = remember(selectedLane, allies.toList(), enemies.toList()) {
        DraftAnalyzer.analyze(selectedLane, allies.toList(), enemies.toList(), NeutralCatalog.heroes)
    }

    val bgDark = Color(0xFF0D1117)
    val cardDark = Color(0xFF161B22)
    val accentCyan = Color(0xFF22D3EE)
    val accentAmber = Color(0xFFFBBF24)
    val textBright = Color(0xFFF3F4F6)
    val textMuted = Color(0xFF9CA3AF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgDark)
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "03 DRAFT PICK",
                color = accentCyan,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            TextButton(onClick = {
                for (i in 0 until 5) {
                    if (i < allies.size) allies[i] = null
                    if (i < enemies.size) enemies[i] = null
                }
                activeSlot = null
            }) {
                Text("RESET", color = accentAmber, fontWeight = FontWeight.SemiBold)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(vertical = 6.dp)
        ) {
            items(LaneRole.entries) { lane ->
                val selected = lane == selectedLane
                Box(
                    modifier = Modifier
                        .background(if (selected) accentAmber else cardDark, RoundedCornerShape(8.dp))
                        .clickable { selectedLane = lane }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = lane.label,
                        color = if (selected) Color.Black else textBright,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Surface(color = cardDark, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("DRAFT PICK & COUNTER", color = accentCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("ALLY", color = textMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                            for (i in 0 until 5) {
                                SlotChip(
                                    label = allies.getOrNull(i)?.name ?: "A${i + 1}",
                                    isSelected = activeSlot == (true to i),
                                    accent = accentCyan,
                                    modifier = Modifier.weight(1f),
                                    onClick = { activeSlot = true to i }
                                )
                            }
                        }
                        Text("ENEMY", color = textMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                            for (i in 0 until 5) {
                                SlotChip(
                                    label = enemies.getOrNull(i)?.name ?: "E${i + 1}",
                                    isSelected = activeSlot == (false to i),
                                    accent = accentAmber,
                                    modifier = Modifier.weight(1f),
                                    onClick = { activeSlot = false to i }
                                )
                            }
                        }
                        insight.counterNotes.forEach { note ->
                            Text("• $note", color = textBright, fontSize = 12.sp)
                        }
                    }
                }
            }

            item {
                Surface(color = cardDark, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("WIN PREDICTION • DRAFT STRENGTH", color = accentCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("ALLY ${insight.allyWinPercent}%", color = accentCyan, fontWeight = FontWeight.Bold)
                            Text("ENEMY ${insight.enemyWinPercent}%", color = accentAmber, fontWeight = FontWeight.Bold)
                        }
                        LinearProgressIndicator(
                            progress = { (insight.allyWinPercent / 100f).coerceIn(0f, 1f) },
                            modifier = Modifier.fillMaxWidth().height(8.dp),
                            color = accentCyan,
                            trackColor = accentAmber
                        )
                    }
                }
            }

            item {
                Surface(color = cardDark, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("POWER SPIKE & GAME PLAN", color = accentAmber, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Ally: ${insight.allySpikePhase}", color = accentCyan, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        Text("Enemy: ${insight.enemySpikePhase}", color = accentAmber, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                        insight.allyStrengths.forEach { s -> Text("+ $s", color = textBright, fontSize = 12.sp) }
                        insight.allyWeaknesses.forEach { w -> Text("- $w", color = textMuted, fontSize = 12.sp) }
                        Text("Game Plan: ${insight.gamePlan}", color = textBright, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            item {
                Surface(color = cardDark, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("BAN • BUILD • EMBLEM • SPELL • ROTATION", color = accentCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(
                            "Ban: ${insight.banSuggestions.joinToString(", ") { it.heroName }}",
                            color = accentAmber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text("Spell: ${insight.spellSuggestion}", color = textBright, fontSize = 12.sp)
                        Text("Emblem: ${insight.emblemSuggestion}", color = textBright, fontSize = 12.sp)
                        Text("Build: ${insight.buildItems.joinToString(" • ")}", color = textBright, fontSize = 12.sp)
                        Text("Rotation: ${insight.rotationSuggestion}", color = textBright, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    activeSlot?.let { (isAlly, index) ->
        AlertDialog(
            onDismissRequest = { activeSlot = null },
            containerColor = cardDark,
            title = {
                Text(
                    text = "Pilih Hero (${if (isAlly) "Ally ${index + 1}" else "Enemy ${index + 1}"})",
                    color = textBright,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.heightIn(max = 360.dp)) {
                    item {
                        Text(
                            text = "Kosongkan Slot",
                            color = accentAmber,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (isAlly && index in allies.indices) allies[index] = null
                                    if (!isAlly && index in enemies.indices) enemies[index] = null
                                    activeSlot = null
                                }
                                .padding(8.dp)
                        )
                    }
                    items(NeutralCatalog.heroes) { hero ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(bgDark, RoundedCornerShape(8.dp))
                                .clickable {
                                    if (isAlly && index in allies.indices) allies[index] = hero
                                    if (!isAlly && index in enemies.indices) enemies[index] = hero
                                    activeSlot = null
                                }
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(hero.name, color = textBright, fontWeight = FontWeight.SemiBold)
                            Text(hero.primaryLane.label, color = accentCyan, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { activeSlot = null }) {
                    Text("Tutup", color = accentCyan)
                }
            }
        )
    }
}

@Composable
private fun SlotChip(
    label: String,
    isSelected: Boolean,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(
                color = if (isSelected) accent.copy(alpha = 0.25f) else Color(0xFF0D1117),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(vertical = 8.dp, horizontal = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = Color(0xFFF3F4F6),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}
