package pl.piotrskiba.angularowo.domain.applock.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel

interface AppLockRepository {

    fun getAppLockConfig(): Single<AppLockConfigModel>
}
