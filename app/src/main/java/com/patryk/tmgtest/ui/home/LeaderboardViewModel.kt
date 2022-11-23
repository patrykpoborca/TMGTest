package com.patryk.tmgtest.ui.home

import com.patryk.tmgtest.di.SchedulerHelper
import com.patryk.tmgtest.network.repositories.FakeDataRepo
import com.patryk.tmgtest.utility.BaseViewModel
import com.patryk.tmgtest.utility.TMGScreenState
import com.patryk.tmgtest.utility.toObs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import java.util.*
import javax.inject.Inject

@HiltViewModel
class LeaderboardViewModel @Inject constructor(private val repo: FakeDataRepo, schedulerHelper: SchedulerHelper) : BaseViewModel<LeaderboardViewModelState, LeaderboardEvent>(
    LeaderboardViewModelState(),
    schedulerHelper
) {

    override fun mapToState(it: LeaderboardEvent): Observable<LeaderboardViewModelState> {
        return when(it) {
            ScreenLoad -> onScreenLoad()
            is OnFilterChange -> onFilterChanged(it)
            else -> state.toObs()
        }
    }

    private fun onFilterChanged(leaderboardEvent: OnFilterChange): Observable<LeaderboardViewModelState> {
        val updatedList = when(leaderboardEvent.filterType) {
            FilterType.PLAYED -> state.filteredPlayers.sortedByDescending { p -> p.gamesPlayed }
            FilterType.WON -> state.filteredPlayers.sortedByDescending { p -> p.gamesWon }
            FilterType.RATIO -> state.filteredPlayers.sortedByDescending { p -> p.ratio }
        }
        return state.copy(
            filterType = leaderboardEvent.filterType,
            filteredPlayers = updatedList
        ).toObs()
    }

    private fun onScreenLoad(): Observable<LeaderboardViewModelState> {
        return repo
            .getUserScores()
            .map {
                val mapped = mutableMapOf<String, SyntheticPlayer>()
                it.forEach { score ->
                    val ws = score.winningScore
                    var p1 = mapped[ws.name] ?: SyntheticPlayer(ws.name, 0, 0)
                    p1 = p1.copy(gamesPlayed = p1.gamesPlayed + 1, gamesWon = p1.gamesWon + 1)

                    val ls = score.losingScore
                    var p2 = mapped[ls.name] ?: SyntheticPlayer(ls.name, 0, 0)
                    p2 = p2.copy(gamesPlayed = p2.gamesPlayed + 1, gamesWon = p2.gamesWon)
                    mapped[p1.name] = p1
                    mapped[p2.name] = p2
                }
                state.copy(filteredPlayers = mapped.values.toList())
            }
    }
}

data class LeaderboardViewModelState(
    val filterType: FilterType = FilterType.PLAYED,
    val filteredPlayers: List<SyntheticPlayer> = listOf()
): TMGScreenState()

data class SyntheticPlayer(val name: String, val gamesPlayed: Int, val gamesWon: Int) {
    val ratio
        get() = (gamesWon.toFloat() / gamesPlayed.toFloat()) * 100
    val ratioString
        get() = String.format(Locale.getDefault(), "%.1f", ratio) + "%"
}