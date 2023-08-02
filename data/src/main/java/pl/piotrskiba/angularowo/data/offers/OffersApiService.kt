package pl.piotrskiba.angularowo.data.offers

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.offers.model.OffersInfoRemote
import retrofit2.http.GET
import retrofit2.http.Query

interface OffersApiService {

    @GET("get_offers_info.php")
    fun getOffersInfo(): Single<OffersInfoRemote>

    @GET("redeem_ad_offer.php")
    fun redeemAdOffer(@Query("offer_id") offerId: String): Completable

    @GET("redeem_offer.php")
    fun redeemOffer(@Query("offer_id") offerId: String): Completable
}
