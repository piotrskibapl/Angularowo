package pl.piotrskiba.angularowo.domain.offers.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

interface OffersRepository {

    fun getOffersInfo(): Single<OffersInfoModel>

    fun redeemAdOffer(
        offerId: String,
    ): Completable

    fun redeemPrizeOffer(
        offerId: String,
    ): Completable
}
