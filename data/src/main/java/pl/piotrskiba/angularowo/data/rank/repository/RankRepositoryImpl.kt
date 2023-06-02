package pl.piotrskiba.angularowo.data.rank.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.rank.model.RankRemote
import pl.piotrskiba.angularowo.data.rank.model.toDomain
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

private const val REMOTE_CONFIG_RANKS_KEY = "ranks"

class RankRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
    private val gson: Gson,
) : RankRepository {

    override fun getAllRanks(): Single<List<RankModel>> {
        val json = firebaseRemoteConfig.getString(REMOTE_CONFIG_RANKS_KEY)
        val itemType = object : TypeToken<List<RankRemote?>?>() {}.type
        val rankList: List<RankRemote> = gson.fromJson(json, itemType)
        return Single.just(rankList.toDomain())
    }
}
