package com.patryk.tmgtest.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.patryk.tmgtest.R
import com.patryk.tmgtest.di.SchedulerHelper
import com.patryk.tmgtest.network.repositories.FakeDataRepo
import com.patryk.tmgtest.utility.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val fakeDataRepo: FakeDataRepo, helper: SchedulerHelper) :
    BaseViewModel<SettingsScreenState, TMGVMEvents>(SettingsScreenState(), helper) {

    fun onDeleteCache() {
        fakeDataRepo.delete()
        effectStream.onNext(
            ToastEvent(
                R.string.successfully_deleted
            )
        )
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
}

class SettingsScreenState : TMGScreenState()