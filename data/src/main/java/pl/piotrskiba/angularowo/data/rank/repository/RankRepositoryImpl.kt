package pl.piotrskiba.angularowo.data.rank.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.rank.model.Rank.CustomRank
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

private const val REMOTE_CONFIG_RANKS_KEY = "ranks"

class RankRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val gson: Gson
) : RankRepository {

    override fun getAllRanks(): Single<List<CustomRank>> {
        val json = firebaseRemoteConfig.getString(REMOTE_CONFIG_RANKS_KEY)
        val itemType = object : TypeToken<List<CustomRank?>?>() {}.type
        val rankList: List<CustomRank> = gson.fromJson(json, itemType)
        return Single.just(rankList)
    }
}
