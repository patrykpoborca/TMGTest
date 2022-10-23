package com.patryk.tmgtest.utility

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import autodispose2.AutoDispose
import autodispose2.androidx.lifecycle.AndroidLifecycleScopeProvider
import com.patryk.tmgtest.R
import com.patryk.tmgtest.di.SchedulerHelper
import com.patryk.tmgtest.ui.dashboard.ScoreScreenState
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject

abstract class BaseViewModel<T: TMGScreenState, B: TMGVMEvents>(defaultState: T, val schedulerHelper: SchedulerHelper): ViewModel() {
    internal val effectStream: PublishSubject<TMGVMEffects> = PublishSubject.create()
    internal val eventStream: PublishSubject<B> = PublishSubject.create()
    internal var state: T = defaultState

    fun observeScreenState(): Observable<T> {
        return eventStream
            .flatMap {
                mapToState(it)
            }
            .doOnNext {
                state = it
            }
            .observeOn(schedulerHelper.uiScheduler)
    }

    open fun mapToState(it: B): Observable<T> {
        return state.toObs()
    }

    fun listenToVMEffects(ctx: Context, lifecycleOwner: LifecycleOwner, hookin: ((TMGVMEffects) -> Unit)? = null) {
        effectStream
            .to(AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(lifecycleOwner)))
            .subscribe(
                {
                    if (it is ToastEvent)
                        Toast.makeText(ctx, it.message, it.length).show()
                    else if (it is DialogEvent) {
                        AlertDialog.Builder(ctx)
                            .setTitle(it.title)
                            .setMessage(it.message)
                            .setPositiveButton(it.positiveButton.str
                            ) { _, _ -> it.positiveButton.onClick }
                            .let { ab ->
                                if(it.negativeButton != null) {
                                    return@let ab.setNegativeButton(it.negativeButton.str
                                    ) { _, _ -> it.negativeButton.onClick }
                                }
                                return@let ab
                            }
                            .show()

                    } else hookin?.invoke(it)

                },
                {}
            )
    }
}
open class TMGScreenState
// Events for IO/Ui events
open class TMGVMEvents


// called events cause they're ephemeral, consumed and displayed, not necessarily a state change
open class TMGVMEffects
data class ToastEvent(@StringRes val message: Int, val length: Int = Toast.LENGTH_LONG) : TMGVMEffects()
data class DialogEvent(
    @StringRes val title: Int,
    @StringRes val message: Int,
    val positiveButton: TMGDialogButton = TMGDialogButton(),
    val negativeButton: TMGDialogButton? = null
) : TMGVMEffects()

data class TMGDialogButton(@StringRes val str: Int = R.string.okay, val onClick: (() -> Unit) = {})