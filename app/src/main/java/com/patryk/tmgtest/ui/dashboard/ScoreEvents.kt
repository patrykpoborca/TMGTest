package com.patryk.tmgtest.ui.dashboard

import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.utility.TMGVMEvents

open class ScoreEvents : TMGVMEvents()

object OnScreenLoad : ScoreEvents()
data class OnUpdateScoreItem(val score: TMGGameScore) : ScoreEvents()
data class FabClicked(val score: TMGGameScore) : ScoreEvents()
object OnClickAbsorbed : ScoreEvents()