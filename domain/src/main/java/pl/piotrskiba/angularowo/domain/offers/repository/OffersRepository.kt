package pl.piotrskiba.angularowo.domain.offers.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

interface OffersRepository {

    fun getOffersInfo(
        accessToken: String,
    ): Single<OffersInfoModel>

    fun redeemAdOffer(
        accessToken: String,
        offerId: String,
    ): Completable

    fun redeemOffer(
        accessToken: String,
        offerId: String,
    ): Completable
}
