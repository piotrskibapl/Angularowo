package pl.piotrskiba.angularowo.domain.cloudmessaging.repository

import io.reactivex.rxjava3.core.Completable

interface CloudMessagingRepository {

    fun subscribeToAppVersion(versionCode: Int): Completable

    fun unsubscribeFromAppVersion(versionCode: Int): Completable

    fun subscribeToPlayerUuid(uuid: String): Completable

    fun unsubscribeFromPlayerUuid(uuid: String): Completable

    fun subscribeToPlayerRank(rankName: String): Completable

    fun unsubscribeFromPlayerRank(rankName: String): Completable

    fun subscribeToNewEvents(): Completable

    fun unsubscribeFromNewEvents(): Completable

    fun subscribeToPrivateMessages(): Completable

    fun unsubscribeFromPrivateMessages(): Completable

    fun subscribeToAccountIncidents(): Completable

    fun unsubscribeFromAccountIncidents(): Completable

    fun subscribeToNewReports(): Completable

    fun unsubscribeFromNewReports(): Completable
}
