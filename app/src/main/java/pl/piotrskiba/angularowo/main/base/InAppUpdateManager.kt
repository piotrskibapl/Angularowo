package pl.piotrskiba.angularowo.main.base

import android.app.Activity
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.clientVersionStalenessDays
import pl.piotrskiba.angularowo.R
import javax.inject.Inject

class InAppUpdateManager @Inject constructor(
    private val appUpdateManager: AppUpdateManager,
) : InstallStateUpdatedListener {

    private lateinit var activity: Activity

    fun init(activity: Activity) {
        this.activity = activity
        checkUpdateAvailability()
        appUpdateManager.registerListener(this)
    }

    fun onResume() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateState ->
            if (updateState.installStatus() == InstallStatus.DOWNLOADED) {
                showAppUpdateDownloadedSnackbar()
            } else if (updateState.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdate(updateState, AppUpdateType.IMMEDIATE)
            }
        }
    }

    fun onDestroy() {
        appUpdateManager.unregisterListener(this)
    }

    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            showAppUpdateDownloadedSnackbar()
        }
    }

    private fun checkUpdateAvailability() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener { updateInfo ->
            if (updateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                val priority = updateInfo.updatePriority()
                val stalenessDays = updateInfo.clientVersionStalenessDays ?: 0
                val immediateUpdateAllowed = updateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                val flexibleUpdateAllowed = updateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                val updateType = when {
                    priority == 5 && immediateUpdateAllowed -> AppUpdateType.IMMEDIATE
                    priority == 5 && flexibleUpdateAllowed -> AppUpdateType.FLEXIBLE
                    priority == 4 && stalenessDays >= 7 && immediateUpdateAllowed -> AppUpdateType.IMMEDIATE
                    priority == 4 && flexibleUpdateAllowed -> AppUpdateType.FLEXIBLE
                    priority == 3 && stalenessDays >= 30 && immediateUpdateAllowed -> AppUpdateType.IMMEDIATE
                    priority == 3 && stalenessDays >= 7 && flexibleUpdateAllowed -> AppUpdateType.FLEXIBLE
                    priority == 2 && stalenessDays >= 90 && immediateUpdateAllowed -> AppUpdateType.IMMEDIATE
                    priority == 2 && stalenessDays >= 30 && flexibleUpdateAllowed -> AppUpdateType.FLEXIBLE
                    priority == 1 && stalenessDays >= 90 && flexibleUpdateAllowed -> AppUpdateType.FLEXIBLE
                    else -> null
                }
                if (updateType != null) {
                    startUpdate(updateInfo, updateType)
                }
            }
        }
    }

    private fun startUpdate(updateInfo: AppUpdateInfo, updateType: Int) {
        appUpdateManager.startUpdateFlowForResult(
            updateInfo,
            activity,
            AppUpdateOptions.defaultOptions(updateType),
            updateType,
        )
    }

    private fun showAppUpdateDownloadedSnackbar() {
        Snackbar.make(
            activity.findViewById(R.id.drawer_layout),
            activity.getString(R.string.in_app_update_downloaded),
            Snackbar.LENGTH_INDEFINITE,
        ).apply {
            setAction(activity.getString(R.string.in_app_update_action_restart)) { appUpdateManager.completeUpdate() }
            show()
        }
    }
}
