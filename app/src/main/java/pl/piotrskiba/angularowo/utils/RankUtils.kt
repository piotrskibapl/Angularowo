package pl.piotrskiba.angularowo.utils

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.models.Rank

object RankUtils {
    /**
     * a list of all ranks available in the app
     */
    @JvmStatic
    val allRanks: List<Rank>
        get() {
            val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
            val json = firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_RANKS_KEY)
            val gson = GsonBuilder().create()
            return gson.fromJson(json, object : TypeToken<List<Rank?>?>() {}.type)
        }

    /**
     * Function for getting the rank corresponding to the specified rank name
     *
     * @param name the name of the rank to obtain
     * @return the corresponding rank from rank list or new rank if not found
     */
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