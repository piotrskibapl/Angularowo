package pl.piotrskiba.angularowo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.models.OffersInfo
import pl.piotrskiba.angularowo.models.ReportList
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
    private var offersInfo: MutableLiveData<OffersInfo?>? = null
    private var userReports: MutableLiveData<ReportList?>? = null
    private var allReports: MutableLiveData<ReportList?>? = null

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

    fun getOffersInfo(): LiveData<OffersInfo?> {
        if (offersInfo == null) {
            offersInfo = MutableLiveData()
            loadOffersInfo()
        } else if (offersInfo!!.value == null) {
            refreshOffersInfo()
        }
        return offersInfo!!
    }

    fun refreshOffersInfo() {
        loadOffersInfo()
    }

    private fun loadOffersInfo() {
        val accessToken = preferenceUtils.accessToken

        if (accessToken != null) {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getOffersInfo(ServerAPIClient.API_KEY, accessToken).enqueue(object : Callback<OffersInfo?> {
                override fun onResponse(call: Call<OffersInfo?>, response: Response<OffersInfo?>) {
                    if (response.isSuccessful && response.body() != null) {
                        offersInfo?.setValue(response.body())
                    } else {
                        offersInfo?.value = null
                        mNetworkErrorListener?.onServerError()
                    }
                }

                override fun onFailure(call: Call<OffersInfo?>, t: Throwable) {
                    offersInfo?.value = null
                    mNetworkErrorListener?.onNoInternet()
                    t.printStackTrace()
                }
            })
        }
    }

    fun getUserReports(): LiveData<ReportList?> {
        if (userReports == null) {
            userReports = MutableLiveData()
            loadUserReports()
        } else if (userReports!!.value == null) {
            refreshUserReports()
        }
        return userReports!!
    }

    fun refreshUserReports() {
        loadUserReports()
    }

    private fun loadUserReports() {
        val accessToken = preferenceUtils.accessToken

        if (accessToken != null) {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getUserReports(ServerAPIClient.API_KEY, accessToken).enqueue(object : Callback<ReportList?> {
                override fun onResponse(call: Call<ReportList?>, response: Response<ReportList?>) {
                    if (response.isSuccessful && response.body() != null) {
                        userReports?.value = response.body()
                    } else {
                        userReports?.value = null
                        mNetworkErrorListener?.onServerError()
                    }
                }

                override fun onFailure(call: Call<ReportList?>, t: Throwable) {
                    userReports?.value = null
                    mNetworkErrorListener?.onNoInternet()
                    t.printStackTrace()
                }
            })
        }
    }

    fun getAllReports(): LiveData<ReportList?> {
        if (allReports == null) {
            allReports = MutableLiveData()
            loadAllReports()
        } else if (allReports!!.value == null) {
            refreshAllReports()
        }
        return allReports!!
    }

    fun refreshAllReports() {
        loadAllReports()
    }

    private fun loadAllReports() {
        val accessToken = preferenceUtils.accessToken

        if (accessToken != null) {
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.getAllReports(ServerAPIClient.API_KEY, false, accessToken).enqueue(object : Callback<ReportList?> {
                override fun onResponse(call: Call<ReportList?>, response: Response<ReportList?>) {
                    if (response.isSuccessful && response.body() != null) {
                        allReports?.value = response.body()
                    } else {
                        allReports?.value = null
                        mNetworkErrorListener?.onServerError()
                    }
                }

                override fun onFailure(call: Call<ReportList?>, t: Throwable) {
                    allReports?.value = null
                    mNetworkErrorListener?.onNoInternet()
                    t.printStackTrace()
                }
            })
        }
    }

    fun setNetworkErrorListener(listener: NetworkErrorListener) {
        mNetworkErrorListener = listener
    }
}