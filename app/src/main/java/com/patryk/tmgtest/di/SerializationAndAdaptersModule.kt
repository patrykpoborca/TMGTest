package com.patryk.tmgtest.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class SerializationAndAdaptersModule {
    @Singleton
    @Provides
    fun provideGson() = Gson()

    @Provides
    @Singleton
    fun providesGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun providesRxJava3CallAdapterFactory(): RxJava3CallAdapterFactory = RxJava3CallAdapterFactory.create()
}