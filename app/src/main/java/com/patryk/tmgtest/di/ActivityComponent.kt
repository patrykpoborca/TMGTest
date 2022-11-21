package com.patryk.tmgtest.di

import android.app.Activity
import com.patryk.tmgtest.HostActivity
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Scope

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(hostActivity: HostActivity)
}

@Module
class ActivityModule(private val activity: Activity) {

    @Provides
    fun provideActivity(): Activity = activity
}

@Scope
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityScope

