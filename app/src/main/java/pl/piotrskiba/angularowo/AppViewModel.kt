package pl.piotrskiba.angularowo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.models.*
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.PreferenceUtils.getAccessToken
import pl.piotrskiba.angularowo.utils.PreferenceUtils.getUsername
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private var serverStatus: MutableLiveData<ServerStatus?>? = null
    private var player: MutableLiveData<DetailedPlayer?>? = null
    private var activePlayerBans: MutableLiveData<BanList?>? = null
    private var banList: MutableLiveData<BanList?>? = null
    private var playerList: MutableLiveData<PlayerList?>? = null
    private var offersInfo: MutableLiveData<OffersInfo?>? = null
    private var userReports: MutableLiveData<ReportList?>? = null
    private var allReports: MutableLiveData<ReportList?>? = null

    private var mNetworkErrorListener: NetworkErrorListener? = null

    fun getServerStatus(): LiveData<ServerStatus?> {
        if (serverStatus == null) {
            serverStatus = MutableLiveData()
            loadServerStatus()
        } else if (serverStatus!!.value == null) {
            refreshServerStatus()
        }
        return serverStatus!!
    }

    fun refreshServerStatus() {
        loadServerStatus()
    }

    private fun loadServerStatus() {
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getServerStatus(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<ServerStatus?> {
            override fun onResponse(call: Call<ServerStatus?>, response: Response<ServerStatus?>) {
                if (response.isSuccessful && response.body() != null) {
                    serverStatus?.setValue(response.body())
                } else {
                    serverStatus?.value = null
                    mNetworkErrorListener?.onServerError()
                }
            }

            override fun onFailure(call: Call<ServerStatus?>, t: Throwable) {
                serverStatus?.value = null
                mNetworkErrorListener?.onNoInternet()
                t.printStackTrace()
            }
        })
    }

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
        val accessToken = getAccessToken(getApplication())
        val username = getUsername(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getPlayerInfo(ServerAPIClient.API_KEY, username!!, accessToken!!).enqueue(object : Callback<DetailedPlayer?> {
            override fun onResponse(call: Call<DetailedPlayer?>, response: Response<DetailedPlayer?>) {
                if (response.isSuccessful && response.body() != null) {
                    player?.setValue(response.body())
                } else {
                    player?.value = null
                    mNetworkErrorListener!!.onServerError()
                }
            }

            override fun onFailure(call: Call<DetailedPlayer?>, t: Throwable) {
                player?.value = null
                mNetworkErrorListener?.onNoInternet()
                t.printStackTrace()
            }
        })
    }

    fun getActivePlayerBans(): LiveData<BanList?> {
        if (activePlayerBans == null) {
            activePlayerBans = MutableLiveData()
            loadActivePlayerBans()
        } else if (activePlayerBans!!.value == null) {
            refreshActivePlayerBans()
        }
        return activePlayerBans!!
    }

    fun refreshActivePlayerBans() {
        loadActivePlayerBans()
    }

    private fun loadActivePlayerBans() {
        val accessToken = getAccessToken(getApplication())
        val username = getUsername(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getActiveBans(ServerAPIClient.API_KEY, Constants.ACTIVE_BAN_TYPES, username!!, accessToken!!).enqueue(object : Callback<BanList?> {
            override fun onResponse(call: Call<BanList?>, response: Response<BanList?>) {
                if (response.isSuccessful && response.body() != null) {
                    activePlayerBans?.setValue(response.body())
                } else {
                    activePlayerBans?.value = null
                    mNetworkErrorListener?.onServerError()
                }
            }

            override fun onFailure(call: Call<BanList?>, t: Throwable) {
                activePlayerBans?.value = null
                mNetworkErrorListener?.onNoInternet()
                t.printStackTrace()
            }
        })
    }

    fun getBanList(): LiveData<BanList?> {
        if (banList == null) {
            banList = MutableLiveData()
            loadBanList()
        } else if (banList!!.value == null) {
            refreshBanList()
        }
        return banList!!
    }

    fun refreshBanList() {
        loadBanList()
    }

    private fun loadBanList() {
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getActiveBans(ServerAPIClient.API_KEY, Constants.BAN_TYPES, null, accessToken!!).enqueue(object : Callback<BanList?> {
            override fun onResponse(call: Call<BanList?>, response: Response<BanList?>) {
                if (response.isSuccessful && response.body() != null) {
                    banList?.value = response.body()
                } else {
                    banList?.value = null
                    mNetworkErrorListener?.onServerError()
                }
            }

            override fun onFailure(call: Call<BanList?>, t: Throwable) {
                banList?.value = null
                mNetworkErrorListener?.onNoInternet()
                t.printStackTrace()
            }
        })
    }

    fun getPlayerList(): LiveData<PlayerList?> {
        if (playerList == null) {
            playerList = MutableLiveData()
            loadPlayerList()
        } else if (playerList!!.value == null) {
            refreshPlayerList()
        }
        return playerList!!
    }

    fun refreshPlayerList() {
        loadPlayerList()
    }

    private fun loadPlayerList() {
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getPlayers(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<PlayerList?> {
            override fun onResponse(call: Call<PlayerList?>, response: Response<PlayerList?>) {
                if (response.isSuccessful && response.body() != null) {
                    playerList?.setValue(response.body())
                } else {
                    playerList?.value = null
                    mNetworkErrorListener?.onServerError()
                }
            }

            override fun onFailure(call: Call<PlayerList?>, t: Throwable) {
                playerList?.value = null
                mNetworkErrorListener?.onNoInternet()
                t.printStackTrace()
            }
        })
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
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getOffersInfo(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<OffersInfo?> {
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
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getUserReports(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<ReportList?> {
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
        val accessToken = getAccessToken(getApplication())

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getAllReports(ServerAPIClient.API_KEY, false, accessToken!!).enqueue(object : Callback<ReportList?> {
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

    fun setNetworkErrorListener(listener: NetworkErrorListener) {
        mNetworkErrorListener = listener
    }
}