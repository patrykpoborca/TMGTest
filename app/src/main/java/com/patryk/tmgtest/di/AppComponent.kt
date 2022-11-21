package com.patryk.tmgtest.di

import android.content.Context
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, AppModule::class])
interface AppComponent {
    fun appContext(): Context

}

@Module
class AppModule(ctx: Context) {
    private val context: Context
    init {
        context = ctx.applicationContext
    }

    // singleton not required as you don't need to cache a variable that doesn't change
    @Provides
    fun provideAppContext() = context


}
