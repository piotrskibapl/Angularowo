package pl.piotrskiba.angularowo.data.offers.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.offers.OffersApiService
import pl.piotrskiba.angularowo.data.offers.model.toDomain
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository

class OffersRepositoryImpl(
    private val offersApi: OffersApiService,
) : OffersRepository {

    override fun getOffersInfo(accessToken: String): Single<OffersInfoModel> =
        offersApi
            .getOffersInfo(accessToken)
            .map { it.toDomain() }

    override fun redeemAdOffer(accessToken: String, offerId: String): Completable =
        offersApi.redeemAdOffer(offerId, accessToken)

    override fun redeemOffer(accessToken: String, offerId: String): Completable =
        offersApi.redeemOffer(offerId, accessToken)
}
