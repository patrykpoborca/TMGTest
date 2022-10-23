package com.patryk.tmgtest.ui.dashboard

import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.utility.TMGVMEvents

open class ScoreEvents : TMGVMEvents()

object onScreenLoad : ScoreEvents()
data class onUpdateScoreItem(val score: TMGGameScore) : ScoreEvents()
data class FabClicked(val score: TMGGameScore) : ScoreEvents()
object onClickAbsorbed : ScoreEvents()