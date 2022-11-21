package com.patryk.tmgtest.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.models.TMGIndividualScore
import com.patryk.tmgtest.network.TMGApi
import com.patryk.tmgtest.utility.fromJson
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

// Either grabs existing cached data or fetches stubbed data from API
class FakeDataRepo @Inject constructor(private val api: TMGApi, private val ctx: Context, private val gson: Gson) {

    private val inMemoryList: MutableList<TMGGameScore> = mutableListOf()

    fun getUserScores(): Observable<List<TMGGameScore>> {
        // super basic persistence
        if (inMemoryList.isNotEmpty())
            return Observable.just(inMemoryList)

        val cachedResult = ctx.getSharedPreferences(FakeDataRepo::class.simpleName, Context.MODE_PRIVATE).getString(TMG_DATA, null)
            ?.let {
                gson.fromJson<List<TMGGameScore>>(it)
            }
        if (cachedResult != null) {
            inMemoryList.clear()
            inMemoryList.addAll(cachedResult)
            return Observable.just(cachedResult)

        }
        return api.getInitialData()
            .map {
                it.map { responseItem ->
                    var winner = TMGIndividualScore(responseItem.p1, responseItem.score1)
                    var loser = TMGIndividualScore(responseItem.p2, responseItem.score2)
                    var temp: TMGIndividualScore;
                    if (winner.score < loser.score) {
                        temp = loser
                        loser = winner
                        winner = temp
                    }
                    TMGGameScore(
                        winningScore = winner,
                        losingScore = loser
                    )
                }
            }
            .doOnNext {
                inMemoryList.clear()
                inMemoryList.addAll(it)
                saveCache()
            }
    }

    fun saveCache() {
        ctx.getSharedPreferences(FakeDataRepo::class.simpleName, Context.MODE_PRIVATE)
            .edit().apply {
                putString(TMG_DATA, gson.toJson(inMemoryList))
                apply()
            }
    }

    companion object {
        const val TMG_DATA = "TMG_DATA"
    }
}