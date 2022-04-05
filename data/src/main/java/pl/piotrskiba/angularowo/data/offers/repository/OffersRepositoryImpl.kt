package pl.piotrskiba.angularowo.data.offers.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.offers.OffersApiService
import pl.piotrskiba.angularowo.data.offers.model.toDomain
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class OffersRepositoryImpl @Inject constructor(
    private val offersApi: OffersApiService
) : OffersRepository {

    override fun getOffersInfo(accessToken: String): Single<OffersInfoModel> =
        offersApi
            .getOffersInfo(BuildConfig.API_KEY, accessToken)
            .map { it.toDomain() }

    override fun redeemAdOffer(accessToken: String, offerId: String): Completable =
        offersApi
            .redeemAdOffer(BuildConfig.API_KEY, offerId, accessToken)

    override fun redeemOffer(accessToken: String, offerId: String): Completable =
        offersApi
            .redeemOffer(BuildConfig.API_KEY, offerId, accessToken)
}
