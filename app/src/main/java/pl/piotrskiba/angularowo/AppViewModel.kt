package pl.piotrskiba.angularowo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val preferenceUtils = PreferenceUtils(application)
    private var player: MutableLiveData<DetailedPlayer?>? = null

    private var mNetworkErrorListener: NetworkErrorListener? = null

    fun getPlayer(): LiveData<DetailedPlayer?> {
        if (player == null) {
            player = MutableLiveData()
            loadPlayer()
        } else if (player!!.value == null) {
            refreshPlayer()
        }
        return player!!
    }

    fun refreshPlayer() {
        loadPlayer()
    }

    private fun loadPlayer() {
        val accessToken = preferenceUtils.accessToken
        val uuid = preferenceUtils.uuid

        if (accessToken != null && uuid != null) {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getPlayerInfoFromUuid(ServerAPIClient.API_KEY, uuid, accessToken).enqueue(object : Callback<DetailedPlayer?> {
                override fun onResponse(call: Call<DetailedPlayer?>, response: Response<DetailedPlayer?>) {
                    if (response.isSuccessful && response.body() != null) {
                        player?.setValue(response.body())
                    } else {
                        player?.value = null
                        mNetworkErrorListener?.onServerError()
                    }
                }

                override fun onFailure(call: Call<DetailedPlayer?>, t: Throwable) {
                    player?.value = null
                    mNetworkErrorListener?.onNoInternet()
                    t.printStackTrace()
                }
            })
        }
    }
}