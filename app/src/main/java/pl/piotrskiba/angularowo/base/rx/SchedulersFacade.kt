package pl.piotrskiba.angularowo.base.rx

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchedulersFacade @Inject constructor() : SchedulersProvider {

    override fun io() = Schedulers.io()

    override fun computation() = Schedulers.computation()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()
}
