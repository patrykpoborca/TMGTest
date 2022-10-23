package com.patryk.tmgtest.ui.dashboard

import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.utility.TMGVMEffects

data class ScoreUpdateEffect(
    val score: TMGGameScore?
): TMGVMEffects()