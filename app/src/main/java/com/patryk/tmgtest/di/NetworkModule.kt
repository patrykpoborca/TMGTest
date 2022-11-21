package com.patryk.tmgtest.di

import com.google.gson.Gson
import com.patryk.tmgtest.network.TMGApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// you could make change impl based on flavor etc incl base url and level of logging/intercepting
@Module
class NetworkModule() {

    @Singleton
    @Provides
    fun provideOkhttp(logger: HttpLoggingInterceptor) = OkHttpClient.Builder()
        .addInterceptor(logger)
        .build()

    @Singleton
    @Provides
    fun provideLoggingInterceptor() = HttpLoggingInterceptor()

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient, callAdapter: RxJava3CallAdapterFactory, converter: GsonConverterFactory): Retrofit = Retrofit.Builder()
        .baseUrl("https://gist.githubusercontent.com")
        .addCallAdapterFactory(callAdapter)
        .addConverterFactory(converter)
        .client(client)
        .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): TMGApi = retrofit.create(TMGApi::class.java)

}