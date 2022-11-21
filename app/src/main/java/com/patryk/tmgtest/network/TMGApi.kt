package com.patryk.tmgtest.network

import com.patryk.tmgtest.models.TMGGameScore
import com.patryk.tmgtest.network.models.InitialDataResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface TMGApi {

    // This is not a real api call just a hosted json that can be deserialized. Figured it would help fill out dagger to have this
    @GET("/patrykpoborca/5f8abb88040d70f57528c9aa0364fecb/raw/9ee6968b94df0594d1a79335c1c0f3f1ffceeaee/tmgdata.json")
    fun getInitialData(): Observable<List<InitialDataResponse>>
}