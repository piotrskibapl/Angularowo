package pl.piotrskiba.angularowo.domain.analytics.repository

import io.reactivex.rxjava3.core.Completable

interface AnalyticsRepository {

    fun logLogin(playerUuid: String, playerName: String): Completable
    fun logLoginError(message: String?): Completable
}
