package com.patryk.tmgtest.network.repositories

import android.content.Context
import com.google.gson.Gson
import com.patryk.tmgtest.di.SchedulerHelper
import com.patryk.tmgtest.di.handleThreading
import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.network.TMGApi
import com.patryk.tmgtest.utility.deriveTMGScore
import com.patryk.tmgtest.utility.fromJson
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject
import javax.inject.Singleton

// Either grabs existing cached data or fetches stubbed data from API
@Singleton
class FakeDataRepo @Inject constructor(
    private val api: TMGApi,
    @ApplicationContext private val ctx: Context,
    private val gson: Gson,
    private val schedulerHelper: SchedulerHelper
) {

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
                it.mapIndexed { index, responseItem ->
                    deriveTMGScore(
                        responseItem.p1,
                        responseItem.score1,
                        responseItem.p2,
                        responseItem.score2,
                        index
                    )
                }
            }
            .doOnNext {
                inMemoryList.clear()
                inMemoryList.addAll(it)
                saveCache()
            }
            .handleThreading(schedulerHelper)
    }

    fun saveCache(overrideScore: TMGGameScore? = null, isNew: Boolean = false) {
        if (overrideScore != null) {
            if (isNew) {
                inMemoryList.add(overrideScore.copy(
                    index = inMemoryList.size
                ))
            } else {
                inMemoryList[overrideScore.index] = overrideScore
            }
        }
        ctx.getSharedPreferences(FakeDataRepo::class.simpleName, Context.MODE_PRIVATE)
            .edit().apply {
                putString(TMG_DATA, gson.toJson(inMemoryList))
                apply()
            }
    }

    fun delete() {
        inMemoryList.clear()
        ctx.getSharedPreferences(FakeDataRepo::class.simpleName, Context.MODE_PRIVATE)
            .edit()
            .remove(TMG_DATA)
            .apply()
    }

    companion object {
        const val TMG_DATA = "TMG_DATA"
    }
}