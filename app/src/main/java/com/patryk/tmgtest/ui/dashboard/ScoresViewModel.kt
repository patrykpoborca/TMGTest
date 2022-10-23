package com.patryk.tmgtest.ui.dashboard

import com.patryk.tmgtest.R
import com.patryk.tmgtest.di.SchedulerHelper
import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.network.repositories.FakeDataRepo
import com.patryk.tmgtest.utility.BaseViewModel
import com.patryk.tmgtest.utility.TMGScreenState
import com.patryk.tmgtest.utility.ToastEvent
import com.patryk.tmgtest.utility.toObs
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class ScoresViewModel @Inject constructor(val repo: FakeDataRepo, schedulerHelper: SchedulerHelper) : BaseViewModel<ScoreScreenState, ScoreEvents>(
    ScoreScreenState(),
    schedulerHelper
) {

    override fun mapToState(it: ScoreEvents): Observable<ScoreScreenState> {
        return when (it) {
            onClickAbsorbed -> onClickAbsorbed()
            onScreenLoad -> onScreenLoad()
            is FabClicked -> fabClicked(it.score)
            is onUpdateScoreItem -> onUpdateScoreItem(it.score)
            else -> state.toObs()
        }
    }

    private fun onScreenLoad(): Observable<ScoreScreenState> {
        return repo.getUserScores()
            .map { state.copy(tmgGameScores = it) }
    }

    private fun onUpdateScoreItem(item: TMGGameScore): Observable<ScoreScreenState> {
        if (item.isValid()) {
            repo.saveCache(item)
            effectStream.onNext(ToastEvent(R.string.item_edited))
            effectStream.onNext(ScoreUpdateEffect(item))
        } else {
            effectStream.onNext(ScoreUpdateEffect(null))
        }
        return state.toObs()
    }

    private fun fabClicked(score: TMGGameScore): Observable<ScoreScreenState> {

        if (state.isMakingScore) {
            if (score.isValid()) {
                repo.saveCache(
                    score,
                    true
                )
                effectStream
                    .onNext(ToastEvent(R.string.on_score_created))
                return repo.getUserScores()
                    .take(1)
                    .map {
                        state.copy(
                            isMakingScore = false,
                            tmgGameScores = it
                        )
                    }
            } else {
                effectStream
                    .onNext(ToastEvent(R.string.on_score_failed))
                return state
                    .copy(
                        isMakingScore = false
                    ).toObs()
            }
        }
        return state
            .copy(
                isMakingScore = true
            ).toObs()
    }

    private fun onClickAbsorbed(): Observable<ScoreScreenState> {
        return state
            .copy(
                isMakingScore = false
            ).toObs()
    }

    //endregion
}

// Rudimentary impl of using a "state" data class for vm side manipulations
data class ScoreScreenState(
    val isMakingScore: Boolean = false,
    val tmgGameScores: List<TMGGameScore> = listOf()
) : TMGScreenState()