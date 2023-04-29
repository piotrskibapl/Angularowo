package pl.piotrskiba.angularowo.data.applock.repository

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel
import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository

private const val TITLE_KEY = "app_lock_title"
private const val BODY_KEY = "app_lock_body"
private const val START_KEY = "app_lock_start_timestamp"
private const val END_KEY = "app_lock_end_timestamp"

class AppLockRepositoryImpl(
    private val firebaseRemoteConfig: FirebaseRemoteConfig,
) : AppLockRepository {

    override fun getAppLockConfig() =
        Single.create { emitter ->
            firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener { result ->
                    if (result.isSuccessful) {
                        emitter.onSuccess(
                            AppLockConfigModel(
                                title = firebaseRemoteConfig.getString(TITLE_KEY),
                                body = firebaseRemoteConfig.getString(BODY_KEY),
                                startTimestamp = firebaseRemoteConfig.getLong(START_KEY),
                                endTimestamp = firebaseRemoteConfig.getLong(END_KEY),
                            )
                        )
                    } else {
                        emitter.onError(result.exception!!)
                    }
                }
        }
}
