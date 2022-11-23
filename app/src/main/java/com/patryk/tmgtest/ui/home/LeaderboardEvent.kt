package com.patryk.tmgtest.ui.home

import androidx.annotation.StringRes
import com.patryk.tmgtest.R
import com.patryk.tmgtest.utility.TMGVMEvents

open class LeaderboardEvent: TMGVMEvents()

object ScreenLoad: LeaderboardEvent()

data class OnFilterChange(val filterType: FilterType): LeaderboardEvent()

enum class FilterType(@StringRes val title: Int) {
    PLAYED(R.string.games_played),
    WON(R.string.games_won),
    RATIO(R.string.win_ratio);
}
