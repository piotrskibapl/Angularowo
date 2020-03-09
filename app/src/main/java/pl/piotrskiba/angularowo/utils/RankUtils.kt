package pl.piotrskiba.angularowo.utils

import android.content.Context
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.models.Rank

object RankUtils {
    @JvmStatic
    val allRanks: List<Rank>
        get() {
            val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val json = firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_RANKS_KEY)
            val gson = GsonBuilder().create()
            return gson.fromJson(json, object : TypeToken<List<Rank?>?>() {}.type)
        }

    @JvmStatic
    fun getRankFromPreferences(context: Context): Rank? {
        val rankName = PreferenceUtils.getRankName(context)

        rankName?.run {
            return getRankFromName(rankName)
        } ?: kotlin.run {
            return null
        }
    }

    @JvmStatic
    fun getRankFromName(name: String): Rank {
        val ranks = allRanks
        for (rank in ranks) {
            if (rank.name == name)
                return rank
        }
        return Rank(name, false, "7", "7")
    }
}