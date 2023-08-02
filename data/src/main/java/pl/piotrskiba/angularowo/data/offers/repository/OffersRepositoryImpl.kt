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

    override fun getOffersInfo(): Single<OffersInfoModel> =
        offersApi
            .getOffersInfo()
            .map { it.toDomain() }

    override fun redeemAdOffer(offerId: String): Completable =
        offersApi.redeemAdOffer(offerId)

    override fun redeemOffer(offerId: String): Completable =
        offersApi.redeemOffer(offerId)
}
