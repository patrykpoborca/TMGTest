package com.patryk.tmgtest.utility

import android.view.ViewPropertyAnimator
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.models.TMGIndividualScore
import io.reactivex.rxjava3.core.Observable

inline fun <reified T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)

fun ViewPropertyAnimator.translateXByOrReset(distance: Float, returnToOriginal: Boolean): ViewPropertyAnimator {
    if (returnToOriginal)
        return translationX(0f)
    else
        return translationXBy(distance)
}

fun deriveTMGScore(p1: String?, score1: Int?, p2: String?, score2: Int?, index: Int): TMGGameScore {
    var winner = TMGIndividualScore(p1 ?: "", score1 ?: 0)
    var loser = TMGIndividualScore(p2 ?: "", score2 ?: 0)
    var temp: TMGIndividualScore;
    if (winner.score < loser.score) {
        temp = loser
        loser = winner
        winner = temp
    }
    return TMGGameScore(
        winningScore = winner,
        losingScore = loser,
        index
    )
}

fun <T: TMGScreenState>T.toObs(): Observable<T> = Observable.just(this)