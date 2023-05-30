package pl.piotrskiba.angularowo.base.extensions

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider

fun <T : Any> Observable<T>.applyDefaultSchedulers(facade: SchedulersProvider) =
    subscribeOn(facade.io())
        .observeOn(facade.ui())

fun <T : Any> Single<T>.applyDefaultSchedulers(facade: SchedulersProvider) =
    subscribeOn(facade.io())
        .observeOn(facade.ui())

fun Completable.applyDefaultSchedulers(facade: SchedulersProvider) =
    subscribeOn(facade.io())
        .observeOn(facade.ui())
