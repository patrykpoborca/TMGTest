package com.patryk.tmgtest.di

import com.patryk.tmgtest.ui.dashboard.ScoresFragment
import com.patryk.tmgtest.ui.home.HomeFragment
import com.patryk.tmgtest.ui.notifications.SettingsFragment
import dagger.Component

@Component(dependencies = [ActivityComponent::class])
interface FragmentComponent {

    // You could break these into their own components and their own injection sites but for such a small project it's not needed.
    fun inject(scoresFragment: ScoresFragment)
    fun inject(homeFragment: HomeFragment)
    fun inject(settingsFragment: SettingsFragment)
}