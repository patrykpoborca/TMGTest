package com.patryk.tmgtest.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SchedulerModule {
    
    @Provides
    @Singleton
    fun provideNetworkScheduler() = SchedulerHelper(
        Schedulers.io(),
        AndroidSchedulers.mainThread()
    )
}

data class SchedulerHelper constructor(val networkScheduler: Scheduler, val uiScheduler: Scheduler)

fun <T : Any>Observable<T>.handleThreading(schedulerHelper: SchedulerHelper): Observable<T> {
    return this.subscribeOn(schedulerHelper.networkScheduler)
        .observeOn(schedulerHelper.uiScheduler)
}